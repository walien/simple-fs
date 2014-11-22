package com.walien.simplefs.shell.commands;

import com.google.common.collect.ImmutableList;
import com.walien.simplefs.shell.Shell;
import com.walien.simplefs.shell.domain.FSCommand;
import com.walien.simplefs.shell.domain.ShellCommand;

import java.io.IOException;

public class TouchCommand extends FSCommand {

    public TouchCommand() {
        super(ImmutableList.of("touch"), "a command creating a file");
    }

    @Override
    public ShellCommand start(String line, Shell shell) throws IOException {

        ImmutableList<String> args = args(line);
        if (args.size() != 1) {
            return error(shell, "use 'touch {path}'");
        }

        getFSManager().touch(args.get(0));

        return this;
    }
}
