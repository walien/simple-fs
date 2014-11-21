package com.walien.simplefs.domain;

import com.walien.simplefs.domain.impl.Directory;
import com.walien.simplefs.domain.impl.User;

import java.io.Serializable;

public abstract class AbstractNode implements INode, Serializable {

    protected String name;

    protected Directory parent;

    protected User owner;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Directory getParent() {
        return parent;
    }

    @Override
    public User getOwner() {
        return owner;
    }

    @Override
    public AbstractNode setName(final String name) {
        this.name = name;
        return this;
    }

    @Override
    public AbstractNode setParent(final Directory parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public AbstractNode setOwner(final User owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public String toString() {
        return "AbstractNode{" +
                "name='" + name + '\'' +
                ", parent=" + parent +
                ", owner=" + owner +
                '}';
    }
}
