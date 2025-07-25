package com.steve.skblock.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvent implements Listener {

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage("Yo muthafukka!!");
    }
}
