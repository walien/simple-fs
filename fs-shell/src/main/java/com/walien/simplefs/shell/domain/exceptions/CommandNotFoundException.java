package com.walien.simplefs.shell.domain.exceptions;

public class CommandNotFoundException extends Exception {

    public CommandNotFoundException(String line) {
        super("No command found for input : '" + line + "'");
    }
}
