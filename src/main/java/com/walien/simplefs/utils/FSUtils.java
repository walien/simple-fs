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

package com.walien.simplefs.utils;

import com.walien.simplefs.domain.INode;
import com.walien.simplefs.domain.impl.Directory;
import com.walien.simplefs.domain.impl.File;

public class FSUtils {

    public static INode cloneNode(INode node) {

        switch (node.getNodeType()) {
            case DIR:
                return FSUtils.cloneDir((Directory) node);
            case FILE:
                return FSUtils.cloneFile((File) node);
        }

        return null;
    }

    public static File cloneFile(File f) {
        return (File) new File()
                .setFileType(f.getFileType())
                .setContent(f.getContent())
                .setName(f.getName())
                .setOwner(f.getOwner());
    }

    public static Directory cloneDir(Directory dir) {

        Directory newDir = (Directory) new Directory()
                .setName(dir.getName())
                .setOwner(dir.getOwner());

        for (INode node : dir.getChildren().values()) {
            switch (node.getNodeType()) {
                case DIR:
                    Directory childDir = (Directory) cloneDir((Directory) node)
                            .setParent(newDir);
                    newDir.addChildNode(childDir);
                case FILE:
                    File childFile = (File) cloneFile((File) node)
                            .setParent(newDir);
                    newDir.addChildNode(childFile);
            }
        }

        return newDir;
    }
}
