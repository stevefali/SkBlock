package com.steve.skblock.menu.shop;

import com.steve.skblock.util.NameFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ShopItem {

    int rawSlot;
    private ItemStack price;
    private ItemStack item;
    private boolean isUnlocked = false;
    private boolean canPlayerAfford = false;

    public ShopItem(int rawSlot, ItemStack item, ItemStack price) {
        this.price = price;
        this.item = item;
        this.rawSlot = rawSlot;
        setMeta();
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        if (!isUnlocked) {
            player.playSound(player, Sound.BLOCK_GRAVEL_STEP, 1.0f, 1.0f);
            player.sendMessage("This item is locked. Complete more quests to unlock it");
            return;
        }

        canPlayerAfford = ShopMenu.canPlayerPayPrice(player, price);

        if (!canPlayerAfford) {
            player.playSound(player, Sound.BLOCK_GRAVEL_STEP, 1.0f, 1.0f);
            player.sendMessage("You can't afford this item. (Price: " + price.getAmount() + " " + NameFormatter.format(price.getType().name()));
            return;
        }

        boolean isPaySuccess = ShopMenu.payItemPrice(player, price);
        if (!isPaySuccess) {
            player.playSound(player, Sound.BLOCK_GRAVEL_STEP, 1.0f, 1.0f);
            player.sendMessage("§eThe purchase was unsuccessful :(");
            return;
        }

        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 1.5f, 1.0f);
        player.getInventory().addItem(item);
        player.sendMessage("§aYou purchased " + item.getAmount() + " " + NameFormatter.format(item.getType().name())
                + " for " + price.getAmount() + " " + NameFormatter.format(price.getType().name()));

        canPlayerAfford = ShopMenu.canPlayerPayPrice(player, price);
        setMeta();
    }


    public void unlock() {
        this.isUnlocked = true;
        setMeta();
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setAffordable(boolean isAffordable) {
        this.canPlayerAfford = isAffordable;
//        getDescription();
//        getLore();
    }

    public boolean canPlayerAfford() {
        return canPlayerAfford;
    }


    private String getDescription() {
        String name = NameFormatter.format(item.getType().name());
        if (!isUnlocked) {
            return "§c§m" + item.getAmount() + " " + name + "§r§c (Locked)";
        }

        return affordabilityFormat() + item.getAmount() + " " + name;
    }

    private List<String> getLore() {
        String priceName = NameFormatter.format(price.getType().name());
        if (!isUnlocked) {
            return List.of(
                    "Price: " + affordabilityFormat() + price.getAmount() + " " + priceName,
                    "§cThis item is locked.",
                    "Complete quests to unlock"
            );
        }
        return List.of(
                "Price: " + affordabilityFormat() + price.getAmount() + " " + priceName,
                canPlayerAfford ? "Click to purchase" : "You can't afford this item"
        );
    }

    private String affordabilityFormat() {
        return canPlayerAfford ? "§a" : "§7";
    }

    private static ItemMeta getMeta(ItemStack itemStack) {
        return itemStack.hasItemMeta()
                ? itemStack.getItemMeta()
                : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
    }

    public void setMeta() {
        ItemMeta meta = getMeta(item);
        meta.setDisplayName(getDescription());
        meta.setLore(getLore());
        item.setItemMeta(meta);
    }

    public ItemStack getPrice() {
        return this.price;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public int getRawSlot() {
        return this.rawSlot;
    }


}
