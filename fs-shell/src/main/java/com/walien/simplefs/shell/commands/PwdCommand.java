package com.walien.simplefs.shell.commands;

import com.google.common.collect.ImmutableList;
import com.walien.simplefs.shell.Shell;
import com.walien.simplefs.shell.domain.FSCommand;
import com.walien.simplefs.shell.domain.ShellCommand;

import java.io.IOException;

public class PwdCommand extends FSCommand {

    public PwdCommand() {
        super(ImmutableList.of("pwd"), "displays the current directory");
    }

    @Override
    public ShellCommand start(String line, Shell shell) throws IOException {

        shell.append(getFSManager().pwd().getPath());
        return this;
    }
}
