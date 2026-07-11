package com.steve.skblock.worlds;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.storage.LevelDataAndDimensions;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.plugin.Plugin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class SkyblockWorldFactory {

    private static final String TEMPLATE_WORLD_PATH = "../skyblockWorldMasterFinal";
    private static final String SKYBLOCK_WORLDS_PATH = "./SkyblockWorlds";
    private static final String WORLD_NAME_PREFIX = "skyblock_";
    private static final String BACKUP_WORLDS_FOLDER = "./BackupWorlds";



    public static CompletableFuture<String> loadPlayerWorld(String worldNameSuffix, Plugin plugin) {
        CompletableFuture<String> future = new CompletableFuture<>();

        String skyblockWorldName = WORLD_NAME_PREFIX + worldNameSuffix;

        if (Bukkit.getWorld(skyblockWorldName) != null) {
            future.complete(skyblockWorldName);
            return future;
        }
        try {
            getOrCreateWorldPath(worldNameSuffix, plugin)
                    .exceptionally(throwable -> {
                        throw new CompletionException(throwable);
                    })
                    .thenAccept(path -> {
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            try {
                                MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();

                                LevelStorageSource source = LevelStorageSource.createDefault(Paths.get(SKYBLOCK_WORLDS_PATH));
                                LevelStorageSource.LevelStorageAccess access = source.createAccess(skyblockWorldName, LevelStem.OVERWORLD);
                                Dynamic<?> dataTag = access.getDataTag();
                                WorldLoader.DataLoadContext dataLoadContext = minecraftServer.worldLoader;
                                RegistryAccess.Frozen registryAccess = dataLoadContext.datapackDimensions();

                                Registry<LevelStem> stemRegistry = registryAccess.lookupOrThrow(Registries.LEVEL_STEM);

                                LevelDataAndDimensions levelDataAndDimensions = LevelStorageSource.getLevelDataAndDimensions(dataTag, dataLoadContext.dataConfiguration(), stemRegistry, dataLoadContext.datapackWorldgen());
                                PrimaryLevelData levelData = (PrimaryLevelData) levelDataAndDimensions.worldData();

                                Registry<DimensionType> dimensionTypeRegistry = minecraftServer.registryAccess().lookupOrThrow(Registries.DIMENSION_TYPE);
                                Holder<DimensionType> overworldTypeHolder = dimensionTypeRegistry.getOrThrow(BuiltinDimensionTypes.OVERWORLD);

                                ResourceKey<Level> skyblockKey = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("mega", skyblockWorldName));

                                Registry<Biome> biomeRegistry = minecraftServer.registryAccess().lookupOrThrow(Registries.BIOME);
                                Holder<Biome> defaultBiome = biomeRegistry.getOrThrow(Biomes.THE_VOID);

                                FlatLevelGeneratorSettings generatorSettings = new FlatLevelGeneratorSettings(
                                        Optional.empty(),
                                        defaultBiome,
                                        List.of()
                                );
                                generatorSettings = generatorSettings.withBiomeAndLayers(
                                        List.of(),
                                        Optional.empty(),
                                        defaultBiome
                                );
                                generatorSettings.updateLayers();
                                ChunkGenerator chunkGenerator = new FlatLevelSource(generatorSettings);

                                LevelStem skyblockStem = new LevelStem(overworldTypeHolder, chunkGenerator);


                                ServerLevel level = new ServerLevel(
                                        minecraftServer,
                                        Util.backgroundExecutor(),
                                        access,
                                        levelData,
                                        skyblockKey,
                                        skyblockStem,
                                        minecraftServer.progressListenerFactory.create(minecraftServer.getGameRules().getInt(GameRules.RULE_SPAWN_CHUNK_RADIUS)),
                                        levelData.isDebugWorld(),
                                        BiomeManager.obfuscateSeed(levelData.worldGenOptions().seed()),
                                        ImmutableList.of(),
                                        true,
                                        null,
                                        World.Environment.NORMAL,
                                        null,
                                        null
                                );

                                minecraftServer.addLevel(level);

                            } catch (Exception e) {
                                throw new CompletionException(e);
                            }
                            future.complete(skyblockWorldName);
                        });

                    });

        } catch (Exception e) {
            future.complete(null);
        }

        return future;
    }

    public static CompletableFuture<Path> getOrCreateWorldPath(String worldNameSuffix, Plugin plugin) {

        CompletableFuture<Path> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Path templatePath = Paths.get(TEMPLATE_WORLD_PATH);
                Path worldPath = Paths.get(SKYBLOCK_WORLDS_PATH, WORLD_NAME_PREFIX + worldNameSuffix);

                if (!Files.exists(worldPath)) {
                    Files.createDirectories(worldPath);
                    Files.walk(templatePath).forEach(src -> {
                        try {
                            Path targetFile = worldPath.resolve(templatePath.relativize(src));
                            if (Files.isDirectory(src)) {
                                Files.createDirectories(targetFile);
                            } else {
                                Files.copy(src, targetFile, StandardCopyOption.COPY_ATTRIBUTES);
                            }
                        } catch (Exception e) {
                            throw new CompletionException(e);
                        }
                    });

                    try {
                        Path levelDatPath = Paths.get(worldPath.toString(), "level.dat");
                        CompoundTag levelDatTag = NbtIo.readCompressed(levelDatPath, NbtAccounter.unlimitedHeap());
                        CompoundTag dataTag = levelDatTag.getCompound("Data");
                        dataTag.putString("LevelName", WORLD_NAME_PREFIX + worldNameSuffix);
                        NbtIo.writeCompressed(levelDatTag, levelDatPath);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                }
                future.complete(worldPath);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }


    public static CompletableFuture<DeletionResult> deleteWorld(String worldNameSuffix, Plugin plugin) {
        CompletableFuture<DeletionResult> future = new CompletableFuture<>();

        Bukkit.unloadWorld(WORLD_NAME_PREFIX + worldNameSuffix, false);

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            try {

                Path worldPath = Paths.get(SKYBLOCK_WORLDS_PATH, WORLD_NAME_PREFIX + worldNameSuffix);

                if (!Files.exists(worldPath)) {
                    future.complete(new DeletionResult(true, "World not found"));
                } else {
                    Path backupFolderPath = Paths.get(BACKUP_WORLDS_FOLDER);

                    Path backupWorldPath = backupFolderPath.resolve(worldPath.getFileName());
                    Files.createDirectories(backupWorldPath);

                    Files.walk(worldPath).forEach(src -> {
                        try {
                            Path backupFile = backupWorldPath.resolve(worldPath.relativize(src));
                            if (Files.isDirectory(src)) {
                                Files.createDirectories(backupFile);
                            } else {
                                Files.copy(src, backupFile, StandardCopyOption.REPLACE_EXISTING);
                            }
                        } catch (Exception e) {
                            throw new CompletionException(e);
                        }
                    });

                    Files.walk(worldPath).sorted(Comparator.reverseOrder())
                            .forEach(path -> {
                                try {
                                    Files.delete(path);
                                } catch (Exception e) {
                                    throw new CompletionException(e);
                                }
                            });

                    if (Files.exists(worldPath)) {
                        future.complete(new DeletionResult(false, "Failed to delete world files"));
                    } else {
                        future.complete(new DeletionResult(true, "World files deleted"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                future.completeExceptionally(e);
            }
        }, 1L);

        return future;
    }


    public record DeletionResult(boolean success, String message) {
    }

}
