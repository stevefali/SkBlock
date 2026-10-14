package com.steve.skblock.game;

import com.steve.skblock.Skblock;
import com.steve.skblock.game.data.SkyblockDataStore;
import com.steve.skblock.sidebar.SkyblockSidebar;
import com.steve.skblock.util.NameFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionRegistry {

    private final SkyblockDataStore skyblockDataStore;
    private final Map<UUID, SkyblockSession> sessions = new ConcurrentHashMap<>();
    private BukkitTask autoSaveTask;
    private BukkitTask biomeTask;

    private final Map<UUID, NamespacedKey> playerBiomes = new HashMap<>();

    private static final long ONE_MINUTE = 20L * 60;

    public SessionRegistry(SkyblockDataStore dataStore) {
        this.skyblockDataStore = dataStore;
    }

    public SkyblockSession createSession(World world) {
        if (sessions.get(world.getUID()) != null) {
            return sessions.get(world.getUID());
        }

        SkyblockSession session = new SkyblockSession(world, skyblockDataStore);
        sessions.put(world.getUID(), session);
        return session;
    }

    public void unregister(World world) {
        sessions.remove(world.getUID());
    }

    public SkyblockSession getSession(World world) {
        return sessions.get(world.getUID());
    }

    public void unregisterAll() {
        sessions.clear();
    }

    public void scheduleAutoSaves(Plugin plugin) {
        // TODO: Switch to 'runTaskTimerAsynchronously' when integrating database
        this.autoSaveTask = Bukkit.getScheduler().runTaskTimer(
                plugin, () -> {
                    for (SkyblockSession skyblockSession : sessions.values()) {
                        skyblockSession.saveDirty();
                    }

                    // TODO: Remove this log?
//            plugin.getLogger().info("Autosaved " + sessions.size() + " Skyblock sessions");

                }, ONE_MINUTE * 5, ONE_MINUTE * 5
        );
    }

    public void stopAutoSaves() {
        if (autoSaveTask != null) {
            autoSaveTask.cancel();
        }
    }

    public void startBiomeTask() {
        this.biomeTask = Bukkit.getScheduler().runTaskTimer(
                JavaPlugin.getPlugin(Skblock.class), () -> {
                    for (SkyblockSession session : sessions.values()) {
                        World world = session.getWorld();
                        if (world == null) {
                            continue;
                        }

                        for (Player player : world.getPlayers()) {
                            Location location = player.getLocation();
                            NamespacedKey playerBiomeKey = world.getBiome(location).getKey();

                            if (!playerBiomeKey.equals(playerBiomes.put(
                                    player.getUniqueId(),
                                    playerBiomeKey
                            ))) {
                                SkyblockSidebar.setIslandLine(player.getUniqueId(), NameFormatter.format(playerBiomeKey.getKey()));
//                                GeneratorType generatorType = BlockGenerators.GENERATOR_BIOMES.getOrDefault(playerBiomeKey, GeneratorType.DEFAULT);
//                                if (!session.isGeneratorUnlocked(generatorType)) {
//                                    generatorType = GeneratorType.DEFAULT;
//                                }
//                                SkyblockSidebar.updateIslandLines(
//                                        player.getUniqueId(),
//                                        NameFormatter.format(playerBiomeKey.getKey()),
//                                        BlockGenerators.GENERATORS.get(generatorType).getPercentages()
//                                );
                            }
                        }
                    }
                }, 30L, 30L
        );
    }

    public void stopBiomeTask() {
        if (biomeTask != null) {
            biomeTask.cancel();
        }
    }

}
