package com.steve.skblock.events.routed;

import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

public class TreeGrowEvent implements Listener {

    private final SessionRegistry sessionRegistry;

    public TreeGrowEvent(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }


    @EventHandler(ignoreCancelled = true)
    public void onTreeGrow(StructureGrowEvent event) {
        SkyblockSession skyblockSession = sessionRegistry.getSession(event.getWorld());
        if (skyblockSession != null) {
            skyblockSession.onTreeGrow(event);
            return;
        }
    }

}
