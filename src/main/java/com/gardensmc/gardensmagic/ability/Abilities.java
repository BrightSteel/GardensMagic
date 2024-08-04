package com.gardensmc.gardensmagic.ability;

import com.gardensmc.gardensmagic.ability.helper.ItemProjectileLauncher;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.WitherSkull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Abilities {

    public static final Map<String, Ability> abilityMap = new HashMap<>();

    /** Teleportation */
    public static final Ability ENDER_SHIFT = new EnderShiftAbility();
    public static final Ability TELEPORT = new TeleportAbility();

    /** Projectiles */
    public static final Ability ICE_PROJECTILE = new ItemProjectileAbility(
            "ice_projectile",
            ItemProjectileLauncher.ProjectileType.ITEM,
            Material.ICE,
            0.7,
            5
    );
    public static final Ability BLOCK_LAUNCH = new BlockLaunchAbility();
    public static final Ability FIREBALL = new ProjectileAbility(
            "fireball",
            Fireball.class
    );
    public static final Ability WITHER_SKULL = new ProjectileAbility(
            "wither_skull",
            WitherSkull.class
    );
    public static final Ability GLACIAL_FREEZE = new GlacialFreezeAbility();

    /** Entity Summoning */
    public static final Ability LIGHTNING = new LightningAbility();

    /** Self */
    public static final Ability ICE_BLOCK = new IceBlockAbility();
    public static final Ability SKY_LASH = new SkyLashAbility();
    public static final Ability DRAGON_FLIGHT = new DragonFlightAbility();
}
