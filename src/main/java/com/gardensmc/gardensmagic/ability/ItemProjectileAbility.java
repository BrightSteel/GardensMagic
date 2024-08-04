package com.gardensmc.gardensmagic.ability;

import com.gardensmc.gardensmagic.ability.helper.ItemProjectileLauncher;
import com.gardensmc.gardensmagic.ability.helper.ItemProjectileProperties;
import org.bukkit.Material;
import org.bukkit.entity.*;

public class ItemProjectileAbility extends Ability {

    private final ItemProjectileProperties projectileProperties;

    public ItemProjectileAbility(
            String name,
            ItemProjectileLauncher.ProjectileType projectileType,
            Material material,
            double speed,
            double damage
    ) {
        super(name);
        this.projectileProperties = ItemProjectileProperties
                .builder()
                .projectileType(projectileType)
                .material(material)
                .speed(speed)
                .damage(damage)
                .build();
    }

    @Override
    public void cast(Player caster) {
        new ItemProjectileLauncher(caster, projectileProperties).launch();
    }
}
