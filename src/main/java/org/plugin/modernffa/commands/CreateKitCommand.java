package org.plugin.modernffa.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.plugin.modernffa.ModernFFA;
import org.plugin.modernffa.models.Kit;

import java.util.List;

public class CreateKitCommand extends SubCommand {

    public CreateKitCommand(ModernFFA plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /ffa createkit <name>");
            return true;
        }

        if (!player.hasPermission("ffa.createkit")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        String kitName = args[1];
        if (plugin.getKits().containsKey(kitName)) {
            player.sendMessage(ChatColor.RED + "A kit with this name already exists.");
            return true;
        }

        ItemStack[] items = player.getInventory().getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();
        ItemStack[] extraContents = player.getInventory().getExtraContents();

        if (isInventoryEmpty(items) && isInventoryEmpty(armor) && isInventoryEmpty(extraContents)) {
            player.sendMessage(ChatColor.RED + "Your inventory is empty. Cannot create an empty kit.");
            return true;
        }

        Kit kit = new Kit(extraContents);
        plugin.getKits().put(kitName, kit);
        plugin.getConfig().set("kits." + kitName + ".items", items);
        plugin.getConfig().set("kits." + kitName + ".armor", armor);
        plugin.getConfig().set("kits." + kitName + ".extraContents", extraContents);
        plugin.saveConfig();
        player.sendMessage(ChatColor.GREEN + "Kit " + kitName + " created successfully.");

        return true;
    }

    @Override
    public List<String> getAliases() {
        return List.of();
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
            return List.of("<kit_name>");
        }
        return super.onTabComplete(sender, cmd, alias, args);
    }
}
