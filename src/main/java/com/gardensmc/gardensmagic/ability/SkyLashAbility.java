package com.gardensmc.gardensmagic.ability;

import org.bukkit.entity.Player;

public class SkyLashAbility extends Ability {

    public SkyLashAbility() {
        super("sky_lash");
    }

    @Override
    public void cast(Player caster) {
        var direction = caster.getEyeLocation().getDirection().normalize().multiply(2.5);
        caster.setVelocity(
                caster.getVelocity().add(direction)
        );
    }
}
