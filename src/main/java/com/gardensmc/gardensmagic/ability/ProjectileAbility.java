package com.gardensmc.gardensmagic.ability;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

public class ProjectileAbility extends Ability {

    private final Class<? extends Projectile> projectile;

    public ProjectileAbility(String name, Class<? extends Projectile> projectile) {
        super(name);
        this.projectile = projectile;
    }

    @Override
    public void cast(Player caster) {
        caster.launchProjectile(projectile);
    }
}
