package com.gardensmc.gardensmagic.ability;

import com.gardensmc.gardensmagic.GardensMagic;
import com.gardensmc.gardensmagic.ability.helper.ItemProjectileLauncher;
import com.gardensmc.gardensmagic.ability.helper.ItemProjectileProperties;
import com.gardensmc.gardensmagic.ability.tracker.AbilityTracker;
import com.gardensmc.gardensmagic.ability.tracker.TrackedAbility;
import de.tr7zw.nbtapi.NBTEntity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class GlacialFreezeAbility extends Ability {

    private final ItemProjectileProperties properties;
    private static final int FREEZE_RADIUS = 5;
    private static final int TICKS_NEEDED_TO_FREEZE = 10;

    protected GlacialFreezeAbility() {
        super("glacial_freeze");
        this.properties = ItemProjectileProperties
                .builder()
                .projectileType(ItemProjectileLauncher.ProjectileType.BLOCK)
                .material(Material.ICE)
                .constantVelocity(false)
                .detonateOnEntities(false)
                .noGravity(false)
                .maxTickLife(40)
                .build();
    }

    @Override
    public void cast(Player caster) {
        new ItemProjectileLauncher(caster, properties) {
            @Override
            protected void detonate(Entity projectile) {
                // rise into air
                projectile.setVelocity(new Vector(0, 0, 0));
                projectile.setGravity(false);
                projectile.teleport(projectile.getLocation().add(0, 1, 0));

                Map<UUID, Integer> mobFreezeTick = new HashMap<>();
                List<Entity> temporaryEntities = new ArrayList<>();
                Set<LivingEntity> frozenEntities = new HashSet<>();
                Set<UUID> frozenPlayers = new HashSet<>();

                new BukkitRunnable() {
                    int tickLife = 100;

                    @Override
                    public void run() {
                        tickLife--;
                        if (tickLife < 1) {
                            finish();
                            return;
                        }

                        // tick frozen entities
                        mobFreezeTick.entrySet().forEach(entry -> entry.setValue(entry.getValue() + 1));

                        // particles
                        projectile.getWorld().spawnParticle(Particle.SNOWFLAKE, projectile.getLocation(), 1);

                        // radial freeze
                        List<LivingEntity> nearbyEntities = projectile
                                .getNearbyEntities(FREEZE_RADIUS, FREEZE_RADIUS, FREEZE_RADIUS)
                                .stream()
                                .filter(entity -> entity.getEntityId() != caster.getEntityId())
                                .filter(entity -> entity instanceof LivingEntity)
                                .map(entity -> (LivingEntity) entity)
                                .toList();
                        for (LivingEntity livingEntity : nearbyEntities) {
                            var key = livingEntity.getUniqueId();
                            int freezeTick = mobFreezeTick.computeIfAbsent(key, uuid -> 1);
                            if (freezeTick >= TICKS_NEEDED_TO_FREEZE) {
//                                if (livingEntity instanceof Mob mob) {
//                                    mob.setTarget(null); // remove target
//                                }
                                if (freezeTick == TICKS_NEEDED_TO_FREEZE) {
                                    freezeEntity(livingEntity, temporaryEntities, frozenPlayers, frozenEntities, getTaskId());
                                } else if (tickLife % 10 == 0) {
                                    livingEntity.damage(1);
                                }
                            }
                        }
                        // remove tracked but no longer nearby entities
                        // not sure how expensive this is, so lets do it every 10 ticks
                        if (tickLife % 10 == 0) {
                            mobFreezeTick.keySet().retainAll(
                                    nearbyEntities
                                            .stream()
                                            .map(Entity::getUniqueId)
                                            .toList()
                            );
                        }
                    }

                    private void finish() {
                        this.cancel();
                        projectile.remove();
                        temporaryEntities.forEach(Entity::remove);
                        frozenPlayers.forEach(uuid -> {
                            AbilityTracker.removeTrackedAbility(uuid);
                            Player player = Bukkit.getPlayer(uuid);
                            if (player != null) {
                                player.setFreezeTicks(0);
                            }
                        });
                        frozenEntities.forEach(this::unfreezeEntity);
                    }

                    private void unfreezeEntity(LivingEntity livingEntity) {
                        livingEntity.setGravity(true);
                        livingEntity.setAI(true);
                        new NBTEntity(livingEntity).getPersistentDataContainer().removeKey("MagicModified");
                    }
                }.runTaskTimer(GardensMagic.getPlugin(), 0, 1);
            }
        }.launch();
    }

    private void freezeEntity(LivingEntity livingEntity, List<Entity> temporaryEntities, Set<UUID> frozenPlayers, Set<LivingEntity> frozenEntities, int taskId) {
        var location = livingEntity.getLocation();
        var world = Objects.requireNonNull(location.getWorld());
        Set.of(
                location,
                location.clone().add(0, 1, 0)
        ).forEach(l -> {
            BlockDisplay blockDisplay = (BlockDisplay) world.spawnEntity(l, EntityType.BLOCK_DISPLAY);
            blockDisplay.setBlock(Material.ICE.createBlockData());
            var nbtEntity = new NBTEntity(blockDisplay).getPersistentDataContainer();
            nbtEntity.setBoolean("MagicAbility", true);
            temporaryEntities.add(blockDisplay);
        });

        // stop entity movement so doesn't fly out of ice block
        livingEntity.setVelocity(new Vector(0, 0, 0));

        // track players to prevent their movements
        if (livingEntity instanceof Player player) {
            livingEntity.setFreezeTicks(1000);
            // track ability
            AbilityTracker.putTrackedAbility(player.getUniqueId(), new TrackedAbility(
                    getName(),
                    taskId,
                    true,
                    false
            ));
            frozenPlayers.add(player.getUniqueId());
        } else {
            // Disable AI, gravity
            livingEntity.setAI(false);
            livingEntity.setGravity(false);
            new NBTEntity(livingEntity).getPersistentDataContainer().setBoolean("MagicModified", true);
            frozenEntities.add(livingEntity);
        }
    }
}
