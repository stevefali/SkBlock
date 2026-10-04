package com.steve.skblock.worldoperation;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.concurrent.CompletableFuture;

public class CuboidOperation {

    public interface BlockOperation {
        void run(int x, int y, int z);
    }

    /**
     * Perform a block operation on each block in the cuboid region. Starts in the lowest numerical position and works is way up.
     *
     * @param start     The lowest(x,y,z) corner of the region.
     * @param sizeX     The x-size of the region.
     * @param sizeY     The height of the region.
     * @param sizeZ     The Z-size of the region
     * @param operation The operation to be performed on each block
     */
    public static void performCuboidSectionOperation(Vector start, int sizeX, int sizeY, int sizeZ, BlockOperation operation) {
        Vector finish = new Vector(start.getBlockX() + sizeX, start.getBlockY() + sizeY, start.getBlockZ() + sizeZ);
        performCuboidSectionOperation(start, finish, operation);
    }

    /**
     * Perform a block operation on each block in the cuboid region. Starts in the lowest numerical position and works is way up.
     *
     * @param start     The lowest(x,y,z) corner of the region.
     * @param finish    The highest(x,y,z) corner of the region.
     * @param operation The operation to be performed on each block
     */
    public static void performCuboidSectionOperation(Vector start, Vector finish, BlockOperation operation) {
        try {
            for (int x = start.getBlockX(); x <= finish.getBlockX(); x++) {
                for (int z = start.getBlockZ(); z <= finish.getBlockZ(); z++) {
                    for (int y = start.getBlockY(); y <= finish.getBlockY(); y++) {
                        operation.run(x, y, z);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Perform a block operation on each block in the cuboid region. Starts in the lowest numerical position and works is way up.
     *
     * @param plugin    The plugin object.
     * @param start     The lowest(x,y,z) corner of the region.
     * @param finish    The highest(x,y,z) corner of the region.
     * @param operation The operation to be performed on each block
     * @return A completable future boolean
     */
    public static CompletableFuture<Boolean> performCuboidSectionOperationAsync(Plugin plugin, Vector start, Vector finish, BlockOperation operation) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(
                plugin, () -> {
                    try {
                        for (int x = start.getBlockX(); x <= finish.getBlockX(); x++) {
                            for (int z = start.getBlockZ(); z <= finish.getBlockZ(); z++) {
                                for (int y = start.getBlockY(); y <= finish.getBlockY(); y++) {
                                    operation.run(x, y, z);
                                }
                            }
                        }
                        future.complete(true);
                    } catch (Exception e) {
                        future.completeExceptionally(new WorldOperationException("Error performing cuboid section operation: " + e.getMessage()));
                        System.out.println(e.getMessage());
                    }
                }
        );
        return future;
    }

    /**
     * Perform a block operation on each block in the cuboid region. Starts in the lowest numerical position and works is way up.
     * **This method uses its own EditSession, so don't wrap it in another one**
     *
     * @param plugin    The plugin object.
     * @param start     The lowest(x,y,z) corner of the region.
     * @param finish    The highest(x,y,z) corner of the region.
     * @param world     The world to perform the operation in.
     * @param blockType The block type to set in the region.
     * @return A completable future boolean
     */
    public static CompletableFuture<Boolean> performCuboidSectionOperationAsync(Plugin plugin, Vector start, Vector finish, World world, BlockType blockType) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(
                plugin, () -> {
                    try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                            .world(BukkitAdapter.adapt(world))
                            .build()) {

                        for (int x = start.getBlockX(); x <= finish.getBlockX(); x++) {
                            for (int z = start.getBlockZ(); z <= finish.getBlockZ(); z++) {
                                for (int y = start.getBlockY(); y <= finish.getBlockY(); y++) {
                                    editSession.setBlock(new BlockVector3(x, y, z), (Pattern) blockType);
                                }
                            }
                        }
                        editSession.close();
                        future.complete(true);
                    } catch (Exception e) {
                        future.completeExceptionally(new WorldOperationException("Error performing cuboid section operation: " + e.getMessage()));
                        System.out.println(e.getMessage());
                    }
                }
        );
        return future;
    }

    /**
     * Perform a block operation in batches on each block in the cuboid region. A "batch" represents the maximum number of blocks to change per tick.
     * Starts in the lowest numerical position and works is way up.
     *
     * @param plugin    The plugin object.
     * @param start     The lowest(x,y,z) corner of the region.
     * @param finish    The highest(x,y,z) corner of the region.
     * @param batchSize The number of blocks to change per tick
     * @param operation The operation to be performed on each block
     * @return A completable future boolean
     */
    public static CompletableFuture<Boolean> performCuboidSectionOperationBatched(
            Plugin plugin,
            Vector start,
            Vector finish,
            int batchSize,
            BlockOperation operation) {

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        int minX = Math.min(start.getBlockX(), finish.getBlockX());
        int maxX = Math.max(start.getBlockX(), finish.getBlockX());

        int minY = Math.min(start.getBlockY(), finish.getBlockY());
        int maxY = Math.max(start.getBlockY(), finish.getBlockY());

        int minZ = Math.min(start.getBlockZ(), finish.getBlockZ());
        int maxZ = Math.max(start.getBlockZ(), finish.getBlockZ());

        new BukkitRunnable() {
            int x = minX;
            int y = minY;
            int z = minZ;

            @Override
            public void run() {
                try {
                    int doneBlocks = 0;
                    while (doneBlocks < batchSize && x <= maxX) {
                        operation.run(x, y, z);
                        doneBlocks++;

                        if (++y > maxY) {
                            y = minY;
                            if (++z > maxZ) {
                                z = minZ;
                                x++;
                            }
                        }
                    }

                    if (x > maxX) {
                        future.complete(true);
                        cancel();
                    }

                } catch (Exception e) {
                    future.completeExceptionally(new WorldOperationException("Error performing cuboid section operation: " + e.getMessage()));
                    System.out.println(e.getMessage());
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);

        return future;
    }

}
