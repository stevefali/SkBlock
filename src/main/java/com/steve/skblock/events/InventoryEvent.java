package com.steve.skblock.events;

import com.steve.skblock.menu.InventoryMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class InventoryEvent implements Listener {


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory().getHolder() != null) {
            if (event.getClickedInventory().getHolder() != null) {
                if (event.getClickedInventory().getHolder() instanceof InventoryMenu inventoryMenu) {
                    event.setCancelled(true);
                    inventoryMenu.handleClick(event);
                }
            }
        }

    }

}
