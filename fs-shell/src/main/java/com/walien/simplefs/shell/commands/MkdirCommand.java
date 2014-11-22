package com.walien.simplefs.shell.commands;

import com.google.common.collect.ImmutableList;
import com.walien.simplefs.shell.Shell;
import com.walien.simplefs.shell.domain.FSCommand;
import com.walien.simplefs.shell.domain.ShellCommand;

import java.io.IOException;

public class MkdirCommand extends FSCommand {

    public MkdirCommand() {
        super(ImmutableList.of("mkdir"), "a command creating a folder");
    }

    @Override
    public ShellCommand start(String line, Shell shell) throws IOException {

        ImmutableList<String> args = args(line);
        if (args.size() != 1) {
            return error(shell, "use 'mkdir {path}'");
        }

        getFSManager().mkdir(args.get(0));

        return this;
    }
}
