package com.steve.skblock.events;

import com.steve.skblock.util.TitlesUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerEvent implements Listener {

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {

        TitlesUtils.sendTitle(event.getPlayer(), "§6Welcome to Skyblock", 7, 40, 7);

    }


}
