package com.steve.skblock.events.unrouted;

import com.steve.skblock.Skblock;
import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.npc.NpcFactory;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.Plugin;

public class WorldEvent implements Listener {

    private final Plugin plugin;
    private final SessionRegistry sessionRegistry;

    public WorldEvent(Plugin plugin, SessionRegistry sessionRegistry) {
        this.plugin = plugin;
        this.sessionRegistry = sessionRegistry;
    }

    @EventHandler
    public void onWorldLoaded(WorldLoadEvent event) {
        World world = event.getWorld();

        if (world.getName().equals("skyblock_lobby")) {
            NpcFactory.createNpcs(world, plugin);
        }
    }


    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        String worldName = event.getWorld().getName();

        Skblock.getNpcIds().remove(worldName);
        Skblock.getNpcService().removeAllNpcsInWorld(worldName);
        sessionRegistry.unregister(event.getWorld());
    }

}
