package com.steve.skblock.game.data;

import com.google.gson.Gson;
import com.steve.skblock.game.SkyblockProgress;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

import static com.steve.skblock.worlds.SkyblockWorldFactory.WORLD_NAME_PREFIX;

public class NbtSkyblockDataStore implements SkyblockDataStore {

    private static final Gson GSON = new Gson();

    @Override
    public int loadSkyblockScore(UUID playerId) {
        World world = getWorldByOwner(playerId);
        return world.getPersistentDataContainer().getOrDefault(DataKeys.SKYBLOCK_SCORE, PersistentDataType.INTEGER, 0);
    }

    @Override
    public void saveSkyblockScore(UUID playerId, int skyblockScore) {
        World world = getWorldByOwner(playerId);
        world.getPersistentDataContainer().set(DataKeys.SKYBLOCK_SCORE, PersistentDataType.INTEGER, skyblockScore);
    }

    @Override
    public SkyblockProgress loadProgress(UUID playerId) {
        World world = getWorldByOwner(playerId);
        String jsonString = world.getPersistentDataContainer().get(DataKeys.PROGRESS_JSON, PersistentDataType.STRING);
        if (jsonString != null) {
            return GSON.fromJson(jsonString, SkyblockProgress.class);
        }
        // TODO: Implement starting Quest ID!!!!!!!!!!!!!!!!!!
        return new SkyblockProgress("");
    }

    @Override
    public void saveProgress(UUID playerId, SkyblockProgress skyblockProgress) {
        World world = getWorldByOwner(playerId);
        world.getPersistentDataContainer().set(
                DataKeys.PROGRESS_JSON,
                PersistentDataType.STRING,
                GSON.toJson(skyblockProgress)
        );
    }


    private World getWorldByOwner(UUID playerId) {
        World world = Bukkit.getWorld(WORLD_NAME_PREFIX + playerId.toString());
        if (world == null) {
            throw new NullPointerException("World  with name " + WORLD_NAME_PREFIX + playerId.toString() + " is null");
        }
        return world;
    }
}
