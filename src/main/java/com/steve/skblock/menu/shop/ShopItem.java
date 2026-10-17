package com.steve.skblock.menu.shop;

import com.steve.skblock.game.generator.GeneratorType;
import org.bukkit.inventory.ItemStack;


public record ShopItem(int rawSlot, ItemStack item, ItemStack price, GeneratorType unlockLevel) {
}
