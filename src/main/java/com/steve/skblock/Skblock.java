package com.steve.skblock;

import com.steve.skblock.commands.*;
import com.steve.skblock.events.BlockEvent;
import com.steve.skblock.events.CobbleGenerationEvent;
import com.steve.skblock.events.PlayerEvent;
import com.steve.skblock.events.PortalEvents;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
//import org.mvplugins.multiverse.portals.MultiversePortals;

public final class Skblock extends JavaPlugin {


    private PlayerEvent playerEvent;
    private CobbleGenerationEvent cobbleGenerationEvent;
    private BlockEvent blockEvent;
    private PortalEvents portalEvents;

    @Override
    public void onEnable() {
        // Plugin startup logic



        playerEvent = new PlayerEvent();
        cobbleGenerationEvent = new CobbleGenerationEvent(this);
        blockEvent = new BlockEvent(this);
        portalEvents = new PortalEvents(this);


        getServer().getPluginManager().registerEvents(playerEvent, this);
        getServer().getPluginManager().registerEvents(cobbleGenerationEvent, this);
        getServer().getPluginManager().registerEvents(blockEvent, this);
        getServer().getPluginManager().registerEvents(portalEvents, this);


        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getCommand("sendplayer").setExecutor(new SendPlayerCommand(this));
        getCommand("makeworld").setExecutor(new MakeWorldCommand(this));
        getCommand("deleteworld").setExecutor(new DeleteWorldCommand(this));
        getCommand("loadworld").setExecutor(new LoadWorldCommand(this));
        getCommand("lobby").setExecutor(new LobbyCommand());
        getCommand("home").setExecutor(new HomeCommand(this));


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        getServer().getMessenger().unregisterOutgoingPluginChannel(this);

        HandlerList.unregisterAll();
    }
}
