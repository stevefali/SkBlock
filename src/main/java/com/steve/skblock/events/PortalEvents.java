package com.steve.skblock.events;

import com.steve.skblock.util.TitlesUtils;
import com.steve.skblock.worlds.SkyblockWorldFactory;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PortalEvents implements Listener {


    private Server server;
    Plugin plugin;

    public PortalEvents(Plugin plugin) {
        this.plugin = plugin;
        this.server = plugin.getServer();
    }

    private final long COOLDOWN = 7000;
    private Map<UUID, Long> playerCooldowns = new HashMap<>();


    @EventHandler
    public void onPortalEnter(EntityPortalEnterEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            if (event.getLocation().getWorld().getName().equals("skyblock_lobby") && event.getLocation().getBlockZ() == 15) {
                if (checkIsPlayerCooled(player.getUniqueId())) {

                    TitlesUtils.sendSubtitle(player, "§9Preparing your island...", 5, 30, 5);

                    setPlayerCooldown(player.getUniqueId());
                }
            }
        }
    }


    @EventHandler
    public void onPortalTeleport(PlayerTeleportEvent event) {
        try {
            if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
                World currentWorld = event.getPlayer().getWorld();
                Player player = event.getPlayer();

                event.setCancelled(true);

                if (!currentWorld.getName().equals("skyblock_lobby")) {
                    World lobbyWorld = Bukkit.getWorld("skyblock_lobby");
                    Location lobbySpawn = new Location(lobbyWorld, 0, 65, 0);
                    player.teleport(lobbySpawn);
                } else {
                    String playerId = player.getUniqueId().toString();
                    SkyblockWorldFactory.loadPlayerWorld(playerId, plugin)
                            .exceptionally(throwable -> {
                                player.sendMessage("§eThere was an error loading your world");
                                throwable.printStackTrace();
                                return null;
                            })
                            .thenApply(playerWorldName -> {
                                World skyblockWorld = Bukkit.getWorld(playerWorldName);
                                Location spawnLocation = skyblockWorld.getSpawnLocation();
                                player.teleport(spawnLocation);
                                TitlesUtils.sendSubtitle(player, "", 5, 10, 5);
                                TitlesUtils.sendTitle(player, "§aWelcome to your Island!", 7, 30, 7);
                                return playerWorldName;
                            });
                }

            } else if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL
                    || event.getCause() == PlayerTeleportEvent.TeleportCause.END_GATEWAY) {
                event.setCancelled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.setCancelled(true);
        }
    }

    private boolean checkIsPlayerCooled(UUID playerUuid) {
        if (!playerCooldowns.containsKey(playerUuid)) {
            return true;
        }
        return System.currentTimeMillis() > playerCooldowns.get(playerUuid);
    }

    private void setPlayerCooldown(UUID playerUuid) {
        long playerCooldown = System.currentTimeMillis() + COOLDOWN;
        playerCooldowns.put(playerUuid, playerCooldown);
    }


}
