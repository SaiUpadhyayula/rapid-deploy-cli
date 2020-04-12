package com.rapid.deploy.cli;

import com.rapid.deploy.cli.commands.RapidDeployCommand;
import picocli.CommandLine;

public class RapidDeployCliRunner {

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new RapidDeployCommand());
        commandLine.execute(args);
    }
}
