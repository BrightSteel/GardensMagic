package com.gardensmc.gardensmagic.listener;

import com.gardensmc.gardensmagic.GardensMagic;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

public class Listeners {

    public static final Set<Listener> listeners = new HashSet<>();

    public static void registerListeners() {
        listeners.forEach(listener -> Bukkit
                .getServer()
                .getPluginManager()
                .registerEvents(listener, GardensMagic.getPlugin())
        );
    }

    static {
        // create auto-registering listeners here
        new EntityListener();
        new PlayerListener();
        new ChunkListener();
    }
}
