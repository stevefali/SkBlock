package com.steve.skblock.menu;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.function.Consumer;

public record MenuItem(
        int rawSlot,
        Material material,
        String displayName,
        List<String> lore,
        Consumer<InventoryClickEvent> interaction
) {
}
