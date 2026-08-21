package com.steve.skblock.util.teleport;

import com.steve.skblock.npc.NpcFactory;
import com.steve.skblock.util.TitlesUtils;
import com.steve.skblock.worlds.SkyblockWorldFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SkyblockWorldTeleport {

    public static void sendPlayerToSkyblockWorld(Player player, Plugin plugin) {
        String playerId = player.getUniqueId().toString();
        SkyblockWorldFactory.loadPlayerWorld(playerId, plugin)
                .exceptionally(throwable -> {
                    player.sendMessage("§eThere was an error loading your world");
                    throwable.printStackTrace();
                    return null;
                })
                .thenApply(playerWorldName -> {
                    World skyblockWorld = Bukkit.getWorld(playerWorldName);
                    NpcFactory.createNpcs(skyblockWorld, plugin);
                    Location spawnLocation = skyblockWorld.getSpawnLocation();
                    player.teleport(spawnLocation);
                    TitlesUtils.sendSubtitle(player, "§aWelcome to your Island!", 7, 30, 7);
                    return playerWorldName;
                });
    }
}
