package com.gardensmc.gardensmagic.ability;

import net.minecraft.world.item.ItemChorusFruit;
import net.minecraft.world.item.Items;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnderShiftAbility extends Ability {

    private static final int WORLD_MIN_HEIGHT = -64;

    public EnderShiftAbility() {
        super("ender_shift");
    }

    @Override
    public void cast(Player caster) {
        ItemChorusFruit chorusFruit = (ItemChorusFruit)Items.uZ;
        var dummyStack = CraftItemStack.asNMSCopy(new ItemStack(Material.CHORUS_FRUIT));
        var world = ((CraftWorld)caster.getWorld()).getHandle();
        var player = ((CraftPlayer)caster).getHandle();
        chorusFruit.a(dummyStack, world, player);
//        new net.minecraft.world.item.ItemStack(Items.uZ).a();
//        itemStack.a(itemStack, );
    }

    // Copied from chorus fruit teleport
//    private void randomTeleport(Player player) {
//        World world = player.getWorld();
//        Location loc = player.getLocation();
//        var rand = ThreadLocalRandom.current();
//        for(int i = 0; i < 16; ++i) {
//            double d = loc.getX() + (rand.nextDouble() - 0.5) * 16.0;
//            double e = MathHelper.clamp(
//                    loc.getY() + (double)(rand.nextInt(16) - 8),
//                    WORLD_MIN_HEIGHT,
//                    WORLD_MIN_HEIGHT + world.getMaxHeight() - 1 // not sure if this is right
//            );
//            double f = loc.getZ() + (rand.nextDouble() - 0.5) * 16.0;
//            if (player.getVehicle() != null) {
//                player.leaveVehicle();
//            }
//
//            if (player.teleport(new Location(world, d, e, f), PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT)) {
//                world.playSound(loc, Sound.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
//                world.spawnParticle(Particle.PORTAL, loc, 5);
//                break;
//            }
//        }
//    }
}
