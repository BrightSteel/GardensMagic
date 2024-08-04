package com.gardensmc.gardensmagic.ability;

import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.Player;

public class LightningAbility extends Ability {

    public LightningAbility() {
        super("lightning");
    }

    @Override
    public void cast(Player caster) {
        var direction = caster.getEyeLocation().getDirection().normalize();
        var rayTrace = caster.getWorld().rayTrace(
                caster.getEyeLocation(),
                direction,
                50, // block range
                FluidCollisionMode.ALWAYS,
                true, // ignore passable blocks
                1.0, // beam size
                entity -> !entity.isDead()
        );
        // do i need to check for entity as well or does it use the entity's block ?
        if (rayTrace != null && rayTrace.getHitBlock() != null) {
            caster.getWorld().strikeLightning(rayTrace.getHitBlock().getLocation());
        }
    }
}
