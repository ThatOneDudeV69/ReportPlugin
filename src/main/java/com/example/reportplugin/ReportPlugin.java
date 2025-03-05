package com.example.reportplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ReportPlugin extends JavaPlugin {
    private static ReportPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getCommand("report").setExecutor(new ReportCommand());

        Bukkit.getLogger().info("[ReportPlugin] Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[ReportPlugin] Plugin has been disabled!");
    }

    public static ReportPlugin getInstance() {
        return instance;
    }
}
