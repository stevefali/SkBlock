package com.steve.skblock;

import com.steve.skblock.events.BlockEvent;
import com.steve.skblock.events.CobbleGenerationEvent;
import com.steve.skblock.events.PlayerEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class Skblock extends JavaPlugin {


    private PlayerEvent playerEvent;
    private CobbleGenerationEvent cobbleGenerationEvent;
    private BlockEvent blockEvent;

    @Override
    public void onEnable() {
        // Plugin startup logic

        playerEvent = new PlayerEvent();
        cobbleGenerationEvent = new CobbleGenerationEvent(this);
        blockEvent = new BlockEvent();


        getServer().getPluginManager().registerEvents(playerEvent, this);
        getServer().getPluginManager().registerEvents(cobbleGenerationEvent, this);
        getServer().getPluginManager().registerEvents(blockEvent, this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        HandlerList.unregisterAll();
    }
}
