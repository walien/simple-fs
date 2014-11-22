package com.walien.simplefs.shell;

import com.walien.simplefs.shell.commands.repositories.CommandsRepository;

import java.io.IOException;

public interface Shell {

    CommandsRepository repository();

    Shell append(CharSequence csq, String ansiCode) throws IOException;

    Shell append(CharSequence csq) throws IOException;
}
