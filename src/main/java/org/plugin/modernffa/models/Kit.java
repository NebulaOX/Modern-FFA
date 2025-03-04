package org.plugin.modernffa.models;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import java.util.*;

@SerializableAs("Kit")
public class Kit implements ConfigurationSerializable {

    private String name;
    private final ItemStack[] armorContents;
    private ItemStack[] inventoryContents;
    private List<PotionEffect> potionEffects;
    private ItemStack[] armor;
    private ItemStack[] extraContents;

    public Kit(ItemStack[] armorContents) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.armorContents = Objects.requireNonNull(armorContents, "Armor contents cannot be null");
        this.inventoryContents = Objects.requireNonNull(inventoryContents, "Inventory contents cannot be null");
        this.potionEffects = Objects.requireNonNull(potionEffects, "Potion effects cannot be null");
    }

    public Kit(Map<String, Object> serialized) {
        this.name = (String) serialized.get("name");
        this.armorContents = deserializeItemStackArray((List<Map<String, Object>>) serialized.get("armorContents"));
        this.inventoryContents = deserializeItemStackArray((List<Map<String, Object>>) serialized.get("inventoryContents"));
        this.potionEffects = (List<PotionEffect>) serialized.get("potionEffects");
    }

    public String getName() {
        return name;
    }

    public ItemStack[] getArmorContents() {
        return armorContents;
    }

    public ItemStack[] getInventoryContents() {
        return inventoryContents;
    }

    public List<PotionEffect> getPotionEffects() {
        return potionEffects;
    }

    public void applyToPlayer(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.setArmorContents(armorContents);
        inventory.setContents(inventoryContents);
        for (PotionEffect effect : potionEffects) {
            player.addPotionEffect(effect);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("name", name);
        serialized.put("armorContents", serializeItemStackArray(armorContents));
        serialized.put("inventoryContents", serializeItemStackArray(inventoryContents));
        serialized.put("potionEffects", potionEffects);
        return serialized;
    }

    private List<Map<String, Object>> serializeItemStackArray(ItemStack[] items) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ItemStack item : items) {
            list.add(item.serialize());
        }
        return list;
    }

    private static ItemStack[] deserializeItemStackArray(List<Map<String, Object>> list) {
        ItemStack[] items = new ItemStack[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = ItemStack.deserialize(list.get(i));
        }
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kit kit = (Kit) o;
        return name.equals(kit.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public ItemStack[] items() {
        return null;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public ItemStack[] getExtraContents() {
        return extraContents;
    }
}
