package com.steve.skblock.events.routed;

import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import com.steve.skblock.menu.InventoryMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
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
            Inventory top = event.getView().getTopInventory();
//            if (event.getClickedInventory().getHolder() != null) {
            if ((top.getHolder() instanceof InventoryMenu inventoryMenu)) {
                event.setCancelled(true);
                if (event.getClickedInventory() == top) {
                    Bukkit.getScheduler().runTaskLater(
                            plugin, () -> {
                                inventoryMenu.handleClick(event);
                            }, 1L
                    );
                    return;
                }
                return;
            }


//            if (event.getClickedInventory().getType() == InventoryType.CRAFTING || event.getClickedInventory().getType() == InventoryType.WORKBENCH) {
//                ItemStack itemStack = event.getClickedInventory().getItem(event.getRawSlot());
//                if (event.getSlotType() == InventoryType.SlotType.RESULT && itemStack != null) {
//                    SkyblockSession skyblockSession = sessionRegistry.getSession(event.getWhoClicked().getWorld());
//                    if (skyblockSession != null) {
//                        skyblockSession.onItemCrafted(itemStack);
//                        plugin.getLogger().info("event level itemstack amount: " + itemStack.getAmount());
//                    }
//                }
//            }
        }
    }


    // TODO: Move this?
    @EventHandler(ignoreCancelled = true)
    public void onItemCrafted(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        SkyblockSession skyblockSession = sessionRegistry.getSession(player.getWorld());
        if (skyblockSession != null) {
            Material itemType = event.getRecipe().getResult().getType();
            int preCraftAmount = countInInventory(player.getInventory(), itemType) + event.getCursor().getAmount();

            Bukkit.getScheduler().runTask(plugin, () -> {
                int postCraftAmount = countInInventory(player.getInventory(), itemType) + event.getCursor().getAmount();
                int craftedAmount = postCraftAmount - preCraftAmount;
                if (craftedAmount > 0) {
                    skyblockSession.onItemCrafted(new ItemStack(itemType, craftedAmount));
                }
            });
        }
    }

    private int countInInventory(Inventory inventory, Material type) {
        int total = 0;
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType() == type) {
                total += itemStack.getAmount();
            }
        }
        return total;
    }


}
