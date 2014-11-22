package com.walien.simplefs.shell.commands;

import com.google.common.collect.ImmutableList;
import com.walien.simplefs.domain.INode;
import com.walien.simplefs.shell.Shell;
import com.walien.simplefs.shell.domain.FSCommand;
import com.walien.simplefs.shell.domain.ShellCommand;

import java.io.IOException;
import java.util.List;

public class LsCommand extends FSCommand {

    public LsCommand() {
        super(ImmutableList.of("ls"), "a command listing the content of a directory");
    }

    @Override
    public ShellCommand start(String line, Shell shell) throws IOException {

        ImmutableList<String> args = args(line);
        if (args.size() > 1) {
            return error(shell, "use 'ls {path}'");
        }

        List<INode> nodes = getFSManager().ls(args.size() > 0 ? args.get(0) : null);
        if (nodes == null) {
            return error(shell, "'" + args.get(0) + "' not found");
        }

        for (INode node : nodes) {
            shell.append(node.getNodeType().name().substring(0, 3) + " - " + node.getName());
        }

        return this;
    }
}
