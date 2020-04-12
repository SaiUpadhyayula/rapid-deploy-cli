package com.rapid.deploy.cli.commands.subcommands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "login")
public class LoginCommand implements Runnable {

    @Option(names = {"-u", "--username"})
    private String username;
    @Option(names = {"-p", "--password"})
    private char[] password;

    public void run() {

    }
}
