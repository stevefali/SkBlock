package com.steve.skblock.menu;

import com.steve.skblock.Skblock;
import com.steve.skblock.util.TitlesUtils;
import com.steve.skblock.util.teleport.ProxyTeleport;
import com.steve.skblock.util.teleport.SkyblockWorldTeleport;
import com.steve.skblock.worlds.WorldDeleter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MenuProvider {

    public static final String SKYBLOCK_MENU_KEY = "skyblock_menu";
    public static final String LOBBY_MENU_KEY = "lobby_menu";

    private static final Map<String, InventoryMenu> inventoryMenus = new HashMap<>();

    public static void register(Plugin plugin) {

        InventoryMenu skyblockMenu = new InventoryMenu(9 * 3, "Skyblock Menu");
        InventoryMenu lobbyMenu = new InventoryMenu(9 * 3, "Lobby Menu");

        inventoryMenus.put(SKYBLOCK_MENU_KEY, skyblockMenu);
        inventoryMenus.put(LOBBY_MENU_KEY, lobbyMenu);

        MenuItem mainLobbyTeleport = new MenuItem(
                12,
                Material.DARK_OAK_DOOR,
                "§eMain Lobby",
                List.of("Click to teleport"),
                event -> {
                    Player player = (Player) event.getWhoClicked();
                    TitlesUtils.sendSubtitle(player, "§9Sending you to main lobby...", 5, 30, 5);
                    player.closeInventory();
                    ProxyTeleport.teleportPlayer(plugin, player, ProxyTeleport.LOBBY_SERVER);
                }
        );

        MenuItem worldTeleport = new MenuItem(
                14,
                Material.GRASS_BLOCK,
                "§eYour Skyblock World",
                List.of("Click to teleport"),
                event -> {
                    Player player = (Player) event.getWhoClicked();
                    TitlesUtils.sendSubtitle(player, "§9Preparing your Skyblock world...", 5, 30, 5);
                    player.closeInventory();
                    SkyblockWorldTeleport.sendPlayerToSkyblockWorld(player, plugin);
                }
        );

        MenuItem worldDelete = new MenuItem(
                8,
                Material.TNT,
                "§4Delete your Skyblock world",
                List.of(
                        "§c§lWARNING!!",
                        "§cThis will delete §lALL §cyour progress in Skyblock!",
                        "§eThis action can not be undone."
                ),
                event -> {
                    Player player = (Player) event.getWhoClicked();
                    player.sendMessage("§8Deleting your skyblock world...");
                    String playerUuid = player.getUniqueId().toString();
                    WorldDeleter.deleteWorld(playerUuid, player, plugin);
                    player.closeInventory();
                }
        );

        MenuItem skyblockLobbyTeleport = new MenuItem(
                22,
                Material.DIRT,
                "§eSkyblock Lobby",
                List.of("Click to teleport"),
                event -> {
                    Player player = (Player) event.getWhoClicked();
                    TitlesUtils.sendSubtitle(player, "§9Sending you to Skblock lobby", 5, 30, 5);
                    player.teleport(Skblock.getLobbySpawn());
                }
        );

        // Lobby Menu
        addItem(lobbyMenu, mainLobbyTeleport, worldTeleport, worldDelete);

        // Skyblock Menu
        addItem(skyblockMenu, mainLobbyTeleport, skyblockLobbyTeleport);

    }

    public static void addItem(InventoryMenu inventoryMenu, MenuItem... menuItems) {
        for (MenuItem menuItem : menuItems) {
            ItemStack item = new ItemStack(menuItem.material());
            ItemMeta meta = getMeta(item);
            meta.setDisplayName(menuItem.displayName());
            meta.setLore(menuItem.lore());
            item.setItemMeta(meta);
            inventoryMenu.putItem(menuItem.rawSlot(), item, menuItem.interaction());
        }
    }

    public static void addItem(
            InventoryMenu inventoryMenu,
            int rawSlot,
            Material material,
            String displayName,
            List<String> lore,
            Consumer<InventoryClickEvent> interaction) {

        ItemStack item = new ItemStack(material);
        ItemMeta meta = getMeta(item);
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inventoryMenu.putItem(rawSlot, item, interaction);
    }

    public static InventoryMenu getMenu(String menuKey) {
        return inventoryMenus.get(menuKey);
    }

    private static ItemMeta getMeta(ItemStack itemStack) {
        return itemStack.hasItemMeta()
                ? itemStack.getItemMeta()
                : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
    }

}
