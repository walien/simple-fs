package com.walien.simplefs.decorators;

import com.walien.simplefs.domain.INode;
import com.walien.simplefs.domain.impl.Constants;
import com.walien.simplefs.domain.impl.Directory;
import com.walien.simplefs.domain.impl.NodeType;

public class TreeDecorator implements FSDecorator<Directory> {

    private int _DEEP = 0;

    @Override
    public String decorate(Directory rootDir) {
        if (rootDir == null) {
            return null;
        }
        String text = dir(rootDir);
        _DEEP += 1;
        for (INode node : rootDir.getChildren().values()) {
            for (int i = 0; i < _DEEP; i++) {
                text += "\t";
            }
            if (node.getNodeType() == NodeType.DIR) {
                Directory dir = (Directory) node;
                if (dir.getChildren().size() > 0) {
                    text += decorate(dir);
                    _DEEP -= 1;
                } else {
                    text += dir(dir);
                }
            } else {
                text += file(node);
            }
        }
        return text;
    }

    private String file(INode node) {
        return "(FIL) - " + node.getName() + "\n";
    }

    private String dir(Directory dir) {
        return "(DIR) - " + dir.getName() + (!Constants.PATH_SEPARATOR.equals(dir.getName()) ? Constants.PATH_SEPARATOR : "") + "\n";
    }
}
