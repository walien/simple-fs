package com.walien.simplefs.observers;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class FSEventManager {

    private List<FSObserver> observers = new ArrayList<FSObserver>();

    public void register(FSObserver observer) {
        observers.add(observer);
    }

    public List<FSObserver> observers() {
        return ImmutableList.copyOf(observers);
    }
}
