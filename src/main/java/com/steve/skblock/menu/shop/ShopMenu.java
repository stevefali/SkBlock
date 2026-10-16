package com.steve.skblock.menu.shop;

import com.steve.skblock.game.SkyblockSession;
import com.steve.skblock.game.generator.GeneratorType;
import com.steve.skblock.menu.InventoryMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopMenu extends InventoryMenu {

    private final Player player;
    private final SkyblockSession skyblockSession;

    public static final ShopItem IRON_1 = new ShopItem(0, new ItemStack(Material.IRON_INGOT, 1), new ItemStack(Material.STICK, 6));
    public static final ShopItem GOLD_8 = new ShopItem(1, new ItemStack(Material.GOLD_INGOT, 8), new ItemStack(Material.IRON_INGOT, 1));
    public static final ShopItem LAVA = new ShopItem(2, new ItemStack(Material.LAVA_BUCKET, 1), new ItemStack(Material.IRON_INGOT, 2));

    private Map<ShopItem, GeneratorType> items = new HashMap<>();

    public ShopMenu(int size, Player player, SkyblockSession skyblockSession) {
        super(size, "Skyblock Shop");

        this.player = player;
        this.skyblockSession = skyblockSession;

        setup();
    }

    public void setup() {

        items.put(IRON_1, GeneratorType.DEFAULT);
        items.put(GOLD_8, GeneratorType.TERRACOTTA);
        items.put(LAVA, GeneratorType.DEEPSLATE);

        for (Map.Entry<ShopItem, GeneratorType> entry : items.entrySet()) {
            addItem(entry.getKey(), entry.getValue());
        }
        updateUI();
    }

    private void addItem(ShopItem shopItem, GeneratorType unlockLevel) {
        boolean isUnlocked = skyblockSession.getSkyblockProgress().isGeneratorUnlocked(unlockLevel);
        if (isUnlocked) {
            shopItem.unlock();
        }
        shopItem.setAffordable(canPlayerPayPrice(player, shopItem.getPrice()));
        this.putItem(shopItem.getRawSlot(), shopItem.getItem(), shopItem::handleClick);
    }

    public void unlockItems(List<ShopItem> stageItems) {
        for (ShopItem item : stageItems) {
            item.unlock();
        }
    }

    public void updateUI() {
        for (ShopItem item : items.keySet()) {
            item.setAffordable(canPlayerPayPrice(player, item.getPrice()));
            item.setMeta();
        }
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
