package com.gardensmc.gardensmagic.ability;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public abstract class Ability {

    private final String name;

    protected Ability(String name) {
        this.name = name;
        Abilities.abilityMap.put(name, this);
    }

    public abstract void cast(Player caster);

}
