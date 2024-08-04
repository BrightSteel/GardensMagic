package com.gardensmc.gardensmagic.ability;

import com.gardensmc.gardensmagic.ability.tracker.AbilityTracker;
import com.gardensmc.gardensmagic.ability.tracker.TrackedAbility;
import org.bukkit.entity.Player;

public class DragonFlightAbility extends Ability {

    protected DragonFlightAbility() {
        super("dragon_flight");
    }

    @Override
    public void cast(Player caster) {
        var direction = caster.getVelocity().clone().normalize().multiply(10);
        caster.setVelocity(direction);
//        AbilityTracker.putTrackedAbility(
//                caster.getUniqueId(),
//                new TrackedAbility(getName(), null, false, false)
//        );
    }
}
