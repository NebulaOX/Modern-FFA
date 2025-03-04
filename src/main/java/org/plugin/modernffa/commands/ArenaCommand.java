package org.plugin.modernffa.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.plugin.modernffa.ModernFFA;
import org.plugin.modernffa.models.Arena;

import java.util.ArrayList;
import java.util.List;

public class ArenaCommand extends SubCommand {

    public ArenaCommand(ModernFFA plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /ffa arena <name>");
            return true;
        }

        String arenaName = args[1];
        Arena arena = plugin.getArenas().get(arenaName);

        if (arena == null) {
            player.sendMessage(ChatColor.RED + "Arena " + arenaName + " does not exist.");
            return true;
        }

        teleportPlayerToArena(player, arena);
        displayArenaHeading(player, arenaName);

        return true;
    }

    @Override
    public List<String> getAliases() {
        return List.of();
    }

    private void teleportPlayerToArena(Player player, Arena arena) {
        Location location = (Location) arena.getLocation();
        if (location != null) {
            player.teleport(location);
            player.sendMessage(ChatColor.GREEN + "Teleported to arena " + ChatColor.YELLOW + arena.getName() + ChatColor.GREEN + ".");
        } else {
            player.sendMessage(ChatColor.RED + "The location for arena " + arena.getName() + " is not set.");
        }
    }

    private void displayArenaHeading(Player player, String arenaName) {
        String heading = ChatColor.GOLD + "" + ChatColor.BOLD + arenaName;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName() + " title " + heading);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 2) {
            return new ArrayList<>(plugin.getArenas().keySet());
        }
        return new ArrayList<>();
    }
}
