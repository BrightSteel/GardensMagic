package com.gardensmc.gardensmagic.ability;

import com.gardensmc.gardensmagic.GardensMagic;
import com.gardensmc.gardensmagic.ability.helper.InvisibleArmorStandSpawner;
import com.gardensmc.gardensmagic.ability.tracker.AbilityTracker;
import com.gardensmc.gardensmagic.ability.tracker.TrackedAbility;
import de.tr7zw.nbtapi.NBTEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class IceBlockAbility extends Ability {

    protected IceBlockAbility() {
        super("ice_block");
    }

    @Override
    public void cast(Player caster) {
        if (AbilityTracker.hasTrackedAbility(caster.getUniqueId())) {
            return; // prevent casting while another ability is being tracked
        }
        // spawn ice blocks
        List<BlockDisplay> blockDisplays = Set.of(
                caster.getLocation(),
                caster.getLocation().add(0, 1, 0)
        )
                .stream()
                .map(this::spawnIceBlock)
                .toList();

        // add freeze effect to caster
        caster.setFreezeTicks(1000);

        // heal and freeze logic
        var runnable = new BukkitRunnable() {

            int maxTickLife = 100;

            @Override
            public void run() {
                maxTickLife -= 10;
                if (maxTickLife < 1 || !caster.isOnline()) {
                    finish();
                    return;
                }

                // heal caster
                var health = caster.getHealth();
                health += 0.5;
                // player always has max hp attribute ?
                var maxHealth = Objects
                        .requireNonNull(caster.getAttribute(Attribute.GENERIC_MAX_HEALTH))
                        .getValue();
                health = Math.min(health, maxHealth);
                // heal player
                caster.setHealth(health);
            }

            private void finish() {
                this.cancel();
                blockDisplays.forEach(Entity::remove);
                caster.setFreezeTicks(0);
                AbilityTracker.removeTrackedAbility(caster.getUniqueId());
            }
        }.runTaskTimer(GardensMagic.getPlugin(), 10, 10);
        // track ability
        AbilityTracker.putTrackedAbility(caster.getUniqueId(), new TrackedAbility(
                getName(),
                runnable.getTaskId(),
                true,
                true
        ));
    }

    private BlockDisplay spawnIceBlock(Location location) {
        var world = Objects.requireNonNull(location.getWorld());
        BlockDisplay blockDisplay = (BlockDisplay) world.spawnEntity(location, EntityType.BLOCK_DISPLAY);
        blockDisplay.setBlock(Material.ICE.createBlockData());
        NBTEntity nbtEntity = new NBTEntity(blockDisplay);
        nbtEntity.getPersistentDataContainer().setBoolean("MagicAbility", true);
//        blockDisplay.setVelocity(new Vector(0, 0, 0)); // might start falling before tag is set
//        fallingBlock.setInvulnerable(true); // probably not needed but to be safe
        return blockDisplay;
    }
}
