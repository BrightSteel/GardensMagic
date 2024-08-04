package com.gardensmc.gardensmagic.listener;

import org.bukkit.event.Listener;

public abstract class BukkitListener implements Listener {

    protected BukkitListener() {
        Listeners.listeners.add(this);
    }
}
