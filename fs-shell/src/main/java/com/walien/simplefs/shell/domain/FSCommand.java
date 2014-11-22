package com.walien.simplefs.shell.domain;

import com.walien.simplefs.FileSystemManager;
import com.walien.simplefs.IFSManager;

import java.util.List;

public abstract class FSCommand extends AbstractShellCommand {

    public FSCommand(List<String> aliases, String description) {
        super(aliases, description);
    }

    protected IFSManager getFSManager() {
        return FileSystemManager.getInstance();
    }
}
