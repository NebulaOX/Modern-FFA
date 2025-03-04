package org.plugin.modernffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.plugin.modernffa.ModernFFA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final ModernFFA plugin;
    private final List<SubCommand> commands = new ArrayList<>();

    public CommandManager(ModernFFA plugin) {
        this.plugin = plugin;
        initializeCommands();
    }

    private void initializeCommands() {
        commands.addAll(Arrays.asList(
                new SetSpawnCommand(plugin),
                new SetArenaCommand(plugin),
                new SetVipArenaCommand(plugin),
                new VipArenaCommand(plugin),
                new ArenaCommand(plugin),
                new CreateKitCommand(plugin),
                new KitCommand(plugin),
                new SetPositionResetMapCommand(plugin),
                new EnableResetArenaCommand(plugin)
        ));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ffa")) {
            if (args.length == 0) {
                sender.sendMessage("Usage: /ffa <command>");
                return true;
            }
            String subCommandName = args[0];
            SubCommand subCommand = getSubCommand(subCommandName);
            if (subCommand != null) {
                return subCommand.execute(sender, args);
            } else {
                sender.sendMessage("Unknown command. Type \"/ffa help\" for help.");
                return true;
            }
        }
        return false;
    }

    private SubCommand getSubCommand(String name) {
        for (SubCommand command : commands) {
            if (command.getName().equalsIgnoreCase(name)) {
                return command;
            }
        }
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ffa")) {
            if (args.length == 1) {
                return getCommandNames(args[0]);
            } else if (args.length >= 2) {
                SubCommand subCommand = getSubCommand(args[0]);
                if (subCommand != null) {
                    return subCommand.onTabComplete(sender, cmd, alias, args);
                }
            }
        }
        return Collections.emptyList();
    }

    private List<String> getCommandNames(String prefix) {
        return commands.stream()
                .map(SubCommand::getName)
                .filter(name -> name.startsWith(prefix))
                .collect(Collectors.toList());
    }
}
