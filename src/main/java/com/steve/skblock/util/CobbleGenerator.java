package com.steve.skblock.util;

import org.bukkit.Material;

public class CobbleGenerator {


    private static final int MIN = 1;
    private static final int MAX = 240;

    public static Material determineBlock() {
        int randomNumber = MIN + (int) (Math.random() * ((MAX - MIN) + 1));

        if (isInRange(randomNumber, 1, 154)) { // 154:240
            return Material.COBBLESTONE;
        } else if (isInRange(randomNumber, 155, 179)) { // 25:240 chance
            return Material.IRON_ORE;
        } else if (isInRange(randomNumber, 180, 204)) { // 25:240 chance
            return Material.COAL_ORE;
        } else if (isInRange(randomNumber, 205, 219)) { // 15:240 chance
            return Material.LAPIS_ORE;
        } else if (isInRange(randomNumber, 220, 230)) { // 11:240 chance
            return Material.REDSTONE_ORE;
        } else if (isInRange(randomNumber, 231, 237)) { // 7:240 chance
            return Material.GOLD_ORE;
        } else if (isInRange(randomNumber, 238, 240)) { // 3:240 chance
            return Material.DIAMOND_ORE;
        }

        return Material.COBBLESTONE;
    }

    private static boolean isInRange(int query, int lowest, int highest) {
        return query >= lowest && query <= highest;
    }

}
