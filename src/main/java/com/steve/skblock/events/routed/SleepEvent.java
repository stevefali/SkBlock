package com.steve.skblock.events.routed;

import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;

public class SleepEvent implements Listener {

    private final SessionRegistry sessionRegistry;

    public SleepEvent(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }


    @EventHandler
    public void onPlayerSleep(TimeSkipEvent event) {
        if (event.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP) {
            SkyblockSession skyblockSession = sessionRegistry.getSession(event.getWorld());
            if (skyblockSession != null) {
                skyblockSession.onPlayerSleep(event);
            }
        }
    }

}
