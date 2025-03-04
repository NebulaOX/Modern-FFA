package org.plugin.modernffa;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.modernffa.commands.*;
import org.plugin.modernffa.listeners.*;
import org.plugin.modernffa.models.Arena;
import org.plugin.modernffa.models.Kit;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class ModernFFA extends JavaPlugin {

    private final Map<String, Arena> arenas = new HashMap<>();
    private final Map<String, Kit> kits = new HashMap<>();
    private Arena vipArena;
    private Logger logger;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        logger = this.getLogger();
        logger.info("ModernFFA is being enabled!");

        // Load configuration
        loadConfiguration();

        // Register commands
        registerCommands();

        // Register listeners
        registerListeners();

        // Load arenas and kits
        loadArenas();
        loadKits();

        logger.info("ModernFFA has been enabled!");
    }

    @Override
    public void onDisable() {
        logger.info("ModernFFA is being disabled!");

        // Save arenas and kits
        saveArenas();
        saveKits();

        logger.info("ModernFFA has been disabled!");
    }

    private void loadConfiguration() {
        saveDefaultConfig();
        config = getConfig();
    }

    private void registerCommands() {
        CommandManager commandManager = new CommandManager(this);
        Objects.requireNonNull(this.getCommand("ffa")).setExecutor(commandManager);
        Objects.requireNonNull(this.getCommand("ffa")).setTabCompleter(commandManager);
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerKillListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerHitListener(), this);
    }

    private void loadArenas() {
        SetPositionResetMapCommand.ArenaSelection selection = null;
        if (config.contains("arenas")) {
            for (String arenaName : Objects.requireNonNull(config.getConfigurationSection("arenas")).getKeys(false)) {
                assert false;
                arenas.put(arenaName, new Arena(arenaName, selection.getMin(), config.getLocation("arenas." + arenaName + ".location")));
            }
        }
        if (config.contains("vipArena")) {
            vipArena = new Arena("vip", selection.getMin(), config.getLocation("vipArena.location"));
        }
    }

    private void saveArenas() {
        for (Map.Entry<String, Arena> entry : arenas.entrySet()) {
            config.set("arenas." + entry.getKey() + ".location", entry.getValue().getLocation());
        }
        if (vipArena != null) {
            config.set("vipArena.location", vipArena.getLocation());
        }
        saveConfig();
    }

    private void loadKits() {
        if (config.contains("kits")) {
            for (String kitName : Objects.requireNonNull(config.getConfigurationSection("kits")).getKeys(false)) {
                ItemStack[] items = new ItemStack[0];
                ItemStack[] armor = new ItemStack[0];
                kits.put(kitName, new Kit(new ItemStack[]{config.getItemStack("kits." + kitName + ".items")}));
            }
        }
    }

    private void saveKits() {
        for (Map.Entry<String, Kit> entry : kits.entrySet()) {
            config.set("kits." + entry.getKey() + ".items", entry.getValue().items());
        }
        saveConfig();
    }

    public Map<String, Arena> getArenas() {
        return arenas;
    }

    public Map<String, Kit> getKits() {
        return kits;
    }

    public Arena getVipArena() {
        return vipArena;
    }

    public void setVipArena(Arena vipArena) {
        this.vipArena = vipArena;
    }

    public Logger getPluginLogger() {
        return logger;
    }
}
