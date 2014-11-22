package com.walien.simplefs.shell.commands;

import com.google.common.collect.ImmutableList;
import com.walien.simplefs.shell.Shell;
import com.walien.simplefs.shell.domain.AbstractShellCommand;
import com.walien.simplefs.shell.domain.ShellCommand;
import com.walien.simplefs.shell.domain.exceptions.ExitShell;

import java.io.IOException;

public class ExitCommand extends AbstractShellCommand {

    public ExitCommand() {
        super(ImmutableList.of("exit", "quit"), "quit the shell app");
    }

    @Override
    public ShellCommand start(String line, Shell shell) throws IOException {
        throw new ExitShell();
    }
}
