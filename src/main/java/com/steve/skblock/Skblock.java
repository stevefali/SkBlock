package com.steve.skblock;

import com.steve.skblock.commands.*;
import com.steve.skblock.events.BlockEvent;
import com.steve.skblock.events.CobbleGenerationEvent;
import com.steve.skblock.events.PlayerEvent;
import com.steve.skblock.events.PortalEvents;
import com.steve.skblock.network.PacketListenerInjector;
import com.steve.skblock.npc.NpcSkin;
import com.steve.skblock.npc.NpcSkinDataAccess;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
//import org.mvplugins.multiverse.portals.MultiversePortals;

public final class Skblock extends JavaPlugin {


    private PlayerEvent playerEvent;
    private CobbleGenerationEvent cobbleGenerationEvent;
    private BlockEvent blockEvent;
    private PortalEvents portalEvents;

    public static Map<String, NpcSkin> NPC_SKINS = null;

    @Override
    public void onEnable() {
        // Plugin startup logic


        playerEvent = new PlayerEvent(this);
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

        // TODO: Move/refactor this to setup lobby NPC's when lobby world is loaded (and skyblock worlds when they load)
       /* NpcSkinDataAccess.load(this)
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                })
                .thenApply(result -> {
                    NPC_SKINS = result;
                    return result;
                });*/

//        List<Player> skyblockPlayers = Bukkit.getWorld("skyblock_lobby").getPlayers();
//        for (Player player : skyblockPlayers) {
//            PacketListenerInjector.inject(player, this);
//        }

//        List<Player> currentPlayers =

        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketListenerInjector.inject(player, this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketListenerInjector.eject(player);
        }

        getServer().getMessenger().unregisterOutgoingPluginChannel(this);

        HandlerList.unregisterAll();
    }
}
