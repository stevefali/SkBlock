package com.steve.skblock.game.data;

import java.util.UUID;

public class SqlSkyblockDataStore  implements SkyblockDataStore{

    // TODO: Implement this instead of NbtSkyblockDataStore when integrating database.


    @Override
    public int loadSkyblockScore(UUID playerId) {
        return 0;
    }

    @Override
    public void saveSkyblockScore(UUID playerId, int skyblockScore) {

    }

}
