package org.plugin.modernffa.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.plugin.modernffa.ModernFFA;
import org.plugin.modernffa.models.Arena;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SetPositionResetMapCommand extends SubCommand implements Listener {

    private final Map<String, ArenaSelection> selections = new HashMap<>();

    public SetPositionResetMapCommand(ModernFFA plugin) {
        super(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (isPlayer(sender)) return true;

        Player player = (Player) sender;
        if (hasPermission(player, "ffa.setpositionresetmap")) return true;

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /ffa setpositionresetmap <name>");
            return true;
        }

        String arenaName = args[1];
        ArenaSelection selection = selections.get(player.getUniqueId().toString());

        if (selection == null || !selection.isComplete()) {
            player.sendMessage(ChatColor.RED + "You need to select two corners first.");
            return true;
        }

        Arena arena = new Arena(arenaName, selection.getMin(), selection.getMax());
        plugin.getArenas().put(arenaName, arena);
        plugin.getConfig().set("arenas." + arenaName + ".min", serializeLocation(selection.getMin()));
        plugin.getConfig().set("arenas." + arenaName + ".max", serializeLocation(selection.getMax()));
        plugin.saveConfig();

        player.sendMessage(ChatColor.GREEN + "Position reset map for arena " + arenaName + " set successfully.");

        return true;
    }

    @Override
    public List<String> getAliases() {
        return List.of("setprmap");
    }

    @Override
    protected List<String> onSubCommandTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 2) {
            return List.of("<arena_name>");
        }
        return super.onSubCommandTabComplete(sender, cmd, alias, args);
    }

    // Listeners for selecting positions with a wooden axe

    @org.bukkit.event.EventHandler
    public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() == Material.WOODEN_AXE) {
            event.setCancelled(true);

            Location location = Objects.requireNonNull(event.getClickedBlock()).getLocation();
            ArenaSelection selection = selections.computeIfAbsent(player.getUniqueId().toString(), k -> new ArenaSelection());

            if (event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK) {
                selection.setMin(location);
                player.sendMessage(ChatColor.GREEN + "First corner set at " + locationToString(location));
            } else if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
                selection.setMax(location);
                player.sendMessage(ChatColor.GREEN + "Second corner set at " + locationToString(location));
            }

            if (selection.isComplete()) {
                player.sendMessage(ChatColor.GOLD + "Both corners have been set. Use /ffa setpositionresetmap <name> to save the arena.");
            }
        }
    }

    public static class ArenaSelection {
        private Location min;
        private Location max;

        public void setMin(Location min) {
            this.min = min;
        }

        public void setMax(Location max) {
            this.max = max;
        }

        public Location getMin() {
            return min;
        }

        public Location getMax() {
            return max;
        }

        public boolean isComplete() {
            return min != null && max != null;
        }
    }
}
