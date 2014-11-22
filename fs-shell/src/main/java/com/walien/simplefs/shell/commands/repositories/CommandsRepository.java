package com.walien.simplefs.shell.commands.repositories;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.walien.simplefs.shell.domain.AbstractShellCommand;

public interface CommandsRepository {

    CommandsRepository register(AbstractShellCommand command);

    CommandsRepository remove(String alias);

    Optional<AbstractShellCommand> find(String alias);

    ImmutableList<AbstractShellCommand> commands();
}
