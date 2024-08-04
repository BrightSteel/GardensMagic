package com.gardensmc.gardensmagic.command;

import com.gardensmc.gardensmagic.ability.Abilities;
import com.gardensmc.gardensmagic.ability.Ability;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CastCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to send this command");
            return true;
        }

        String abilityName = args[0];
        Ability ability = Abilities.abilityMap.get(abilityName.toLowerCase());
        if (ability != null) {
            ability.cast(player);
            player.sendMessage("Casted " + abilityName);
        } else {
            player.sendMessage("Error: No ability named " + abilityName);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return Abilities.abilityMap.keySet()
                .stream()
                .toList();
    }
}
