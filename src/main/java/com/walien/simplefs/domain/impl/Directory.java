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

import java.util.HashMap;
import java.util.Map;

public class Directory extends AbstractNode implements Comparable<INode> {

    private Map<String, INode> children = new HashMap<>();

    @Override
    public NodeType getNodeType() {
        return NodeType.DIR;
    }

    public Map<String, INode> getChildren() {
        return children;
    }

    public Directory setChildren(final Map<String, INode> children) {
        this.children = children;
        return this;
    }

    @Override
    public String getPath() {
        if (isRoot()) {
            return name;
        }
        if (parent.isRoot()) {
            return parent.getPath() + getName();
        }
        return parent.getPath() + Constants.PATH_SEPARATOR + getName();
    }

    public boolean isRoot() {
        return parent == null;
    }

    public void addChildNode(INode node) {
        children.put(node.getName(), node);
    }

    @Override
    public int compareTo(INode node) {

        if (node.getNodeType() == NodeType.FILE) {
            return -1;
        }
        if (node.getNodeType() == NodeType.DIR) {
            return getName().compareTo(node.getName());
        }
        return 10;
    }
}
