package com.steve.skblock.events;

import com.steve.skblock.network.PacketListenerInjector;
import com.steve.skblock.util.TitlesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.Objects;


public class PlayerEvent implements Listener {

    private Plugin plugin;

    public PlayerEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        PacketListenerInjector.inject(player, plugin);

        if (!player.getWorld().getName().equals("skyblock_lobby")) {
            World lobbyWorld = Bukkit.getWorld("skyblock_lobby");
            Location lobbySpawn = new Location(lobbyWorld, 0, 65, 0);
            player.teleport(lobbySpawn);
        }

        TitlesUtils.sendTitle(player, "§6Welcome to Skyblock", 7, 40, 7);

    }

    @EventHandler
    public void onInteractWithEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity instanceof Player player) {
            if (Objects.equals(((CraftEntity) player).getCustomName(), "§aRandy")) {
                event.getPlayer().sendMessage("§6 That worked");
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PacketListenerInjector.eject(event.getPlayer());
    }

    @EventHandler
    public void onJoinWorld(PlayerChangedWorldEvent event) {
        System.out.println("Player joined world: " + event.getPlayer().getWorld().getName());
    }


}
