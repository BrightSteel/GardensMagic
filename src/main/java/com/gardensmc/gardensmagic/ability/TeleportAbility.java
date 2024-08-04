package com.gardensmc.gardensmagic.ability;

import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.Player;

public class TeleportAbility extends Ability {

    public TeleportAbility() {
        super("teleport");
    }

    @Override
    public void cast(Player caster) {
        var direction = caster.getEyeLocation().getDirection().normalize();
        var rayTrace = caster.getWorld().rayTraceBlocks(
                caster.getEyeLocation(),
                direction,
                50, // block range
                FluidCollisionMode.NEVER, // ignore water
                false // don't ignore passable blocks?
        );
        if (rayTrace != null && rayTrace.getHitBlock() != null) {
            var loc = caster.getLocation();
            var dest = rayTrace.getHitBlock().getLocation(); // wait try using the hitposition or just move
            // keep caster's yaw and pitch
            dest.setYaw(loc.getYaw());
            dest.setPitch(loc.getPitch());
            caster.teleport(dest);
        }
    }
}
