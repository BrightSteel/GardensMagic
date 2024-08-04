package com.gardensmc.gardensmagic.util;

import org.bukkit.Location;

public class LocationUtil {

    public static Location stripYawAndPitch(Location location) {
        var stripped = location.clone();
        stripped.setYaw(0);
        stripped.setPitch(0);
        return stripped;
    }
}
