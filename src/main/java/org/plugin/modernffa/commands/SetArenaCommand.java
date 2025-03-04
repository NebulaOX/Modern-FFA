package org.plugin.modernffa.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.plugin.modernffa.ModernFFA;
import org.plugin.modernffa.models.Arena;

import java.util.List;

public class SetArenaCommand extends SubCommand {

    public SetArenaCommand(ModernFFA plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (isPlayer(sender)) return true;

        Player player = (Player) sender;
        if (hasPermission(player, "ffa.setarena")) return true;

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /ffa setarena <name>");
            return true;
        }

        String arenaName = args[1];
        Location location = player.getLocation();

        SetPositionResetMapCommand.ArenaSelection selection = null;
        Arena arena = new Arena(arenaName, selection.getMin(), location);
        plugin.getArenas().put(arenaName, arena);
        plugin.getConfig().set("arenas." + arenaName + ".location", serializeLocation(location));
        plugin.saveConfig();

        player.sendMessage(ChatColor.GREEN + "Arena " + arenaName + " set successfully at your current location.");

        return true;
    }

    @Override
    public List<String> getAliases() {
        return List.of("seta");
    }

    @Override
    protected List<String> onSubCommandTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 2) {
            return List.of("<arena_name>");
        }
        return super.onSubCommandTabComplete(sender, cmd, alias, args);
    }
}
