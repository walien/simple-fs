package com.walien.simplefs.shell;

import com.walien.simplefs.FileSystemManager;
import com.walien.simplefs.domain.impl.Directory;
import com.walien.simplefs.shell.commands.*;
import com.walien.simplefs.shell.commands.repositories.CommandsRepository;
import com.walien.simplefs.shell.commands.repositories.MemoryCommandsRepository;
import com.walien.simplefs.shell.domain.AnsiCodes;
import com.walien.simplefs.shell.domain.ShellCommand;
import com.walien.simplefs.shell.domain.exceptions.CommandNotFoundException;
import com.walien.simplefs.shell.domain.exceptions.ExitShell;
import jline.console.ConsoleReader;
import jline.console.completer.Completer;

import java.io.IOException;

public class SimpleFSShell implements Shell {

    private static final String PROMPT = "> ";

    private final ConsoleReader console;
    private final CommandsRepository repository;

    public SimpleFSShell(ConsoleReader console, CommandsRepository repository) {
        this.console = console;
        this.repository = repository;
    }

    public static void main(String[] args) throws Exception {

        FileSystemManager.getInstance().setRootDir((Directory) new Directory().setName("/"));

        MemoryCommandsRepository repository = new MemoryCommandsRepository();
        repository
                .register(new ManCommand())
                .register(new ExitCommand())
                .register(new CommandsListCommand())
                .register(new CdCommand())
                .register(new PwdCommand())
                .register(new LsCommand())
                .register(new MkdirCommand());

        new SimpleFSShell(new ConsoleReader(), repository).start();
    }

    protected void initConsole() {
        console.setPrompt(PROMPT);
        console.setHistoryEnabled(true);
    }

    protected void banner() throws IOException {
        console.println("===============================================================================");
        console.println("== Welcome to simplefs - by walien");
        console.println("===============================================================================");
    }

    protected void start() throws IOException {

        initConsole();
        banner();
        installCompleters();

        boolean exit = false;

        while (!exit) {

            String line = ask();

            try {
                doExec(line);
            } catch (CommandNotFoundException e) {
                append(e.getMessage());
            } catch (ExitShell e) {
                exit = terminate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean terminate() throws IOException {
        append("Bye !");
        console.shutdown();
        return true;
    }

    protected void installCompleters() {
        for (ShellCommand shellCommand : repository.commands()) {
            for (Completer completer : shellCommand.getCompleters()) {
                console.addCompleter(completer);
            }
        }
    }

    protected void doExec(String line) throws IOException, CommandNotFoundException {

        for (ShellCommand shellCommand : repository.commands()) {

            if (shellCommand.match(line)) {
                shellCommand.start(line, this);
                return;
            }
        }

        throw new CommandNotFoundException(line);
    }

    protected String ask() throws IOException {
        return console.readLine();
    }

    @Override
    public CommandsRepository repository() {
        return repository;
    }

    @Override
    public Shell append(CharSequence csq, String ansiCode) throws IOException {
        console.println(ansiCode + csq + AnsiCodes.ANSI_RESET);
        return this;
    }

    @Override
    public Shell append(CharSequence csq) throws IOException {
        console.println(csq);
        return this;
    }
}
