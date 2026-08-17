package com.steve.skblock.events;

import com.steve.skblock.Skblock;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldEvent implements Listener {


    @EventHandler
    public void onWorldLoaded(WorldLoadEvent event) {
        World world = event.getWorld();

        if (world.getName().equals("skyblock_lobby")) {
            Skblock.createNpcs(world);
        }
    }


    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        String worldName = event.getWorld().getName();

        Skblock.getNpcIds().remove(worldName);
        Skblock.getNpcService().removeAllNpcsInWorld(worldName);
    }

}
