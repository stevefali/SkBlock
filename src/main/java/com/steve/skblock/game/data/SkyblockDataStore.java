package com.steve.skblock.game.data;

import com.steve.skblock.game.SkyblockProgress;

import java.util.UUID;

public interface SkyblockDataStore {

    int loadSkyblockScore(UUID playerId);
    void saveSkyblockScore(UUID playerId, int skyblockScore);

    SkyblockProgress loadProgress(UUID playerId);
    void saveProgress(UUID playerId, SkyblockProgress skyblockProgress);


}
