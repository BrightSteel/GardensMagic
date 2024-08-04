package com.gardensmc.gardensmagic.listener;

import com.gardensmc.gardensmagic.ability.Abilities;
import com.gardensmc.gardensmagic.ability.tracker.AbilityTracker;
import de.tr7zw.nbtapi.NBTEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.*;

@SuppressWarnings("unused")
public class EntityListener extends BukkitListener {

    @EventHandler
    public void preventAbilityKnockedBack(EntityKnockbackEvent event) {
        if (!event.getEntity().hasGravity()) {
            NBTEntity nbtEntity = new NBTEntity(event.getEntity());
            if (nbtEntity.getPersistentDataContainer().hasTag("MagicAbility")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerFall(EntityDamageByBlockEvent e) {
        var isPlayer = e.getEntity().getType() == EntityType.PLAYER;
        var isFallDamage = e.getCause() == EntityDamageEvent.DamageCause.FALL;
        if (isPlayer && isFallDamage) {
            var uuid = e.getEntity().getUniqueId();
            var trackedAbility = AbilityTracker.getTrackedAbility(uuid);
            if (trackedAbility != null && trackedAbility.getAbilityName().equals(Abilities.DRAGON_FLIGHT.getName())) {
                e.getEntity().getWorld().getNearbyEntities(e.getEntity().getLocation(), 5, 5, 5)
                        .stream()
                        .filter(entity -> entity instanceof LivingEntity)
                        .map(entity -> (LivingEntity) entity)
                        .forEach(livingEntity -> knockbackEnemy(e.getEntity(), livingEntity));
            }
            // remove tracked ability
            AbilityTracker.removeTrackedAbility(uuid);
        }
    }

    private void knockbackEnemy(Entity entitySource, LivingEntity livingEntity) {
        var direction = entitySource.getLocation().toVector()
                .subtract(livingEntity.getLocation().toVector())
                .normalize()
                .multiply(2);

        livingEntity.setVelocity(livingEntity.getVelocity().add(direction));
        livingEntity.damage(5);
    }
}
