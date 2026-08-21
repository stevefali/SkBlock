package com.steve.skblock.worlds;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WorldDeleter {

    public static void deleteWorld(String worldNameSuffix, Player player,  Plugin plugin) {
        SkyblockWorldFactory.deleteWorld(worldNameSuffix, plugin)
                .exceptionally(throwable -> {
                    player.sendMessage("§c" + throwable.getMessage());
                    return  null;
                })
                .thenApply(deletionResult -> {
                    if (deletionResult.success()) {
                        player.sendMessage("§a" + deletionResult.message());
                    } else {
                        player.sendMessage("§c" + deletionResult.message());
                    }
                    return deletionResult;
                });
    }

}
