package com.walien.simplefs.shell.commands.repositories;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.walien.simplefs.shell.domain.AbstractShellCommand;
import com.walien.simplefs.shell.domain.ShellCommand;

import java.util.HashSet;
import java.util.Set;

public class MemoryCommandsRepository implements CommandsRepository {

    private Set<AbstractShellCommand> commands = new HashSet<>();

    @Override
    public ImmutableList<AbstractShellCommand> commands() {
        return ImmutableList.copyOf(commands);
    }

    @Override
    public CommandsRepository register(AbstractShellCommand command) {
        commands.add(command);
        return this;
    }

    @Override
    public Optional<AbstractShellCommand> find(final String alias) {
        return Iterables.tryFind(commands, new Predicate<ShellCommand>() {
            @Override
            public boolean apply(ShellCommand input) {
                return input.getAliases().contains(alias);
            }
        });
    }

    @Override
    public CommandsRepository remove(final String alias) {
        Optional<AbstractShellCommand> command = find(alias);
        if (command.isPresent()) {
            commands.remove(command.get());
        }
        return this;
    }
}
