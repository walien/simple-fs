package com.walien.simplefs.shell.commands;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.walien.simplefs.shell.Shell;
import com.walien.simplefs.shell.domain.AbstractShellCommand;
import com.walien.simplefs.shell.domain.ShellCommand;

import java.io.IOException;

public class ManCommand extends AbstractShellCommand {

    public ManCommand() {
        super(ImmutableList.of("man"), "A command returning another command manual");
    }

    @Override
    public ShellCommand start(String line, Shell shell) throws IOException {

        ImmutableList<String> args = args(line);
        String commandAlias = args.get(0);

        if (commandAlias == null) {
            return error(shell, "use : 'man {commandAlias}'");
        }

        Optional<AbstractShellCommand> shellCommand = shell.repository().find(commandAlias);
        if (!shellCommand.isPresent()) {
            return error(shell, "command not found : '" + commandAlias + "'");
        }

        shellCommand.get().man(shell);

        return this;
    }
}
