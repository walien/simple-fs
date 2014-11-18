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

package com.walien.simplefs.domain.impl;

import com.walien.simplefs.domain.AbstractNode;
import com.walien.simplefs.domain.INode;

public class File extends AbstractNode implements Comparable<INode> {

    private FileType fileType = FileType.TEXT;

    private String content;

    @Override
    public NodeType getNodeType() {
        return NodeType.FILE;
    }

    public FileType getFileType() {
        return fileType;
    }

    public File setFileType(final FileType fileType) {
        this.fileType = fileType;
        return this;
    }

    public String getContent() {
        return content;
    }

    public File setContent(final String content) {
        this.content = content;
        return this;
    }

    @Override
    public String getPath() {
        return parent.getPath() + Constants.PATH_SEPARATOR + getName();
    }

    @Override
    public int compareTo(INode node) {

        if (node.getNodeType() == NodeType.DIR) {
            return 1;
        }
        if (node.getNodeType() == NodeType.FILE) {
            return getName().compareTo(node.getName());
        }
        return 10;
    }
}
