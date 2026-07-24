package com.steve.skblock.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldEvent implements Listener {

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        System.out.println("World unload event: " + event.getWorld().getName());
    }
}
