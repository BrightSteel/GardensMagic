package com.gardensmc.gardensmagic.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MagicPlaceholder extends PlaceholderExpansion {

    private static final int HP_BAR_SCALE = 15;
    private static final char NEG_1 = 'ꐡ';

    @Override
    public @NotNull String getIdentifier() {
        return "GardensMagic";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Bright_Steel";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null || player.getPlayer() == null) {
            return null;
        }

        Player onlinePlayer = player.getPlayer();
        if (params.equalsIgnoreCase("health")) {
            var health = onlinePlayer.getHealth();
            AttributeInstance attribute = onlinePlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            double maxHealth = attribute != null ? attribute.getValue() : 20;

            return getHpBar(health, maxHealth);
        } else if (params.contains("health")) {
            // likely mob health
            // should be health_<health>_<maxhealth>
            String[] healths = params.split("_");
            if (healths.length < 3) {
                return null;
            }
            double health = Double.parseDouble(healths[1]);
            double maxHealth = Double.parseDouble(healths[2]);
            return getHpBar(health, maxHealth);
        }
        return null;
    }

    private String getHpBar(double health, double maxHealth) {
        double barsPerHealth = HP_BAR_SCALE / maxHealth;
        int redBars = (int) Math.floor(barsPerHealth * health);
        int emptyBars = HP_BAR_SCALE - redBars;
        // bar + health number
        return buildHpBar(redBars, emptyBars) + " " + (int) Math.floor(health);
    }

    private String buildHpBar(int redCount, int emptyCount) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= redCount; i++) {
            stringBuilder
                    .append(getRedChar(i))
                    .append(NEG_1);
        }
        for (int i = redCount + 1; i <= redCount + emptyCount; i++) {
            stringBuilder
                    .append(getEmptyChar(i))
                    .append(NEG_1);
        }
        var hpBar = stringBuilder.toString();
        return hpBar.substring(0, hpBar.length() - 1); // remove trailing negative
    }

    private char getEmptyChar(int emptyBarInSequence) {
        char emptyChar = ' ';
        switch (emptyBarInSequence) {
            case 1 -> emptyChar = '\uE20F';
            case 2,3,13,14 -> emptyChar = '\uE21A';
            case 4 -> emptyChar = '\uE21B';
            case 5,6,7,8,9,10,11 -> emptyChar = '\uE21C';
            case 12 -> emptyChar = '\uE21D';
            case 15 -> emptyChar = '\uE21E';
        }
        return emptyChar;
    }

    private char getRedChar(int redBarInSequence) {
        char redChar = ' ';
        switch (redBarInSequence) {
            case 1 -> redChar = '\uE210';
            case 2,3,13,14 -> redChar = '\uE211';
            case 4 -> redChar = '\uE212';
            case 5,6,7,8,9,10,11 -> redChar = '\uE213';
            case 12 -> redChar = '\uE214';
            case 15 -> redChar = '\uE215';
        }
        return redChar;
    }
}
