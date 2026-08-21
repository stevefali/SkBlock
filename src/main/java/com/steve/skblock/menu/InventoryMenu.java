package com.steve.skblock.menu;


import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class InventoryMenu implements InventoryHolder {

    private final Inventory inventory;
    private final Map<Integer, Consumer<InventoryClickEvent>> clickHandlers = new HashMap<>();

    public InventoryMenu(int size, String title) {
        this.inventory = Bukkit.createInventory(this, size, title);
    }

    public void putItem(int rawSlot, ItemStack item, Consumer<InventoryClickEvent> onInteract) {
        inventory.setItem(rawSlot, item);
        clickHandlers.put(rawSlot, onInteract);
    }

    public void handleClick(InventoryClickEvent event) {
        Consumer<InventoryClickEvent> clickHandler = clickHandlers.get(event.getRawSlot());
        if (clickHandler != null) {
            clickHandler.accept(event);
        }
    }


    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
