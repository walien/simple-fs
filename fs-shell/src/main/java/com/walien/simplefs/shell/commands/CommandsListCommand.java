package com.walien.simplefs.shell.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.walien.simplefs.shell.Shell;
import com.walien.simplefs.shell.domain.AbstractShellCommand;
import com.walien.simplefs.shell.domain.ShellCommand;

import java.io.IOException;

public class CommandsListCommand extends AbstractShellCommand {

    public CommandsListCommand() {
        super(ImmutableList.of("commands"), "A command returning the list of all registered commands");
    }

    @Override
    public ShellCommand start(String line, Shell shell) throws IOException {

        ImmutableList<AbstractShellCommand> commands = shell.repository().commands();

        if (commands.isEmpty()) {
            return error(shell, "no commands founds...");
        } else {

            StringBuilder builder = new StringBuilder();
            for (AbstractShellCommand shellCommand : commands) {
                builder.append("(").append(Joiner.on(", ").join(shellCommand.getAliases())).append(")")
                        .append(" : ")
                        .append(shellCommand.getDescription())
                        .append("\n");
            }

            shell.append(builder);
        }

        return this;
    }
}
