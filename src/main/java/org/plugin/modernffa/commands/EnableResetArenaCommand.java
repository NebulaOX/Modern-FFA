package org.plugin.modernffa.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.plugin.modernffa.ModernFFA;
import org.plugin.modernffa.models.Arena;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnableResetArenaCommand extends SubCommand implements Listener {

    private final Map<String, ResetTask> resetTasks = new HashMap<>();

    public EnableResetArenaCommand(ModernFFA plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("Usage: /ffa enableresetarena <name> <interval>");
            return true;
        }

        if (!hasPermission(player, "ffa.enableresetarena")) return true;

        String arenaName = args[1];
        long resetInterval;

        try {
            resetInterval = parseInterval(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid interval format. Please use a number followed by s, m, or h (e.g., 10s, 5m, 1h).");
            return true;
        }

        Arena arena = plugin.getArenas().get(arenaName);
        if (arena == null) {
            player.sendMessage("Arena not found: " + arenaName);
            return true;
        }

        scheduleArenaReset(arena, resetInterval);
        player.sendMessage("Reset for arena " + arenaName + " scheduled every " + args[2] + ".");
        return true;
    }

    @Override
    public List<String> getAliases() {
        return List.of("resetarena");
    }

    private void scheduleArenaReset(Arena arena, long resetInterval) {
        ResetTask resetTask = new ResetTask(arena);
        resetTask.runTaskTimer(plugin, resetInterval, resetInterval);
        resetTasks.put(arena.getName(), resetTask);
    }

    private long parseInterval(String interval) throws NumberFormatException {
        char unit = interval.charAt(interval.length() - 1);
        long value = Long.parseLong(interval.substring(0, interval.length() - 1));

        return switch (unit) {
            case 's' -> value * 20;
            case 'm' -> value * 20 * 60;
            case 'h' -> value * 20 * 60 * 60;
            default -> throw new NumberFormatException("Invalid time unit");
        };
    }

    private static class ResetTask extends BukkitRunnable implements org.plugin.modernffa.commands.ResetTask {

        private final Location originalLocation;

        public ResetTask(Arena arena) {
            this.originalLocation = copyLocation(arena.getSpawnLocation());
        }

        @Override
        public void run() {
            World world = originalLocation.getWorld();
            if (world == null) {
                return;
            }

            restoreBlockStates();
            restoreEntityStates();
        }

        private Location copyLocation(Location location) {
            return new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        private List<BlockState> captureBlockStates() {
            // Logic to capture and store the current block states in the arena
            return null;
        }

        private List<Entity> captureEntityStates() {
            // Logic to capture and store the current entity states in the arena
            return null;
        }

        private void restoreBlockStates() {
            // Logic to restore the block states to their original state
        }

        private void restoreEntityStates() {
            // Logic to restore the entities to their original state
        }

        @Override
        public void runTaskTimer(ModernFFA plugin, long resetInterval, long resetInterval1) {
        }
    }
}
