package com.captainbboy.factionmappoints;

import com.captainbboy.factionmappoints.SQLite.SQLite;
import com.captainbboy.factionmappoints.commands.MainCommand;
import com.captainbboy.factionmappoints.commands.MainCommandTab;
import com.captainbboy.factionmappoints.events.PlayerJoinLeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FactionMapPoints extends JavaPlugin {

    private SQLite sqLite;
    private MapPointsExpansion expansion;
    public final String currVersion = "1.0";

    @Override
    public void onEnable() {
        // Default Config
        this.saveDefaultConfig();

        // Commands:
        getCommand("mappoints").setExecutor(new MainCommand(this));
        getCommand("mappoints").setTabCompleter(new MainCommandTab(this));

        // Events:
        getServer().getPluginManager().registerEvents(new PlayerJoinLeaveEvent(this), this);

        // Database
        sqLite = new SQLite(this);
        sqLite.load();
        sqLite.initialize();

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            expansion = new MapPointsExpansion(this);
            expansion.register();
        }

        getServer().getConsoleSender().sendMessage(MainUtil.cvtStr(
                "&7[&e&lMapPoints&7] &fPlugin has been successfully loaded."
        ));
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(MainUtil.cvtStr(
                "&7[&e&lMapPoints&7] &fPlugin has been successfully unloaded."
        ));
    }
    public SQLite getSQLite() {
        return this.sqLite;
    }

    public MapPointsExpansion getMapPointsExpansion() {
        return this.expansion;
    }

}
