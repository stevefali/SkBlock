package com.steve.skblock.menu.shop;

import com.steve.skblock.game.SkyblockSession;
import com.steve.skblock.game.generator.GeneratorType;
import com.steve.skblock.menu.InventoryMenu;
import com.steve.skblock.menu.MenuItem;
import com.steve.skblock.util.NameFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class ShopMenu extends InventoryMenu {

    private final SkyblockSession skyblockSession;

    private static final List<ShopItem> ITEMS = List.of(
            new ShopItem(0, new ItemStack(Material.IRON_INGOT, 1), new ItemStack(Material.STICK, 6), GeneratorType.DEFAULT),
            new ShopItem(1, new ItemStack(Material.GOLD_INGOT, 8), new ItemStack(Material.IRON_INGOT, 1), GeneratorType.TERRACOTTA),
            new ShopItem(2, new ItemStack(Material.LAVA_BUCKET, 1), new ItemStack(Material.IRON_INGOT, 2), GeneratorType.DEEPSLATE)
    );

    private final MenuItem backButton = new MenuItem(
            26,
            Material.BARRIER,
            "Exit",
            List.of(),
            event -> {
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
            }
    );

    public ShopMenu(int size, SkyblockSession skyblockSession, Player player) {
        super(size, "§lSkyblock Shop");

        this.skyblockSession = skyblockSession;
        addNormalItem(backButton);
        populate(player);
    }

    public void populate(Player player) {
        for (ShopItem shopItem : ITEMS) {
            this.putItem(shopItem.rawSlot(), getIconItem(player, shopItem), event -> onItemClick(event, shopItem));
        }
    }


    private ItemStack getIconItem(Player player, ShopItem shopItem) {

        boolean canPlayerAfford = canPlayerPayPrice(player, shopItem.price());

        ItemStack iconItem = new ItemStack(shopItem.item().getType(), shopItem.item().getAmount());

        ItemMeta itemMeta = getMeta(iconItem);

        String affordFormat = canPlayerAfford ? "§a" : "§7";
        String itemName = NameFormatter.format(iconItem.getType().name());
        String priceName = NameFormatter.format(shopItem.price().getType().name());
        if (!isUnlocked(shopItem)) {
            itemMeta.setDisplayName("§c§m" + iconItem.getAmount() + " " + itemName + "§r§c (Locked)");
            itemMeta.setLore(List.of(
                    "Price: " + affordFormat + shopItem.price().getAmount() + " " + priceName,
                    "§cThis item is locked.",
                    "Complete quests to unlock"
            ));
        } else {
            itemMeta.setDisplayName(affordFormat + iconItem.getAmount() + " " + itemName);
            itemMeta.setLore(List.of(
                    "Price: " + affordFormat + shopItem.price().getAmount() + " " + priceName,
                    canPlayerAfford ? "Click to purchase" : "You can't afford this item"
            ));
        }
        iconItem.setItemMeta(itemMeta);

        return iconItem;
    }

    public void onItemClick(InventoryClickEvent event, ShopItem shopItem) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        if (!isUnlocked(shopItem)) {
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.0f, 1.0f);
            player.sendMessage("This item is locked. Complete more quests to unlock it");
            return;
        }

        if (!canPlayerPayPrice(player, shopItem.price())) {
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.0f, 1.0f);
            player.sendMessage("You can't afford this item. (Price: " + shopItem.price().getAmount() + " "
                    + NameFormatter.format(shopItem.price().getType().name()) + ")");
            return;
        }

        payItemPrice(player, shopItem.price());

        Map<Integer, ItemStack> overflow = player.getInventory().addItem(new ItemStack(shopItem.item().getType(), shopItem.item().getAmount()));
        for (ItemStack overflowStack : overflow.values()) {
            player.getWorld().dropItem(player.getLocation(), overflowStack);
        }

        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 1.5f, 2.0f);
        player.sendMessage("§aYou purchased " + shopItem.item().getAmount() + " " + NameFormatter.format(shopItem.item().getType().name())
                + " for " + shopItem.price().getAmount() + " " + NameFormatter.format(shopItem.price().getType().name()));

        populate(player);
    }

    private boolean isUnlocked(ShopItem shopItem) {
        return skyblockSession.getSkyblockProgress().isGeneratorUnlocked(shopItem.unlockLevel());
    }


    private static ItemMeta getMeta(ItemStack itemStack) {
        return itemStack.hasItemMeta()
                ? itemStack.getItemMeta()
                : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
    }

    private void addNormalItem(MenuItem menuItem) {
        ItemStack itemStack = new ItemStack(menuItem.material());
        ItemMeta itemMeta = getMeta(itemStack);
        itemMeta.setDisplayName(menuItem.displayName());
        itemMeta.setLore(menuItem.lore());
        itemStack.setItemMeta(itemMeta);
        this.putItem(menuItem.rawSlot(), itemStack, menuItem.interaction());
    }


    public static boolean canPlayerPayPrice(Player player, ItemStack price) {
        Map<Integer, ? extends ItemStack> matchedStacks = player.getInventory().all(price.getType());
        if (matchedStacks.isEmpty()) {
            return false;
        }

        int totalPossessed = 0;
        for (ItemStack stack : matchedStacks.values()) {
            totalPossessed += stack.getAmount();
        }
        return totalPossessed >= price.getAmount();
    }

    public static boolean payItemPrice(Player player, ItemStack price) {
        if (!canPlayerPayPrice(player, price)) {
            return false;
        }

        Map<Integer, ? extends ItemStack> matchedStacks = player.getInventory().all(price.getType());
        int priceRemaining = price.getAmount();
        for (Map.Entry<Integer, ? extends ItemStack> entry : matchedStacks.entrySet()) {
            if (priceRemaining <= 0) {
                return true;
            }
            ItemStack stack = (ItemStack) entry.getValue();
            if (stack.getAmount() <= priceRemaining) {
                player.getInventory().setItem(entry.getKey(), new ItemStack(Material.AIR));
                priceRemaining -= stack.getAmount();
            } else {
                player.getInventory().setItem(entry.getKey(), new ItemStack(price.getType(), stack.getAmount() - priceRemaining));
                priceRemaining = 0;
            }
        }
        return priceRemaining <= 0;
    }


}
