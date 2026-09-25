package com.steve.skblock.events.routed;

import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import com.steve.skblock.menu.InventoryMenu;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


public class InventoryEvent implements Listener {

    private Plugin plugin;
    private final SessionRegistry sessionRegistry;

    public InventoryEvent(Plugin plugin, SessionRegistry sessionRegistry) {
        this.plugin = plugin;
        this.sessionRegistry = sessionRegistry;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getClickedInventory().getHolder() != null) {
                if (event.getClickedInventory().getHolder() instanceof InventoryMenu inventoryMenu) {
                    event.setCancelled(true);
                    Bukkit.getScheduler().runTaskLater(
                            plugin, () -> {
                                inventoryMenu.handleClick(event);
                            }, 1L
                    );
                    return;
                }
            }

            if (event.getClickedInventory().getType() == InventoryType.CRAFTING || event.getClickedInventory().getType() == InventoryType.WORKBENCH) {
                ItemStack itemStack = event.getClickedInventory().getItem(event.getRawSlot());
                if (event.getSlotType() == InventoryType.SlotType.RESULT && itemStack != null) {
                    SkyblockSession skyblockSession = sessionRegistry.getSession(event.getWhoClicked().getWorld());
                    if (skyblockSession != null) {
                        skyblockSession.onItemCrafted(itemStack);
                    }
                }
            }
        }
    }


}
