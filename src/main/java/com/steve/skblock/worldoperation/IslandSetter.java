package com.steve.skblock.worldoperation;

import com.sk89q.worldedit.world.block.BlockTypes;
import com.steve.skblock.Skblock;
import com.steve.skblock.util.Vector2D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class IslandSetter {

    private static final String SCHEMATICS_PATH = "plugins/FastAsyncWorldEdit/schematics/";
    private static final Plugin plugin = JavaPlugin.getPlugin(Skblock.class);
    private static final int ISLAND_HEIGHT = 65;
    private static final int LOWEST = -64;
    private static final int HIGHEST = 319;


    public static void setIslandInSection(int sectionIndex, World world, Player messageRecipient) {
        List<CompletableFuture<Boolean>> sectionTasks = new ArrayList<>();
        int startY = LOWEST;
        int endY = HIGHEST;

        switch (sectionIndex) {
            case 0: { // Plains
                sectionTasks.add(pasteIsland(world, "island_plains.schem", new Vector2D(0, 0)));
                break;
            }
            case 1: { // Desert
                sectionTasks.add(pasteIsland(world, "island_desert.schem", new Vector2D(0, -100)));
                sectionTasks.add(removeBarrier(new Vector(-50, startY, -51), new Vector(50, endY, -51), world));
                sectionTasks.add(removeBarrier(new Vector(-50, startY, -600), new Vector(50, endY, -600), world));
                break;
            }
            case 2: { // Jungle
                sectionTasks.add(pasteIsland(world, "island_jungle.schem", new Vector2D(-100, -100)));
                sectionTasks.add(removeBarrier(new Vector(-51, startY, -599), new Vector(-51, endY, -52), world));
                sectionTasks.add(removeBarrier(new Vector(-600, startY, -600), new Vector(-51, endY, -600), world));
                sectionTasks.add(removeBarrier(new Vector(-600, startY, -599), new Vector(-600, endY, -52), world));
                break;
            }
            case 3: { // Badlands
                sectionTasks.add(pasteIsland(world, "island_badlands.schem", new Vector2D(-100, 0)));
                sectionTasks.add(removeBarrier(new Vector(-599, startY, -51), new Vector(-52, endY, -51), world));
                sectionTasks.add(removeBarrier(new Vector(-51, startY, -51), new Vector(-51, endY, 50), world));
                sectionTasks.add(removeBarrier(new Vector(-600, startY, -51), new Vector(-600, endY, 50), world));
                break;
            }
            case 4: { // Savanna
                sectionTasks.add(pasteIsland(world, "island_savanna.schem", new Vector2D(-100, 100)));
                sectionTasks.add(removeBarrier(new Vector(-599, startY, 51), new Vector(-52, endY, 51), world));
                sectionTasks.add(removeBarrier(new Vector(-600, startY, 51), new Vector(-600, endY, 599), world));
                sectionTasks.add(removeBarrier(new Vector(-600, startY, 600), new Vector(-52, endY, 600), world));
                break;
            }
            case 5: { // Taiga
                sectionTasks.add(pasteIsland(world, "island_taiga.schem", new Vector2D(0, 100)));
                sectionTasks.add(removeBarrier(new Vector(-51, startY, 52), new Vector(-51, endY, 600), world));
                sectionTasks.add(removeBarrier(new Vector(-51, startY, 51), new Vector(50, endY, 51), world));
                sectionTasks.add(removeBarrier(new Vector(-50, startY, 600), new Vector(50, endY, 600), world));
                break;
            }
            case 6: { // Nether
                sectionTasks.add(pasteIsland(world, "island_nether.schem", new Vector2D(100, 100)));
                sectionTasks.add(removeBarrier(new Vector(51, startY, 52), new Vector(51, endY, 599), world));
                sectionTasks.add(removeBarrier(new Vector(51, startY, 600), new Vector(600, endY, 600), world));
                sectionTasks.add(removeBarrier(new Vector(600, startY, 52), new Vector(600, endY, 599), world));
                break;
            }
            case 7: { // Ice Spikes
                sectionTasks.add(pasteIsland(world, "island_ice_spikes.schem", new Vector2D(100, 0)));
                sectionTasks.add(removeBarrier(new Vector(51, startY, -50), new Vector(51, endY, 50), world));
                sectionTasks.add(removeBarrier(new Vector(51, startY, 51), new Vector(599, endY, 51), world));
                sectionTasks.add(removeBarrier(new Vector(600, startY, -50), new Vector(600, endY, 51), world));
                break;
            }
            case 8: { // End
                sectionTasks.add(pasteIsland(world, "island_end.schem", new Vector2D(100, -100)));
                sectionTasks.add(removeBarrier(new Vector(51, startY, -600), new Vector(51, endY, -51), world));
                sectionTasks.add(removeBarrier(new Vector(52, startY, -51), new Vector(599, endY, -51), world));
                sectionTasks.add(removeBarrier(new Vector(600, startY, -599), new Vector(600, endY, -51), world));
                sectionTasks.add(removeBarrier(new Vector(52, startY, -600), new Vector(600, endY, -600), world));
                break;
            }
            default:
                plugin.getLogger().warning("Error pasting schematic: Please specify a section number between 0 and 8");
        }

        CompletableFuture.allOf(sectionTasks.toArray(new CompletableFuture[0]))
                .whenComplete((ignored, throwable) -> {
                    Bukkit.getScheduler().runTask(
                            plugin, () -> {
                                performSectionFinishSteps(sectionIndex, world, messageRecipient, throwable);
                            }
                    );
                });
    }

    private static CompletableFuture<Boolean> pasteIsland(World world, String schematicName, Vector2D coords) {
        return SchematicOperation.pasteSchematic(
                        SCHEMATICS_PATH + schematicName,
                        world,
                        plugin,
                        new Vector(coords.x(), ISLAND_HEIGHT, coords.y())
                )
                .exceptionally(throwable -> {
                    plugin.getLogger().warning("Error pasting schematic " + schematicName + " in world " + world.getName() + ": " + throwable.getMessage());
                    throwable.printStackTrace();
                    return false;
                })
                .thenApply(success -> success);
    }

    private static CompletableFuture<Boolean> removeBarrier(Vector start, Vector end, World world) {
        return CuboidOperation.performCuboidSectionOperationAsync(plugin, start, end, world, BlockTypes.AIR)
                .exceptionally(throwable -> {
                    plugin.getLogger().warning("Error removing barrier in world " + world.getName() + ": " + throwable.getMessage());
                    throwable.printStackTrace();
                    return false;
                })
                .thenApply(success -> success);
    }

    private static void performSectionFinishSteps(int sectionIndex, World world, Player messageRecipient, Throwable throwable) {
        if (throwable != null) {
            plugin.getLogger().warning("Error performing island section operation in world " + world.getName() + ": " + throwable.getMessage()
            );
            return;
        }

        plugin.getLogger().info("Section operations completed for section " + sectionIndex + " in world " + world.getName());

        String islandName;

        switch (sectionIndex) {
            case 0: {
                islandName = "Plains";
                break;
            }
            case 1: {
                islandName = "Desert";
                break;
            }
            case 2: {
                islandName = "Jungle";
                Location location = new Location(world, -100, 65, -100);
                Block block = location.getBlock();
                block.setType(Material.CHEST);
                Chest chest = (Chest) block.getState();

                Inventory inventory = chest.getInventory();
                inventory.setItem(3, new ItemStack(Material.STICK, 4));
                break;
            }
            case 3: {
                islandName = "Badlands";
                Location location = new Location(world, -102, 61, -3);
                StorageMinecart minecartChest = (StorageMinecart) world.spawnEntity(location, EntityType.CHEST_MINECART);

                Inventory minecartInventory = minecartChest.getInventory();
                minecartInventory.setItem(7, new ItemStack(Material.SHORT_GRASS, 3));
                break;
            }
            case 4: {
                islandName = "Savanna";
                Location location = new Location(world, -103, 67, 99);
                world.spawnEntity(location, EntityType.BEE);
                break;
            }
            case 5: {
                islandName = "Taiga";
                break;
            }
            case 6: {
                islandName = "Nether";
                break;
            }
            case 7: {
                islandName = "Ice Spikes";
                break;
            }
            case 8: {
                islandName = "The End";
                break;
            }
            default: {
                islandName = "";
            }
        }

        messageRecipient.sendMessage("§aA new island has appeared: " + islandName + "!");

    }

}
