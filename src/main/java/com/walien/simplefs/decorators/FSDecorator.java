package com.walien.simplefs.decorators;

import com.walien.simplefs.domain.INode;

public interface FSDecorator<T extends INode> {

    String decorate(T node);
}
