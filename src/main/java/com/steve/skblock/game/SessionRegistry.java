package com.steve.skblock.game;

import com.steve.skblock.game.data.SkyblockDataStore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionRegistry {

    private final SkyblockDataStore skyblockDataStore;
    private final Map<UUID, SkyblockSession> sessions = new ConcurrentHashMap<>();
    private BukkitTask autoSaveTask;

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
        this.autoSaveTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (SkyblockSession skyblockSession : sessions.values()) {
                skyblockSession.saveDirty();
            }

            // TODO: Remove this log?
//            plugin.getLogger().info("Autosaved " + sessions.size() + " Skyblock sessions");

        }, ONE_MINUTE * 5, ONE_MINUTE * 5);
    }

    public void stopAutoSaves() {
        if (autoSaveTask != null) {
            autoSaveTask.cancel();
        }
    }


}
