package com.steve.skblock;

import com.steve.MegaHUD.api.MegaHudService;
import com.steve.MegaNPCs.api.NpcService;
import com.steve.skblock.commands.*;
import com.steve.skblock.events.routed.*;
import com.steve.skblock.events.unrouted.*;
import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import com.steve.skblock.game.data.NbtSkyblockDataStore;
import com.steve.skblock.game.data.SkyblockDataStore;
import com.steve.skblock.game.quest.QuestRegistry;
import com.steve.skblock.menu.MenuProvider;
import com.steve.skblock.npc.NpcFactory;
import com.steve.skblock.sidebar.SkyblockSidebar;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public final class Skblock extends JavaPlugin {


    private PlayerEvent playerEvent;
    private BlockGeneratorEvent cobbleGenerationEvent;
    private BlockEvent blockEvent;
    private PortalEvents portalEvents;

    private static NpcService npcService;
    private static MegaHudService megaHudService;
    private static Location lobbySpawn;
    private Logger logger;

    private static final SkyblockDataStore skyblockDataStore = new NbtSkyblockDataStore();
    private static final SessionRegistry sessionRegistry = new SessionRegistry(skyblockDataStore);
    private static final Map<String, List<UUID>> NPC_IDS = new HashMap<>();
    private static QuestRegistry questRegistry;

    public static final String SKYBLOCK_LOBBY_NAME = "skyblock_lobby";

    @Override
    public void onEnable() {
        // Plugin startup logic

        npcService = Bukkit.getServicesManager().load(NpcService.class);
        megaHudService = Bukkit.getServicesManager().load(MegaHudService.class);
        lobbySpawn = new Location(Bukkit.getWorld(SKYBLOCK_LOBBY_NAME), 0.5, 65, 0.5, 30.0F, 0.0F);
        logger = this.getLogger();

        saveDefaultConfig();
        reloadConfig();
        questRegistry = new QuestRegistry(this);

        SkyblockSidebar.register(megaHudService);

        playerEvent = new PlayerEvent(this, lobbySpawn);
        cobbleGenerationEvent = new BlockGeneratorEvent(this, sessionRegistry);
        blockEvent = new BlockEvent(this, sessionRegistry);
        portalEvents = new PortalEvents(this);


        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(playerEvent, this);
        pluginManager.registerEvents(cobbleGenerationEvent, this);
        pluginManager.registerEvents(blockEvent, this);
        pluginManager.registerEvents(portalEvents, this);
        pluginManager.registerEvents(new WorldEvent(this, sessionRegistry), this);
        pluginManager.registerEvents(new InventoryEvent(this, sessionRegistry), this);
        pluginManager.registerEvents(new MegaHudEvent(), this);
        pluginManager.registerEvents(new NpcEvent(sessionRegistry), this);
        pluginManager.registerEvents(new MobSpawnEvent(sessionRegistry), this);


        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getCommand("sendplayer").setExecutor(new SendPlayerCommand(this));
        getCommand("makeworld").setExecutor(new MakeWorldCommand(this));
        getCommand("deleteworld").setExecutor(new DeleteWorldCommand(this));
        getCommand("loadworld").setExecutor(new LoadWorldCommand(this));
        getCommand("lobby").setExecutor(new LobbyCommand());
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("forceSetPlayerQuest").setExecutor(new ForceSetPlayerQuestCommand());
        getCommand("forceSetPlayerScore").setExecutor(new ForceSetPlayerScoreCommand());
        getCommand("setIsland").setExecutor(new SetIslandCommand());

        MenuProvider.register(this);

        World skyblockLobbyWorld = Bukkit.getWorld(SKYBLOCK_LOBBY_NAME);
        if (skyblockLobbyWorld != null) {
            skyblockLobbyWorld.setSpawnLocation(lobbySpawn);
            skyblockLobbyWorld.setPVP(false);
            skyblockLobbyWorld.setDifficulty(Difficulty.PEACEFUL);
        }

        for (World world : Bukkit.getWorlds()) {
            NpcFactory.createNpcs(world, this);
            if (npcService.getNpcsInWorld(world.getName()) != null) {
                for (Player player : world.getPlayers()) {
                    NpcFactory.showNPCs(world.getName(), player);
                }
            }

            if (!world.getName().equals(SKYBLOCK_LOBBY_NAME)) {
                sessionRegistry.createSession(world);
            }

            for (Player player : world.getPlayers()) {
                SkyblockSidebar.showSidebar(player.getUniqueId(), world.getName());
            }
        }

        sessionRegistry.scheduleAutoSaves(this);
        sessionRegistry.startBiomeTask();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic


        for (World world : Bukkit.getWorlds()) {
            npcService.removeAllNpcsInWorld(world.getName());
            logger.info("Removed NPCs for world " + world.getName());

            if (!world.getName().equals(SKYBLOCK_LOBBY_NAME)) {
                SkyblockSession skyblockSession = sessionRegistry.getSession(world);
                if (skyblockSession != null) {
                    skyblockSession.saveDirty();
                    skyblockSession.onUnload();
                }
            }
            npcService.removeOrphansFromWorld(world.getName());
        }
        NPC_IDS.clear();
        npcService = null;

        sessionRegistry.stopBiomeTask();
        sessionRegistry.stopAutoSaves();
        sessionRegistry.unregisterAll();

        megaHudService = null;

        getServer().getMessenger().unregisterOutgoingPluginChannel(this);

        HandlerList.unregisterAll(this);

    }

    public static NpcService getNpcService() {
        return npcService;
    }

    public static Map<String, List<UUID>> getNpcIds() {
        return NPC_IDS;
    }

    public static Location getLobbySpawn() {
        return lobbySpawn;
    }

    public static SessionRegistry getSessionRegistry() {
        return sessionRegistry;
    }

    public static MegaHudService getMegaHudService() {
        return megaHudService;
    }

    public static QuestRegistry getQuestRegistry() {
        return questRegistry;
    }


}
