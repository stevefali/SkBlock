package com.steve.skblock.events.routed;

import com.steve.MegaNPCs.api.NpcInteractionEvent;
import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NpcEvent implements Listener {

    private final SessionRegistry sessionRegistry;

    public NpcEvent(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }


    @EventHandler(ignoreCancelled = true)
    public void onNpcInteract(NpcInteractionEvent event) {
        SkyblockSession skyblockSession = sessionRegistry.getSession(event.getPlayer().getWorld());
        if (skyblockSession != null) {
            skyblockSession.onNpcInteract(event);
            return;
        }
    }

}
