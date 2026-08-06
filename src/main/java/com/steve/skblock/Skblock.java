package com.steve.skblock;

import com.steve.skblock.commands.*;
import com.steve.skblock.events.*;
import com.steve.skblock.network.PacketListenerInjector;
import com.steve.skblock.npc.NPC;
import com.steve.skblock.npc.NPCs;
import com.steve.skblock.npc.NpcSkin;
import com.steve.skblock.npc.NpcSkinDataAccess;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
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
        getServer().getPluginManager().registerEvents(new WorldEvent(), this);


        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getCommand("sendplayer").setExecutor(new SendPlayerCommand(this));
        getCommand("makeworld").setExecutor(new MakeWorldCommand(this));
        getCommand("deleteworld").setExecutor(new DeleteWorldCommand(this));
        getCommand("loadworld").setExecutor(new LoadWorldCommand(this));
        getCommand("lobby").setExecutor(new LobbyCommand());
        getCommand("home").setExecutor(new HomeCommand(this));


        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketListenerInjector.inject(player, this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        try {


           NPC npc = NPCs.npcMap.remove("Randy_skyblock_lobby");

            for (Player player : Bukkit.getOnlinePlayers()) {
                ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;

                ClientboundRemoveEntitiesPacket removeEntitiesPacket = new ClientboundRemoveEntitiesPacket(npc.getId());

                connection.send(removeEntitiesPacket);
                System.out.println("Removed NPC");
            }



        } catch (Exception e) {
            System.out.println("Error removing npc's");
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketListenerInjector.eject(player);
        }

        getServer().getMessenger().unregisterOutgoingPluginChannel(this);

        HandlerList.unregisterAll();
    }
}
