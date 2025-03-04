package org.plugin.modernffa.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.plugin.modernffa.ModernFFA;

import java.util.Collections;
import java.util.List;

public class SetSpawnCommand extends SubCommand {

    public SetSpawnCommand(ModernFFA plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (isPlayer(sender)) return true;

        Player player = (Player) sender;
        if (hasPermission(player, "ffa.setspawn")) return true;

        Location location = player.getLocation();
        plugin.getConfig().set("spawn", serializeLocation(location));
        plugin.saveConfig();

        player.sendMessage(ChatColor.GREEN + "Spawn location set successfully.");
        logger.info(player.getName() + " set the spawn location to " + locationToString(location));

        return true;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("setsp");
    }

    @Override
    protected List<String> onSubCommandTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            return Collections.singletonList("<spawn>");
        }
        return super.onSubCommandTabComplete(sender, cmd, alias, args);
    }
}
