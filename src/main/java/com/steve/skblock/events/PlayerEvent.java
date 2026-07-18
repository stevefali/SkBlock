package com.steve.skblock.events;

import com.steve.skblock.util.TitlesUtils;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerEvent implements Listener {

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {

        TitlesUtils.sendTitle(event.getPlayer(), "§6Welcome to Skyblock", 7, 40, 7);

    }

    @EventHandler
    public void onInteractWithEntity(PlayerInteractEntityEvent event) {
        if (((CraftEntity) event.getRightClicked()).getCustomName().equals("§aRandy")) {
            event.getPlayer().sendMessage("§6 That worked");
        }
    }


}
