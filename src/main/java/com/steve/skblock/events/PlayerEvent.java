package com.steve.skblock.events;

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
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;



public class PlayerEvent implements Listener {

    private Plugin plugin;

    private static final String SKYBLOCK_LOBBY_NAME = "skyblock_lobby";
    private static final String WORLD_NAME_PREFIX = "skyblock_";
    private final Location lobbySpawn;

    public PlayerEvent(Plugin plugin, Location lobbySpawn) {
        this.plugin = plugin;
        this.lobbySpawn = lobbySpawn;
    }


    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (!player.getWorld().getName().equals(SKYBLOCK_LOBBY_NAME)) {
            player.teleport(lobbySpawn);
        }

        NpcFactory.showNPCs(player.getWorld().getName(), player);

        TitlesUtils.sendTitle(player, "§6Welcome to Skyblock", 7, 40, 7);
    }

    @EventHandler
    public void onInteractWithEntity(PlayerInteractEntityEvent event) {
//        Entity entity = event.getRightClicked();
//        if (entity instanceof Player player) {
//            if (Objects.equals(((CraftEntity) player).getCustomName(), "§aRandy")) {
//                event.getPlayer().sendMessage("§6 That worked");
//            }
//        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        String worldName = WORLD_NAME_PREFIX + event.getPlayer().getUniqueId();
        removeWorldNpcs(worldName);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (Bukkit.getWorld(worldName) != null) {
                if (Bukkit.getWorld(worldName).getPlayers().isEmpty()) {
                    Bukkit.unloadWorld(worldName, true);
                }
            }
        }, 20L);
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World toWorld = event.getPlayer().getWorld();

        if (toWorld.getName().equals(SKYBLOCK_LOBBY_NAME)) {
            player.teleport(lobbySpawn);
        }

        NpcFactory.showNPCs(toWorld.getName(), player);
    }

    private void removeWorldNpcs(String worldName) {
        Skblock.getNpcService().removeAllNpcsInWorld(worldName);
        Skblock.getNpcIds().remove(worldName);
    }


}
