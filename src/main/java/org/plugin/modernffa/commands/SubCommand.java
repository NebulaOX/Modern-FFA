package org.plugin.modernffa.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.plugin.modernffa.ModernFFA;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public abstract class SubCommand implements TabCompleter {

    protected final ModernFFA plugin;
    protected final Logger logger;

    public SubCommand(ModernFFA plugin) {
        this.plugin = plugin;
        this.logger = this.plugin.getLogger();
    }

    public String getName() {
        return "";
    }

    public abstract boolean execute(CommandSender sender, String[] args);

    public abstract List<String> getAliases();

    protected boolean isPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }
        return false;
    }

    protected boolean hasPermission(Player player, String permission) {
        if (!player.hasPermission(permission)) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }
        return false;
    }

    protected String serializeLocation(Location location) {
        return Objects.requireNonNull(location.getWorld()).getName() + "," +
                location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getYaw() + "," +
                location.getPitch();
    }

    protected Location deserializeLocation(String locationString) {
        String[] parts = locationString.split(",");
        return new Location(
                plugin.getServer().getWorld(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]),
                Float.parseFloat(parts[4]),
                Float.parseFloat(parts[5])
        );
    }

    protected String locationToString(Location location) {
        return "[" + Objects.requireNonNull(location.getWorld()).getName() + "] " +
                "X: " + location.getX() +
                " Y: " + location.getY() +
                " Z: " + location.getZ() +
                " Yaw: " + location.getYaw() +
                " Pitch: " + location.getPitch();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            return getCommandNames(args[0]);
        } else if (args.length >= 2) {
            return onSubCommandTabComplete(sender, cmd, alias, args);
        }
        return new ArrayList<>();
    }

    protected List<String> getCommandNames(String prefix) {
        List<String> commandNames = new ArrayList<>(plugin.getDescription().getCommands().keySet());
        return StringUtil.copyPartialMatches(prefix, commandNames, new ArrayList<>());
    }

    protected List<String> onSubCommandTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        return new ArrayList<>();
    }
}
