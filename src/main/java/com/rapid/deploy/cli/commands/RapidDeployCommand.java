package com.rapid.deploy.cli.commands;

import com.rapid.deploy.cli.commands.subcommands.AppsCommand;
import com.rapid.deploy.cli.commands.subcommands.CreateAppCommand;
import com.rapid.deploy.cli.commands.subcommands.PushCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "rd", mixinStandardHelpOptions = true, subcommands = {CreateAppCommand.class, AppsCommand.class, PushCommand.class})
public class RapidDeployCommand implements Callable<Integer> {

    public Integer call() {
        System.out.println("Please use one of the sub commands");
        return 0;
    }
}
