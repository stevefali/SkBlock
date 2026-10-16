package com.steve.skblock.worldoperation;

import com.steve.skblock.Skblock;
import com.steve.skblock.game.SkyblockSession;
import com.steve.skblock.game.generator.GeneratorType;
import com.steve.skblock.menu.shop.ShopItem;
import com.steve.skblock.menu.shop.ShopMenu;
import com.steve.skblock.util.Vector2D;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.phys.Vec3;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class IslandSetter {

    private static final String SCHEMATICS_PATH = "plugins/WorldEdit/schematics/";
    private static final Plugin plugin = JavaPlugin.getPlugin(Skblock.class);
    private static final int ISLAND_HEIGHT = 65;
    private static final int LOWEST = -64;
    private static final int HIGHEST = 319;

    private static final Vector2D TAIGA_COORDS = new Vector2D(0, 100);
    private static final Vector2D DESERT_COORDS = new Vector2D(0, -100);
    private static final Vector2D JUNGLE_COORDS = new Vector2D(-100, -100);
    private static final Vector2D BADLANDS_COORDS = new Vector2D(-100, 0);
    private static final Vector2D SAVANNA_COORDS = new Vector2D(-100, 100);
    private static final Vector2D NETHER_COORDS = new Vector2D(100, 100);
    private static final Vector2D ICE_SPIKES_COORDS = new Vector2D(100, 0);
    private static final Vector2D END_COORDS = new Vector2D(100, -100);


    public static void setIslandInSection(int sectionIndex, World world, Player messageRecipient) {

        List<Supplier<CompletableFuture<Boolean>>> barrierTasks = new ArrayList<>();
        CompletableFuture<Boolean> islandFuture = new CompletableFuture<>();
        int startY = LOWEST;
        int endY = HIGHEST;

        switch (sectionIndex) {
            case 0: { // Plains
                islandFuture = pasteIsland(world, "island_plains.schem", new Vector2D(0, 0));
                break;
            }
            case 1: { // Taiga
                islandFuture = pasteIsland(world, "island_taiga.schem", TAIGA_COORDS);
                barrierTasks.add(() -> removeBarrier(new Vector(-50, startY, 51), new Vector(50, endY, 51), world));
                barrierTasks.add(() -> removeBarrier(new Vector(-50, startY, 600), new Vector(50, endY, 600), world));
                break;
            }
            case 2: { // Desert
                islandFuture = pasteIsland(world, "island_desert.schem", DESERT_COORDS);
                barrierTasks.add(() -> removeBarrier(new Vector(-50, startY, -51), new Vector(50, endY, -51), world));
                barrierTasks.add(() -> removeBarrier(new Vector(-50, startY, -600), new Vector(50, endY, -600), world));
                break;
            }
            case 3: { // Jungle
                islandFuture = pasteIsland(world, "island_jungle.schem", JUNGLE_COORDS);
                barrierTasks.add(() -> removeBarrier(new Vector(-51, startY, -599), new Vector(-51, endY, -52), world));
                barrierTasks.add(() -> removeBarrier(new Vector(-600, startY, -600), new Vector(-51, endY, -600), world));
                barrierTasks.add(() -> removeBarrier(new Vector(-600, startY, -599), new Vector(-600, endY, -52), world));
                break;
            }
            case 4: { // Badlands
                islandFuture = pasteIsland(world, "island_badlands.schem", BADLANDS_COORDS);
                barrierTasks.add(() -> removeBarrier(new Vector(-599, startY, -51), new Vector(-52, endY, -51), world));
                barrierTasks.add(() -> removeBarrier(new Vector(-51, startY, -51), new Vector(-51, endY, 50), world));
                barrierTasks.add(() -> removeBarrier(new Vector(-600, startY, -51), new Vector(-600, endY, 50), world));
                break;
            }
            case 5: { // Savanna
                islandFuture = pasteIsland(world, "island_savanna.schem", SAVANNA_COORDS);
                barrierTasks.add(() -> removeBarrier(new Vector(-599, startY, 51), new Vector(-52, endY, 51), world));
                barrierTasks.add(() -> removeBarrier(new Vector(-600, startY, 51), new Vector(-600, endY, 599), world));
                barrierTasks.add(() -> removeBarrier(new Vector(-600, startY, 600), new Vector(-52, endY, 600), world));
                barrierTasks.add(() -> removeBarrier(new Vector(-51, startY, 52), new Vector(-51, endY, 600), world));
                break;
            }
            case 6: { // Nether
                islandFuture = pasteIsland(world, "island_nether.schem", NETHER_COORDS);
                barrierTasks.add(() -> removeBarrier(new Vector(51, startY, 52), new Vector(51, endY, 599), world));
                barrierTasks.add(() -> removeBarrier(new Vector(51, startY, 600), new Vector(600, endY, 600), world));
                barrierTasks.add(() -> removeBarrier(new Vector(600, startY, 52), new Vector(600, endY, 599), world));
                break;
            }
            case 7: { // Ice Spikes
                islandFuture = pasteIsland(world, "island_ice_spikes.schem", ICE_SPIKES_COORDS);
                barrierTasks.add(() -> removeBarrier(new Vector(51, startY, -50), new Vector(51, endY, 50), world));
                barrierTasks.add(() -> removeBarrier(new Vector(51, startY, 51), new Vector(599, endY, 51), world));
                barrierTasks.add(() -> removeBarrier(new Vector(600, startY, -50), new Vector(600, endY, 51), world));
                break;
            }
            case 8: { // End
                islandFuture = pasteIsland(world, "island_end.schem", END_COORDS);
                barrierTasks.add(() -> removeBarrier(new Vector(51, startY, -600), new Vector(51, endY, -51), world));
                barrierTasks.add(() -> removeBarrier(new Vector(52, startY, -51), new Vector(599, endY, -51), world));
                barrierTasks.add(() -> removeBarrier(new Vector(600, startY, -599), new Vector(600, endY, -51), world));
                barrierTasks.add(() -> removeBarrier(new Vector(52, startY, -600), new Vector(600, endY, -600), world));
                break;
            }
            default:
                plugin.getLogger().warning("Error pasting schematic: Please specify a section number between 0 and 8");
        }

        CompletableFuture<Boolean> chainedTasks = islandFuture;
        for (Supplier<CompletableFuture<Boolean>> task : barrierTasks) {
            chainedTasks = chainedTasks.thenCompose(ignored -> task.get());
        }

        chainedTasks.whenComplete((ignored, throwable) -> {
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
        return CuboidOperation.performCuboidSectionOperationBatched(
                        plugin, start, end, 2500, (x, y, z) -> {
                            world.getBlockAt(x, y, z).setType(Material.AIR);
                        }
                )
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
        Vec3 islandLocation;

        switch (sectionIndex) {
            case 0: {
                islandName = "Plains";
                islandLocation = new Vec3(0, 65, 0);
                break;
            }
            case 1: {
                islandName = "Taiga";
                islandLocation = getIslandVec3(TAIGA_COORDS);
                break;
            }
            case 2: {
                islandName = "Desert";
                islandLocation = getIslandVec3(DESERT_COORDS);
                SkyblockSession session = Skblock.getSessionRegistry().getSession(world);
                session.unlockGenerator(GeneratorType.SAND);
                break;
            }
            case 3: {
                islandName = "Jungle";
                Location location = new Location(world, -100, 65, -100);
                Block block = location.getBlock();
                block.setType(Material.CHEST);
                Chest chest = (Chest) block.getState();

                Inventory inventory = chest.getInventory();
                inventory.setItem(3, new ItemStack(Material.STICK, 4));

                islandLocation = getIslandVec3(JUNGLE_COORDS);
                Skblock.getSessionRegistry().getSession(world).unlockGenerator(GeneratorType.GRASS_BLOCK);
                break;
            }
            case 4: {
                islandName = "Badlands";
                Location location = new Location(world, -102, 61, -3);
                StorageMinecart minecartChest = (StorageMinecart) world.spawnEntity(location, EntityType.CHEST_MINECART);

                Inventory minecartInventory = minecartChest.getInventory();
                minecartInventory.setItem(7, new ItemStack(Material.SHORT_GRASS, 3));
                islandLocation = getIslandVec3(BADLANDS_COORDS);
                Skblock.getSessionRegistry().getSession(world).unlockGenerator(GeneratorType.TERRACOTTA);
                break;
            }
            case 5: {
                islandName = "Savanna";
                Location location = new Location(world, -103, 67, 99);
                world.spawnEntity(location, EntityType.BEE);
                islandLocation = getIslandVec3(SAVANNA_COORDS);
                SkyblockSession session = Skblock.getSessionRegistry().getSession(world);
                session.unlockGenerator(GeneratorType.DEEPSLATE);
                List<ShopItem> unlockItems = List.of(ShopMenu.LAVA);
                session.getShopMenu().unlockItems(unlockItems);
                break;
            }

            case 6: {
                islandName = "Nether";
                islandLocation = getIslandVec3(NETHER_COORDS);
                Skblock.getSessionRegistry().getSession(world).unlockGenerator(GeneratorType.NETHERRACK);
                break;
            }
            case 7: {
                islandName = "Ice Spikes";
                Location zombieVillagerLocation = new Location(world, 96.5, 57.0, 0.5);
                Location villagerLocation = new Location(world, 98.5, 57.0, 0.5);

                ZombieVillager zombieVillager = (ZombieVillager) world.spawnEntity(zombieVillagerLocation, EntityType.ZOMBIE_VILLAGER);
                zombieVillager.setVillagerProfession(Villager.Profession.CLERIC);
                zombieVillager.setRemoveWhenFarAway(false);

                world.spawnEntity(villagerLocation, EntityType.VILLAGER);
                islandLocation = getIslandVec3(ICE_SPIKES_COORDS);
                break;
            }
            case 8: {
                islandName = "The End";
                islandLocation = getIslandVec3(END_COORDS);
                Skblock.getSessionRegistry().getSession(world).unlockGenerator(GeneratorType.END_STONE);
                break;
            }
            default: {
                islandName = "";
                islandLocation = Vec3.ZERO;
            }
        }

        messageRecipient.playSound(messageRecipient.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
        messageRecipient.sendMessage("§aA new island has appeared: " + islandName + "!");

        ((CraftPlayer) messageRecipient).getHandle().lookAt(EntityAnchorArgument.Anchor.FEET, islandLocation);
    }

//    private static void cleanLight(int chunkX, int chunkZ, int radius, String worldName) {
//        plugin.getLogger().info("Cleaning light at chunk " + chunkX + ", " + chunkZ + " in world " + worldName);
//        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cleanlight at " + chunkX + " " + chunkZ + " " + radius + " " + worldName);
//    }

    private static Vec3 getIslandVec3(Vector2D islandCoords) {
        return new Vec3(islandCoords.x(), ISLAND_HEIGHT, islandCoords.y());
    }

}
