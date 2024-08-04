package com.gardensmc.gardensmagic.ability.helper;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.Objects;
import java.util.function.Consumer;

public class InvisibleArmorStandSpawner {

    public static ArmorStand spawn(Location location, Consumer<ArmorStand> armorStandConsumer) {
        World world = Objects.requireNonNull(location.getWorld());
        // spawn armor stand out of viewer sight
        Location offsetLocation = location.clone();
        offsetLocation.setY(world.getMaxHeight());
        ArmorStand armorStand = (ArmorStand) world.spawnEntity(offsetLocation, EntityType.ARMOR_STAND);

        // apply armor stand modifications
        armorStand.setInvisible(true);
        armorStandConsumer.accept(armorStand);
        // teleport back to desired location
        armorStand.teleport(location);
        return armorStand;
    }
}
