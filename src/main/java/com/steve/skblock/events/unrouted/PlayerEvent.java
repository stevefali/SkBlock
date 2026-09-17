package com.steve.skblock.events.unrouted;

import com.steve.skblock.Skblock;
import com.steve.skblock.npc.NpcFactory;
import com.steve.skblock.util.TitlesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

import static com.steve.skblock.Skblock.SKYBLOCK_LOBBY_NAME;


public class PlayerEvent implements Listener {

    private Plugin plugin;

    private static final String WORLD_NAME_PREFIX = "skyblock_";
    private final Location lobbySpawn;

    public PlayerEvent(Plugin plugin, Location lobbySpawn) {
        this.plugin = plugin;
        this.lobbySpawn = lobbySpawn;
    }


    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        player.teleport(lobbySpawn);

        NpcFactory.showNPCs(player.getWorld().getName(), player);

        TitlesUtils.sendTitle(player, "§6Welcome to Skyblock", 7, 40, 7);

        Skblock.showSidebar(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();


        String worldName = WORLD_NAME_PREFIX + event.getPlayer().getUniqueId();
        removeWorldNpcs(worldName);
        Bukkit.getScheduler().runTaskLater(
                plugin, () -> {
                    World world = Bukkit.getWorld(worldName);
                    if (world != null) {
                        if (world.getPlayers().isEmpty()
                                && Bukkit.getPlayer(playerId) == null
                        ) {
                            Skblock.getSessionRegistry().getSession(world).saveAll();
                            Bukkit.unloadWorld(worldName, true);
                        }
                    }
                }, 20L * 30
        );
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World toWorld = event.getPlayer().getWorld();

        if (toWorld.getName().equals(SKYBLOCK_LOBBY_NAME)) {
            player.teleport(lobbySpawn);
            TitlesUtils.sendTitleAndSubtitle(player, "§6Skyblock Lobby", "", 7, 40, 7);
        }

        NpcFactory.showNPCs(toWorld.getName(), player);
    }

    private void removeWorldNpcs(String worldName) {
        Skblock.getNpcService().removeAllNpcsInWorld(worldName);
        Skblock.getNpcIds().remove(worldName);
        Skblock.getNpcService().removeOrphansFromWorld(worldName);
    }


}
