package com.steve.skblock.events.routed;

import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

public class FurnaceEvent implements Listener {

    private final SessionRegistry sessionRegistry;

    public FurnaceEvent(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @EventHandler
    public void onSmelt(FurnaceSmeltEvent event) {
        SkyblockSession skyblockSession = sessionRegistry.getSession(event.getBlock().getWorld());
        if (skyblockSession != null) {
            skyblockSession.onItemSmelt(event);
        }
    }

}
