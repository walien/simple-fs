package com.walien.simplefs.shell.domain;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.walien.simplefs.shell.Shell;
import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public abstract class AbstractShellCommand implements ShellCommand {

    private final String shortDescription;
    private final List<String> aliases;

    public AbstractShellCommand(List<String> aliases, String description) {
        this.aliases = aliases;
        this.shortDescription = description;
    }

    protected ImmutableList<String> args(String line) {
        int indexOf = line.indexOf(" ");
        if (indexOf == -1) {
            return ImmutableList.of();
        }
        return ImmutableList.copyOf(line.substring(indexOf).trim().split(" "));
    }

    @Override
    public void help(Shell shell) throws IOException {
        man(shell);
    }

    @Override
    public boolean match(String line) {
        for (String alias : aliases) {
            if (line.equals(alias) || line.startsWith(alias + " ")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void man(Shell shell) throws IOException {
        shell.append("Description : " + getDescription());
        shell.append("Aliases : " + Joiner.on(", ").join(getAliases()));
    }

    @Override
    public Iterable<Completer> getCompleters() {
        return Collections.<Completer>singleton(new StringsCompleter(aliases));
    }

    @Override
    public ImmutableList<String> getAliases() {
        return ImmutableList.copyOf(aliases);
    }

    @Override
    public String getDescription() {
        return shortDescription;
    }

    protected ShellCommand error(Shell shell, CharSequence message) throws IOException {
        shell.append(message, AnsiCodes.ANSI_RED);
        return this;
    }

    protected ShellCommand highlight(Shell shell, CharSequence message) throws IOException {
        shell.append(message, AnsiCodes.ANSI_PURPLE);
        return this;
    }

    protected ShellCommand success(Shell shell, CharSequence message) throws IOException {
        shell.append(message, AnsiCodes.ANSI_GREEN);
        return this;
    }
}
