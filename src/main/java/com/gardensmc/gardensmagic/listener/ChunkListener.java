package com.gardensmc.gardensmagic.listener;

import de.tr7zw.nbtapi.NBTEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;

@SuppressWarnings("unused")
public class ChunkListener extends BukkitListener {

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (!event.isNewChunk()) {
            for (Entity entity : event.getChunk().getEntities()) {
                    var nbtEntity = new NBTEntity(entity).getPersistentDataContainer();
                    if (nbtEntity.hasTag("MagicAbility")) {
                        // remove lingering magic abilities
                        entity.remove();
                    } else if (entity instanceof LivingEntity livingEntity && nbtEntity.hasTag("MagicModified")) {
                        livingEntity.setAI(true);
                        livingEntity.setGravity(true);
                        nbtEntity.removeKey("MagicModified");
                    }
            }
        }
    }
}
