package com.gardensmc.gardensmagic.ability.helper;

import com.gardensmc.gardensmagic.GardensMagic;
import de.tr7zw.nbtapi.NBTEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Objects;

@AllArgsConstructor
public class ItemProjectileLauncher {

    private final Player caster;
    private final ItemProjectileProperties properties;

    public void launch() {
        var launchLocation = caster.getEyeLocation().subtract(0, 0.5, 0);
        Display projectile = (Display) spawnProjectile(launchLocation);
        var nbt = new NBTEntity(projectile).getPersistentDataContainer();
        nbt.setBoolean("MagicAbility", true);
        nbt.setBoolean("MagicProjectile", true);

        // store constant velocity
        final var velocity = launchLocation.getDirection().normalize().multiply(properties.getSpeed());
        projectile.setVelocity(velocity);

        // projectile logic
        new BukkitRunnable() {

            int tickLife = properties.getMaxTickLife();

            @Override
            public void run() {
                tickLife--;
                if (tickLife < 1) {
                    detonate(projectile);
                    this.cancel();
                    return;
                }

                // keep constant velocity in initial direction
                if (properties.isConstantVelocity()) {
                    var l = projectile.getLocation().add(projectile.getLocation().getDirection());
                    projectile.teleport(l);
//                    projectile.setVelocity(velocity);
                }

                // spawn particle
//                itemEntity.getWorld().spawnParticle(Particle.SNOWFLAKE, itemEntity.getLocation(), 1, 0, 0, 0);

                // damage logic
                if (properties.isDetonateOnEntities()) {
                    var nearbyEntities = projectile.getNearbyEntities(0.5, 0.5, 0.5);
                    if (!nearbyEntities.isEmpty()) {
                        for (Entity entity : nearbyEntities) {
                            var self = entity.getEntityId() == caster.getEntityId();
                            if (!self && entity instanceof LivingEntity livingEntity) {
                                detonate(projectile, livingEntity);
                                this.cancel();
                                return;
                            }
                        }
                    }
                }

                // remove if hit block
                // todo tjhis is gltichy; lets just check if we are in a block or slightly above a block
                var rayTrace = projectile.getWorld().rayTraceBlocks(
                        projectile.getLocation(),
                        velocity,
                        0.1,
                        FluidCollisionMode.ALWAYS,
                        true // ignore passable blocks
                );
                if (rayTrace != null && rayTrace.getHitBlock() != null) {
                    detonate(projectile);
                    this.cancel();
                }
            }
        }.runTaskTimer(GardensMagic.getPlugin(), 0, 20);
    }

    protected void detonate(Entity projectile) {
        projectile.remove();
    }

    protected void detonate(Entity projectile, LivingEntity hitEntity) {
        hitEntity.damage(properties.getDamage());
        detonate(projectile);
    }

    private Entity spawnProjectile(Location location) {
        Display entity;
        var world = Objects.requireNonNull(location.getWorld());
        switch (properties.getProjectileType()) {
            case ITEM -> {
                ItemDisplay itemDisplay = (ItemDisplay) world.spawnEntity(location, EntityType.ITEM_DISPLAY);
                itemDisplay.setItemStack(new ItemStack(properties.getMaterial()));
                entity = itemDisplay;
            }
            case BLOCK -> {
                BlockDisplay blockDisplay = (BlockDisplay) world.spawnEntity(location, EntityType.BLOCK_DISPLAY);
                blockDisplay.setBlock(properties.getMaterial().createBlockData());
                entity = blockDisplay;
            }
            default -> throw new RuntimeException("Unsupported projectile type: " + properties.getProjectileType());
        }
        entity.setTeleportDuration(20);
        // universal modifications
//        entity.setGravity(false);
//        entity.setInvulnerable(true);
        return entity;
    }

    public enum ProjectileType {
        ITEM,
        BLOCK
    }
}
