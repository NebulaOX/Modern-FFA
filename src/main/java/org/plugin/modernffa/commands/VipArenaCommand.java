package org.plugin.modernffa.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.plugin.modernffa.ModernFFA;
import org.plugin.modernffa.models.Arena;

import java.util.Collections;
import java.util.List;

public class VipArenaCommand extends SubCommand {

    public VipArenaCommand(ModernFFA plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (isPlayer(sender)) return true;

        Player player = (Player) sender;
        if (!hasPermission()) return true;

        Arena vipArena = plugin.getVipArena();
        if (vipArena == null) {
            player.sendMessage(ChatColor.RED + "No VIP arena set.");
            return true;
        }

        Location location = (Location) vipArena.getLocation();
        if (location == null || location.getWorld() == null) {
            player.sendMessage(ChatColor.RED + "The location for the VIP arena is not set or the world is invalid.");
            return true;
        }

        player.teleport(location);
        player.sendMessage(ChatColor.GREEN + "Teleported to VIP Arena " + ChatColor.YELLOW + vipArena.getName() + ChatColor.GREEN + ".");
        displayVipArenaHeading(player, vipArena.getName());

        return true;
    }

    private boolean hasPermission() {
        return false;
    }

    private void displayVipArenaHeading(Player player, String arenaName) {
        String heading = ChatColor.GOLD + "" + ChatColor.BOLD + arenaName;
        player.sendTitle(heading, "", 10, 70, 20);
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("vipa");
    }

    @Override
    protected List<String> onSubCommandTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        return Collections.emptyList();
    }
}
