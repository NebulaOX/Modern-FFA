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

import java.util.HashMap;
import java.util.Map;

public class PlayerKillListener implements Listener {

    private final ModernFFA plugin;
    private final Map<Player, Integer> killStreaks = new HashMap<>();

    public PlayerKillListener(ModernFFA plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        // Ensure the killer is a player
        if (killer != null) {
            // Update and announce kill streak
            int streak = killStreaks.getOrDefault(killer, 0) + 1;
            killStreaks.put(killer, streak);
            announceKillStreak(killer, streak);

            // Refill health and hunger for the killer
            refillHealthAndHunger(killer);

            // Drop the items of the victim and clear the drops to prevent duplication
            for (ItemStack drop : event.getDrops()) {
                victim.getWorld().dropItemNaturally(victim.getLocation(), drop);
            }
            event.getDrops().clear();
        }

        // Reset kill streak for the victim
        killStreaks.remove(victim);

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

    private void announceKillStreak(Player player, int streak) {
        String streakMessage = ChatColor.YELLOW + player.getName() + ChatColor.GREEN + " is on a " +
                ChatColor.RED + streak + ChatColor.GREEN + " kill streak!";
        Bukkit.broadcastMessage(streakMessage);
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
