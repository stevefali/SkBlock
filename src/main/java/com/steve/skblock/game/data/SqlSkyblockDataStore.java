package com.steve.skblock.game.data;

import com.steve.skblock.game.SkyblockProgress;

import java.util.UUID;

public class SqlSkyblockDataStore implements SkyblockDataStore {

    // TODO: Implement this instead of NbtSkyblockDataStore when integrating database.


    @Override
    public int loadSkyblockScore(UUID playerId) {
        return 0;
    }

    @Override
    public void saveSkyblockScore(UUID playerId, int skyblockScore) {

    }

    @Override
    public SkyblockProgress loadProgress(UUID playerId) {
        return null;
    }

    @Override
    public void saveProgress(UUID playerId, SkyblockProgress skyblockProgress) {

    }

}
