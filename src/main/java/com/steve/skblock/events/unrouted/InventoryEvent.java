package com.steve.skblock.events.unrouted;

import com.steve.skblock.menu.InventoryMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class InventoryEvent implements Listener {


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getClickedInventory().getHolder() != null) {
                if (event.getClickedInventory().getHolder() instanceof InventoryMenu inventoryMenu) {
                    event.setCancelled(true);
                    inventoryMenu.handleClick(event);
                    return;
                }
            }
        }
        /*
            TODO: Crafted item check (plan):
            if (event.getClickedInventory().getType() == InventoryType.CRAFTING || event.getClickedInventory().getType() == InventoryType.WORKBENCH) {
                if (event.getSlotType() == InventoryType.SlotType.RESULT && event.getClickedInventory().getItem(event.getRawSlot()) != null) {
                    // Check item type here!
                }
            }
*/

    }


}
