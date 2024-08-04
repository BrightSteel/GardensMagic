package com.gardensmc.gardensmagic.ability.tracker;


import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityTracker {

    private static final Map<UUID, TrackedAbility> abilityTrackerMap = new HashMap<>();

    @Nullable
    public static TrackedAbility getTrackedAbility(UUID uuid) {
        return abilityTrackerMap.get(uuid);
    }

    public static void putTrackedAbility(UUID uuid, TrackedAbility trackedAbility) {
        abilityTrackerMap.put(uuid, trackedAbility);
    }

    public static boolean hasTrackedAbility(UUID uuid) {
        return abilityTrackerMap.containsKey(uuid);
    }

    public static void removeTrackedAbility(UUID uuid) {
        abilityTrackerMap.remove(uuid);
    }

}
