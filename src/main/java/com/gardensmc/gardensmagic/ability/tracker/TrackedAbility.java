package com.gardensmc.gardensmagic.ability.tracker;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class TrackedAbility {
    private String abilityName;
    private @Nullable Integer taskId;
    // special properties during on-going ability, should turn into builder
    private boolean preventMovement;
    private boolean preventDamage;
}
