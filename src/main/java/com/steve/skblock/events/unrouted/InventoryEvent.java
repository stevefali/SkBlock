package com.steve.skblock.events.unrouted;

import com.steve.skblock.menu.InventoryMenu;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;


public class InventoryEvent implements Listener {

    private Plugin plugin;

    public InventoryEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getClickedInventory().getHolder() != null) {
                if (event.getClickedInventory().getHolder() instanceof InventoryMenu inventoryMenu) {
                    event.setCancelled(true);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        inventoryMenu.handleClick(event);
                    }, 1L);

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
