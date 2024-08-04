package com.gardensmc.gardensmagic;

import com.gardensmc.gardensmagic.ability.tracker.TrackedAbility;
import com.gardensmc.gardensmagic.command.CastCommand;
import com.gardensmc.gardensmagic.listener.EntityListener;
import com.gardensmc.gardensmagic.listener.Listeners;
import com.gardensmc.gardensmagic.placeholder.MagicPlaceholder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GardensMagic extends JavaPlugin {

    @Getter
    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        // commands
        Objects.requireNonNull(getCommand("cast")).setExecutor(new CastCommand());

        Listeners.registerListeners();

        if (Bukkit.getPluginManager().getPlugin("PlaceHolderAPI") != null) {
            new MagicPlaceholder().register();
        }
    }
}
