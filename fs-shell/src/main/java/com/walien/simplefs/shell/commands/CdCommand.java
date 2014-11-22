package com.walien.simplefs.shell.commands;

import com.google.common.collect.ImmutableList;
import com.walien.simplefs.domain.impl.Directory;
import com.walien.simplefs.shell.Shell;
import com.walien.simplefs.shell.domain.FSCommand;
import com.walien.simplefs.shell.domain.ShellCommand;

import java.io.IOException;

public class CdCommand extends FSCommand {

    public CdCommand() {
        super(ImmutableList.of("cd"), "changes the current directory");
    }

    @Override
    public ShellCommand start(String line, Shell shell) throws IOException {

        ImmutableList<String> args = args(line);
        if (args.size() != 1) {
            return error(shell, "use 'cd {path}");
        }

        Directory cd = getFSManager().cd(args.get(0));
        shell.append(cd.getPath());

        return this;
    }
}
