package com.gardensmc.gardensmagic.ability;

import com.gardensmc.gardensmagic.ability.helper.ItemProjectileLauncher;
import com.gardensmc.gardensmagic.ability.helper.ItemProjectileProperties;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlockLaunchAbility extends Ability {

    protected BlockLaunchAbility() {
        super("block_launch");
    }

    @Override
    public void cast(Player caster) {
        ItemStack offHand = caster.getInventory().getItemInOffHand();
        if (offHand.getType() == Material.AIR) {
            return;
        }
        if (!offHand.getType().isBlock()) {
            return; // only launch blocks
        }

        // launch projectile
        new ItemProjectileLauncher(
                caster,
                ItemProjectileProperties
                        .builder()
                        .projectileType(ItemProjectileLauncher.ProjectileType.BLOCK)
                        .material(offHand.getType())
                        .build()
        ).launch();

        // decrement item
        offHand.setAmount(offHand.getAmount() - 1);
    }
}
