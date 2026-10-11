package com.steve.skblock.events.routed;


import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobSpawnEvent implements Listener {

    private final SessionRegistry sessionRegistry;


    public MobSpawnEvent(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {

        SkyblockSession skyblockSession = sessionRegistry.getSession(event.getEntity().getWorld());
        if (skyblockSession != null) {
            skyblockSession.onCreatureSpawn(event);
            return;
        }

    }

}
