package com.gardensmc.gardensmagic.ability.helper;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;

@Builder
@Getter
public class ItemProjectileProperties {
    @Builder.Default
    private final ItemProjectileLauncher.ProjectileType projectileType = ItemProjectileLauncher.ProjectileType.ITEM;
    @Builder.Default
    private final Material material = Material.GRASS_BLOCK;
    @Builder.Default
    private final double speed = 0.7;
    @Builder.Default
    private final double damage = 5;
    @Builder.Default
    private final boolean detonateOnEntities = true;
    @Builder.Default
    private final int maxTickLife = 100;
    @Builder.Default
    private final boolean constantVelocity = true;
    @Builder.Default
    private final boolean noGravity = true;
}
