package org.plugin.modernffa.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.plugin.modernffa.ModernFFA;
import org.plugin.modernffa.models.Arena;

import java.util.List;

public class SetVipArenaCommand extends SubCommand {

    public SetVipArenaCommand(ModernFFA plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (isPlayer(sender)) return true;

        Player player = (Player) sender;
        if (hasPermission(player, "ffa.setviparena")) return true;

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /ffa setviparena <name>");
            return true;
        }

        String arenaName = args[1];
        if (plugin.getArenas().containsKey(arenaName)) {
            player.sendMessage(ChatColor.RED + "An arena with the name " + arenaName + " already exists.");
            return true;
        }

        Location location = player.getLocation();
        if (location.getWorld() == null) {
            player.sendMessage(ChatColor.RED + "Invalid world. Please try again.");
            return true;
        }

        SetPositionResetMapCommand.ArenaSelection selection = null;
        Arena vipArena = new Arena(arenaName, selection.getMin(), location);
        plugin.setVipArena(vipArena);
        plugin.getConfig().set("vipArena.location", serializeLocation(location));
        plugin.saveConfig();

        player.sendMessage(ChatColor.GREEN + "VIP Arena " + arenaName + " set successfully at your current location.");
        logger.info(player.getName() + " set the VIP Arena location for " + arenaName + " to " + locationToString(location));

        return true;
    }

    @Override
    public List<String> getAliases() {
        return List.of("setvipa");
    }

    @Override
    protected List<String> onSubCommandTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 2) {
            return List.of("<arena_name>");
        }
        return super.onSubCommandTabComplete(sender, cmd, alias, args);
    }
}
