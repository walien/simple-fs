package com.walien.simplefs.observers;

import com.walien.simplefs.domain.INode;

public class FSEvent {

    private FSEventType type;

    private INode concernedNode;

    private INode targetConcernedNode;

    private FSEvent(FSEventType type, INode concernedNode, INode targetConcernedNode) {
        this.type = type;
        this.concernedNode = concernedNode;
        this.targetConcernedNode = targetConcernedNode;
    }

    public static FSEvent of(FSEventType type, INode concernedNode) {
        return new FSEvent(type, concernedNode, null);
    }

    public static FSEvent of(FSEventType type, INode concernedNode, INode targetConcernedNode) {
        return new FSEvent(type, concernedNode, targetConcernedNode);
    }

    public FSEventType getType() {
        return type;
    }

    public INode getConcernedNode() {
        return concernedNode;
    }

    public INode getTargetConcernedNode() {
        return targetConcernedNode;
    }
}
