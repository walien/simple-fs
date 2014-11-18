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

package com.walien.simplefs.domain;

import com.walien.simplefs.domain.impl.Directory;
import com.walien.simplefs.domain.impl.NodeType;
import com.walien.simplefs.domain.impl.User;

public interface INode {

    String getName();

    INode setName(String name);

    Directory getParent();

    INode setParent(Directory d);

    NodeType getNodeType();

    String getPath();

    User getOwner();

    INode setOwner(User owner);
}
