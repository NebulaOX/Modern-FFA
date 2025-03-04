package org.plugin.modernffa.listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.plugin.modernffa.ModernFFA;

public class PlayerDeathListener implements Listener {

    private final ModernFFA plugin;

    public PlayerDeathListener(ModernFFA plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        // Ensure the killer is a player
        if (killer != null) {
            double killerHealth = killer.getHealth() / 2.0;
            killer.getMaxHealth();

            // Send messages to both victim and killer
            String killerMessage = ChatColor.GREEN + "You have killed " + ChatColor.RED + victim.getName() +
                    ChatColor.GREEN + " with " + ChatColor.RED + killerHealth + " hearts " +
                    ChatColor.GREEN + "left. You gained " + ChatColor.RED + 20 + " hearts.";
            killer.sendMessage(killerMessage);

            String victimMessage = ChatColor.RED + "You have been killed by " + ChatColor.GOLD + killer.getName() +
                    ChatColor.RED + ". They have " + ChatColor.GOLD + killerHealth + " hearts " + ChatColor.RED + "left.";
            victim.sendMessage(victimMessage);

            // Refill health and hunger for the killer
            refillHealthAndHunger(killer);

            // Drop the items of the victim and clear the drops to prevent duplication
            for (ItemStack drop : event.getDrops()) {
                victim.getWorld().dropItemNaturally(victim.getLocation(), drop);
            }
            event.getDrops().clear();
        }

        // Schedule respawn for the player
        scheduleRespawn(victim);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Location spawnLocation = deserializeLocation(plugin.getConfig().getString("spawn"));
        if (spawnLocation != null) {
            event.setRespawnLocation(spawnLocation);
        }
    }

    private void refillHealthAndHunger(Player player) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
        });
    }

    private Location deserializeLocation(String locationString) {
        if (locationString == null || locationString.isEmpty()) {
            return null;
        }
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

    private void scheduleRespawn(Player player) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.spigot().respawn(), 1L);
    }
}
