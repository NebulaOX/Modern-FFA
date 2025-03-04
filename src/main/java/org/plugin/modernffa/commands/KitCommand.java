package org.plugin.modernffa.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.plugin.modernffa.ModernFFA;
import org.plugin.modernffa.models.Kit;

import java.util.ArrayList;
import java.util.List;

public class KitCommand extends SubCommand {

    public KitCommand(ModernFFA plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /ffa kit <name>");
            return true;
        }

        String kitName = args[1];

        Kit kit = plugin.getKits().get(kitName);
        if (kit == null) {
            player.sendMessage(ChatColor.RED + "Kit " + kitName + " does not exist.");
            return true;
        }

        assert kit.items() != null;
        if (isInventoryEmpty(kit.items())) {
            player.sendMessage(ChatColor.RED + "The kit " + kitName + " is empty and cannot be equipped.");
            return true;
        }

        equipKit(player, kit);
        player.sendMessage(ChatColor.GREEN + "Kit " + kitName + " equipped successfully.");

        return true;
    }

    private boolean isInventoryEmpty(Object items) {
        return false;
    }

    @Override
    public List<String> getAliases() {
        return List.of();
    }

    private void equipKit(Player player, Kit kit) {
        clearInventory(player);
        ItemStack[] items = kit.items();
        assert items != null;
        player.getInventory().setContents(items);

        // Equip armor if included in the kit
        player.getInventory().setArmorContents(kit.getArmor());

        // Equip extra contents if included in the kit
        player.getInventory().setExtraContents(kit.getExtraContents());
    }

    private void clearInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setExtraContents(null);
    }

    private boolean isInventoryEmpty(ItemStack[] items) {
        for (ItemStack item : items) {
            if (item != null && item.getType() != Material.AIR) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 2) {
            return new ArrayList<>(plugin.getKits().keySet());
        }
        return super.onTabComplete(sender, cmd, alias, args);
    }
}
