package com.steve.skblock.events;

import com.steve.skblock.Skblock;
import com.steve.skblock.menu.InventoryMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class BlockEvent implements Listener {

    private Plugin plugin;
    private static final String BUNGEE_CHANNEL = "BungeeCord";


    public BlockEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Material type = event.getBlock().getType();
        Player player = event.getPlayer();

        /*if (type == Material.CRYING_OBSIDIAN) {
            System.out.println("Counting entities....");
            for (String worldName : Skblock.getNpcIds().keySet()) {
                for (UUID uuid : Skblock.getNpcIds().get(worldName)) {
                    System.out.println(worldName + ": " + uuid);
                }
            }

            World world = event.getBlock().getWorld();
            List<Entity> stands = world.getEntities().stream().filter(entity -> entity.getType() == EntityType.ARMOR_STAND && entity.getCustomName() != null).toList();
            List<Entity> displays = world.getEntities().stream().filter(entity -> entity.getType() == EntityType.TEXT_DISPLAY).toList();
            System.out.println("Armor stands: " + stands.size());
            System.out.println("TextDisplays: " + displays.size());
        }

        if (type == Material.BROWN_WOOL) {
            System.out.println("Removing Entities....");
            World world = event.getBlock().getWorld();
            List<Entity> stands = world.getEntities().stream().filter(entity -> entity.getType() == EntityType.ARMOR_STAND && entity.getCustomName() != null).toList();
            for (Entity stand : stands) {
                stand.remove();
            }
            List<Entity> displays = world.getEntities().stream().filter(entity -> entity.getType() == EntityType.TEXT_DISPLAY).toList();
            for (Entity display : displays) {
                display.remove();
            }
        }

        if (type == Material.YELLOW_WOOL) {
            World world = event.getPlayer().getWorld();
            Skblock.getNpcIds().remove(world.getName());
            Skblock.getNpcService().removeAllNpcsInWorld(world.getName());
        }*/

        if (type == Material.BLACK_WOOL) {


        }


    }


}
