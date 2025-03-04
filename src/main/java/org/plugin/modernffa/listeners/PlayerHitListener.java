package org.plugin.modernffa.listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.plugin.modernffa.ModernFFA;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerHitListener implements Listener {

    private final Map<UUID, Long> hitCooldowns = new HashMap<>();
    private static final long HIT_COOLDOWN = 10000; // 10 seconds in milliseconds

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim) || !(event.getDamager() instanceof Player attacker)) {
            return;
        }

        // Check cooldown
        long currentTime = System.currentTimeMillis();
        Long lastHitTime = hitCooldowns.get(victim.getUniqueId());

        if (lastHitTime == null || (currentTime - lastHitTime) >= HIT_COOLDOWN) {
            hitCooldowns.put(victim.getUniqueId(), currentTime);
            sendFightRequest(attacker, victim);
        } else {
            event.setCancelled(true);
            long secondsLeft = (HIT_COOLDOWN - (currentTime - lastHitTime)) / 1000;
            attacker.sendMessage(ChatColor.RED + "You can send another fight request in " + secondsLeft + " seconds.");
        }
    }

    private void sendFightRequest(Player attacker, Player victim) {
        String requestMessage = ChatColor.GOLD + attacker.getName() + ChatColor.GREEN + " wants to fight against you!";
        victim.sendMessage(requestMessage);
        attacker.sendMessage(ChatColor.GREEN + "Fight request sent to " + ChatColor.GOLD + victim.getName() + ChatColor.GREEN + ".");
    }

    private void handleFightAcceptance() {
        // Logic to handle fight acceptance (e.g., start the fight, update statuses, etc.)
    }

    private void handleFightRejection() {
        // Logic to handle fight rejection (e.g., inform the players, update statuses, etc.)
    }

    @EventHandler
    public void onPlayerAttackBack(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player attacker) || !(event.getDamager() instanceof Player victim)) {
            return;
        }

        if (hitCooldowns.containsKey(attacker.getUniqueId()) && hitCooldowns.containsKey(victim.getUniqueId())) {
            handleFightAcceptance();
            hitCooldowns.remove(attacker.getUniqueId());
            hitCooldowns.remove(victim.getUniqueId());
        }
    }
}
