package com.walien.simplefs.shell.domain;

import com.google.common.collect.ImmutableList;
import com.walien.simplefs.shell.Shell;
import jline.console.completer.Completer;

import java.io.IOException;

public interface ShellCommand {

    boolean match(String line);

    void help(Shell shell) throws IOException;

    void man(Shell shell) throws IOException;

    String getDescription();

    Iterable<Completer> getCompleters();

    ImmutableList<String> getAliases();

    ShellCommand start(String line, Shell shell) throws IOException;
}