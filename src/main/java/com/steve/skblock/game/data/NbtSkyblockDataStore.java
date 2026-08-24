package com.steve.skblock.game.data;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

import static com.steve.skblock.worlds.SkyblockWorldFactory.WORLD_NAME_PREFIX;

public class NbtSkyblockDataStore implements SkyblockDataStore {

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

    private World getWorldByOwner(UUID playerId) {
        World world = Bukkit.getWorld( WORLD_NAME_PREFIX + playerId.toString());
        if (world == null) {
            throw new NullPointerException("World  with name " + WORLD_NAME_PREFIX + playerId.toString() + " is null");
        }
        return world;
    }
}
