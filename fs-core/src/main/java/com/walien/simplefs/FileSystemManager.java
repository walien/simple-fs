/*
 * Copyright 2014 - Elian ORIOU
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.walien.simplefs;

import com.walien.simplefs.domain.INode;
import com.walien.simplefs.domain.impl.Constants;
import com.walien.simplefs.domain.impl.Directory;
import com.walien.simplefs.domain.impl.File;
import com.walien.simplefs.domain.impl.NodeType;
import com.walien.simplefs.observers.FSEvent;
import com.walien.simplefs.observers.FSEventManager;
import com.walien.simplefs.observers.FSEventType;
import com.walien.simplefs.observers.FSObserver;
import com.walien.simplefs.utils.FSUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FileSystemManager implements IFSManager {

    private static FileSystemManager _INSTANCE;

    private Directory rootDir;

    private Directory currentDir;

    private FSEventManager fsEventManager = new FSEventManager();

    private FileSystemManager() {
    }

    public static FileSystemManager getInstance() {
        if (_INSTANCE == null) {
            _INSTANCE = new FileSystemManager();
        }
        return _INSTANCE;
    }

    public FileSystemManager setRootDir(Directory rootFolder) {
        this.rootDir = this.currentDir = rootFolder;
        return this;
    }

    public Directory getRootDir() {
        return rootDir;
    }

    public FileSystemManager register(FSObserver observer) {
        fsEventManager.register(observer);
        return this;
    }

    private FileSystemManager launchEvent(FSEvent event) {
        for (FSObserver fsObserver : fsEventManager.observers()) {
            fsObserver.onEvent(event);
        }
        return this;
    }

    // //////////////////////////
    // / BASE METHODS
    // //////////////////////////

    private INode exploreRecursively(INode src, List<String> elements, NodeType nodeTypeToCreate) {

        // Case dir/file/dir : Error !
        if (src.getNodeType() != NodeType.DIR && elements.size() > 0) {
            return null;
        }

        // Get the node name and consume the path element
        String nodeName = elements.remove(0);

        // Get the new source node
        INode newSource;
        switch (nodeName) {
            case Constants.CURRENT_DIR_PATH:
                newSource = src;
                break;
            case Constants.PARENT_DIR_PATH:
                newSource = src.getParent();
                break;
            default:
                newSource = ((Directory) src).getChildren().get(nodeName);
                break;
        }

        // Continue
        if (elements.size() == 0) {
            // Case dir/file : Done !
            if (newSource == null && nodeTypeToCreate != null) {
                // Create a new node
                switch (nodeTypeToCreate) {
                    case DIR:
                        INode newDir = new Directory().setName(nodeName).setParent((Directory) src);
                        ((Directory) src).addChildNode(newDir);
                        launchEvent(FSEvent.of(FSEventType.MKDIR, newDir));
                        return newDir;
                    case FILE:
                        INode newFile = new File().setName(nodeName).setParent((Directory) src);
                        ((Directory) src).addChildNode(newFile);
                        launchEvent(FSEvent.of(FSEventType.MKFILE, newFile));
                        return newFile;
                }
            } else {
                return newSource;
            }
        } else {
            // Case dir/dir/[...] : Continue !
            return exploreRecursively(newSource, elements, nodeTypeToCreate);
        }
        return null;
    }

    private List<String> cleanPath(List<String> elts) {

        List<String> result = new ArrayList<>();
        for (String path : elts) {
            if (null == path || path.isEmpty()) {
                continue;
            }
            result.add(path);
        }
        return result;
    }

    public INode getNode(String path, NodeType nodeTypeToCreate) {

        // "." => CURRENT
        if (path.equals(Constants.CURRENT_DIR_PATH)) {
            return currentDir;
        }
        // ".." => PARENT
        if (path.equals(Constants.PARENT_DIR_PATH)) {
            if (currentDir == rootDir) {
                return rootDir;
            }
            return currentDir.getParent();
        }
        // "/" => ROOT
        if (path.equals(Constants.PATH_SEPARATOR)) {
            return rootDir;
        }
        // "/[...]" => EXPLORE FROM THE ROOT
        if (path.startsWith(Constants.PATH_SEPARATOR)) {
            List<String> elts = Arrays.asList(path.split(Constants.PATH_SEPARATOR));
            return exploreRecursively(rootDir, cleanPath(elts), nodeTypeToCreate);
        }
        // "[...]/[...]" => EXPLORE FROM THE CURRENT DIR
        else if (path.contains(Constants.PATH_SEPARATOR)) {
            List<String> elts = Arrays.asList(path.split(Constants.PATH_SEPARATOR));
            return exploreRecursively(currentDir, cleanPath(elts), nodeTypeToCreate);
        }
        // "[...]" => EXPLORE INTO THE CURRENT DIR
        else {
            // Create a node into the current directory
            if (nodeTypeToCreate != null) {
                ArrayList<String> list = new ArrayList<>();
                list.add(path);
                return exploreRecursively(currentDir, list, nodeTypeToCreate);
            }
            // Returns the current directory child
            return currentDir.getChildren().get(path);
        }
    }

    private INode deleteNode(String path, NodeType expectedType) {

        // 1. The node to delete
        INode nodeToDelete = getNode(path, null);
        if (null == nodeToDelete) {
            return null;
        }
        if (expectedType != null && nodeToDelete.getNodeType() != expectedType) {
            return null;
        }

        // 2. The node where delete the node
        INode parentNode = nodeToDelete.getParent();
        if (null == parentNode || parentNode.getNodeType() != NodeType.DIR) {
            return null;
        }

        // 3. Avoid infinite linking
        Iterator<INode> it = ((Directory) parentNode).getChildren().values().iterator();
        for (; it.hasNext(); ) {
            if (it.next() == nodeToDelete) {
                it.remove();
                return nodeToDelete;
            }
        }

        return null;
    }

    private boolean copyNode(INode src, INode dest) {

        // 1. Check types
        if (dest.getNodeType() != NodeType.DIR) {
            return false;
        }
        Directory destDir = (Directory) dest;

        // 2. Clone the node to copy (if it is a directory, cloning operation
        // will be recursive) and add it as child of the dest directory
        INode clonedNode = FSUtils.cloneNode(src);
        clonedNode.setParent(destDir);
        destDir.addChildNode(clonedNode);

        return true;
    }

    public Directory getDirectory(String path) {
        INode node = getNode(path, null);
        if (null == node || node.getNodeType() != NodeType.DIR) {
            return null;
        }
        return (Directory) node;
    }

    public File getFile(String path) {
        INode node = getNode(path, null);
        if (null == node || node.getNodeType() != NodeType.FILE) {
            return null;
        }
        return (File) node;
    }

    // /////////////////////////
    // FS MANAGEMENT METHODS
    // /////////////////////////

    @Override
    public Directory pwd() {
        return currentDir;
    }

    @Override
    public Directory cd(String path) {
        Directory dirChanged = getDirectory(path);
        if (dirChanged == null) {
            return null;
        }
        currentDir = dirChanged;
        return currentDir;
    }

    @Override
    public List<INode> ls(String path) {
        if (path == null) {
            path = ".";
        }
        Directory dir = cd(path);
        if (dir == null) {
            return null;
        }
        return new ArrayList<>(dir.getChildren().values());
    }

    @Override
    public boolean mkdir(String path) {
        return (null != getNode(path, NodeType.DIR));
    }

    @Override
    public boolean touch(String path) {
        return (null != getNode(path, NodeType.FILE));
    }

    @Override
    public boolean rm(String path) {

        // 1. Delete the associate node
        INode deletedNode = deleteNode(path, null);

        // 2. Check if the node has been deleted
        if (null == deletedNode) {
            return false;
        }

        // 3. Launch event
        switch (deletedNode.getNodeType()) {
            case DIR:
                launchEvent(FSEvent.of(FSEventType.RMDIR, deletedNode));
                break;
            case FILE:
                launchEvent(FSEvent.of(FSEventType.RMFILE, deletedNode));
                break;
        }

        return true;
    }

    @Override
    public boolean mv(String src, String dest) {

        // 1. Get the source node
        INode srcNode = getNode(src, null);
        if (srcNode == null) {
            return false;
        }

        // 2. Get the destination node
        Directory destNode = getDirectory(dest);
        if (destNode == null) {
            destNode = getDirectory(dest + "/..");
            if (destNode == null) {
                return false;
            }
        }

        // 3. Copy the source into the destination
        boolean copyResult = copyNode(srcNode, destNode);

        // 4. Delete the source node
        INode deletedNode = deleteNode(src, null);

        return copyResult && (deletedNode != null);
    }

    @Override
    public boolean rename(String path, String newName) {

        // 1. The node to rename
        INode node = getNode(path, null);
        if (node == null) {
            return false;
        }

        // 2. Rename the node
        node.setName(newName);

        // 3. Launch event
        launchEvent(FSEvent.of(FSEventType.RENAME, node));

        // 4. Return true if the renaming has been done successfuly
        return newName.equals(node.getName());
    }

    @Override
    public boolean cp(String src, String dest) {

        // 1. Get the source node
        INode srcNode = getNode(src, null);
        if (srcNode == null) {
            return false;
        }

        // 2. Get the destination node
        Directory destNode = getDirectory(dest);
        if (destNode == null) {
            destNode = getDirectory(dest + "/..");
            if (destNode == null) {
                return false;
            }
        }

        // 3. Copy the source node and add it as a destination node child
        copyNode(srcNode, destNode);

        // 4. Launch event
        launchEvent(FSEvent.of(FSEventType.CP, srcNode, destNode));

        return true;
    }

    @Override
    public String cat(String path) {
        File node = getFile(path);
        if (null == node) {
            return null;
        }
        return node.getContent();
    }
}
