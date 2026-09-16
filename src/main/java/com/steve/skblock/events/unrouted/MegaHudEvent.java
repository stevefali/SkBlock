package com.steve.skblock.events.unrouted;

import com.steve.MegaHUD.api.MegaHudServiceReadyEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MegaHudEvent implements Listener {

    @EventHandler
    public void onMegaHudReady(MegaHudServiceReadyEvent event) {
        System.out.println("MegaHUD ready for Skyblock!");
    }

}
