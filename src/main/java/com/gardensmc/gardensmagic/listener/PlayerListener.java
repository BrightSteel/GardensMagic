package com.gardensmc.gardensmagic.listener;

import com.gardensmc.gardensmagic.ability.tracker.AbilityTracker;
import com.gardensmc.gardensmagic.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("unused")
public class PlayerListener extends BukkitListener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null) { // not sure when this happens ?
            return;
        }
        var from = LocationUtil.stripYawAndPitch(event.getFrom());
        var to = LocationUtil.stripYawAndPitch(event.getTo());
        if (from.equals(to)) { // only moved yaw/pitch
            return;
        }
        var trackedAbility = AbilityTracker.getTrackedAbility(event.getPlayer().getUniqueId());
        if (trackedAbility != null && trackedAbility.isPreventMovement()) {
            // prevent movement during ability
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            var trackedAbility = AbilityTracker.getTrackedAbility(player.getUniqueId());
            if (trackedAbility != null && trackedAbility.isPreventDamage()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerLeaveGame(PlayerQuitEvent event) {
        var uuid = event.getPlayer().getUniqueId();
        var trackedAbility = AbilityTracker.getTrackedAbility(uuid);
        if (trackedAbility != null) {
            var taskId = trackedAbility.getTaskId();
            if (taskId!= null) {
                // cancel any lingering tasks
                Bukkit.getScheduler().cancelTask(taskId);
            }
            // remove tracked ability
            AbilityTracker.removeTrackedAbility(uuid);
        }
    }

    @EventHandler
    public void onPlayerJoinGame(PlayerJoinEvent event) {
        // remove left-over freeze ticks
        var player = event.getPlayer();
        if (player.getFreezeTicks() > 500) {
            player.setFreezeTicks(0);
        }
    }
}
