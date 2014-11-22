package com.walien.simplefs.shell.commands;

import com.google.common.collect.ImmutableList;
import com.walien.simplefs.decorators.TreeDecorator;
import com.walien.simplefs.domain.impl.Directory;
import com.walien.simplefs.shell.Shell;
import com.walien.simplefs.shell.domain.FSCommand;
import com.walien.simplefs.shell.domain.ShellCommand;

import java.io.IOException;

public class TreeCommand extends FSCommand {

    public TreeCommand() {
        super(ImmutableList.of("tree"), "a command displaying the tree architecture from a folder");
    }

    @Override
    public ShellCommand start(String line, Shell shell) throws IOException {

        ImmutableList<String> args = args(line);
        if (args.size() != 1) {
            return error(shell, "use 'tree {path}'");
        }

        Directory directory = getFSManager().getDirectory(args.get(0));
        if (directory == null) {
            return error(shell, "directory not found : " + args.get(0));
        }

        shell.append(new TreeDecorator().decorate(directory));

        return this;
    }
}
