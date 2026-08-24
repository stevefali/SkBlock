package com.steve.skblock.game.data;

import java.util.UUID;

public interface SkyblockDataStore {

    int loadSkyblockScore(UUID playerId);
    void saveSkyblockScore(UUID playerId, int skyblockScore);


}
