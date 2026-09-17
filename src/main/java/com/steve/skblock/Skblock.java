package com.steve.skblock;

import com.steve.MegaHUD.api.MegaHudService;
import com.steve.MegaHUD.api.SidebarLine;
import com.steve.MegaNPCs.api.NpcService;
import com.steve.skblock.commands.*;
import com.steve.skblock.events.routed.BlockEvent;
import com.steve.skblock.events.routed.CobbleGenerationEvent;
import com.steve.skblock.events.unrouted.*;
import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import com.steve.skblock.game.data.NbtSkyblockDataStore;
import com.steve.skblock.game.data.SkyblockDataStore;
import com.steve.skblock.menu.MenuProvider;
import com.steve.skblock.npc.NpcFactory;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

public final class Skblock extends JavaPlugin {


    private PlayerEvent playerEvent;
    private CobbleGenerationEvent cobbleGenerationEvent;
    private BlockEvent blockEvent;
    private PortalEvents portalEvents;

    private static NpcService npcService;
    private static MegaHudService megaHudService;
    private static Location lobbySpawn;
    private Logger logger;

    private static final SkyblockDataStore skyblockDataStore = new NbtSkyblockDataStore();
    private static final SessionRegistry sessionRegistry = new SessionRegistry(skyblockDataStore);
    private static final Map<String, List<UUID>> NPC_IDS = new HashMap<>();

    public static final String SKYBLOCK_LOBBY_NAME = "skyblock_lobby";
    private static final Map<Integer, SidebarLine> lobbySidebarLines = new HashMap<>();
    public static final String LOBBY_SIDEBAR_TITLE = "§a§lSKYBLOCK";


    @Override
    public void onEnable() {
        // Plugin startup logic

        npcService = Bukkit.getServicesManager().load(NpcService.class);
        megaHudService = Bukkit.getServicesManager().load(MegaHudService.class);
        lobbySpawn = new Location(Bukkit.getWorld(SKYBLOCK_LOBBY_NAME), 0.5, 65, 0.5, 30.0F, 0.0F);
        logger = this.getLogger();

        playerEvent = new PlayerEvent(this, lobbySpawn);
        cobbleGenerationEvent = new CobbleGenerationEvent(this);
        blockEvent = new BlockEvent(this, sessionRegistry);
        portalEvents = new PortalEvents(this);


        getServer().getPluginManager().registerEvents(playerEvent, this);
        getServer().getPluginManager().registerEvents(cobbleGenerationEvent, this);
        getServer().getPluginManager().registerEvents(blockEvent, this);
        getServer().getPluginManager().registerEvents(portalEvents, this);
        getServer().getPluginManager().registerEvents(new WorldEvent(this, sessionRegistry), this);
        getServer().getPluginManager().registerEvents(new InventoryEvent(), this);
        getServer().getPluginManager().registerEvents(new MegaHudEvent(), this);


        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getCommand("sendplayer").setExecutor(new SendPlayerCommand(this));
        getCommand("makeworld").setExecutor(new MakeWorldCommand(this));
        getCommand("deleteworld").setExecutor(new DeleteWorldCommand(this));
        getCommand("loadworld").setExecutor(new LoadWorldCommand(this));
        getCommand("lobby").setExecutor(new LobbyCommand());
        getCommand("home").setExecutor(new HomeCommand(this));

        MenuProvider.register(this);

        populateLobbySidebarMap();

        World skyblockLobbyWorld = Bukkit.getWorld(SKYBLOCK_LOBBY_NAME);
        if (skyblockLobbyWorld != null) {
            skyblockLobbyWorld.setSpawnLocation(lobbySpawn);
            skyblockLobbyWorld.setPVP(false);
            skyblockLobbyWorld.setDifficulty(Difficulty.PEACEFUL);

            for (Player player : skyblockLobbyWorld.getPlayers()) {
                showSidebar(player.getUniqueId());
            }
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
        }

        sessionRegistry.scheduleAutoSaves(this);
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
                }
            }
            npcService.removeOrphansFromWorld(world.getName());
        }
        NPC_IDS.clear();
        npcService = null;

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

    public static Map<Integer, SidebarLine> getLobbySidebarLines() {
        return lobbySidebarLines;
    }

    private static void populateLobbySidebarMap() {
        lobbySidebarLines.put(0, SidebarLine.BLANK);
        lobbySidebarLines.put(1, SidebarLine.rightSideOnly("§bSkyblock Lobby   "));
        lobbySidebarLines.put(2, SidebarLine.BLANK);
        lobbySidebarLines.put(3, SidebarLine.BLANK);
        lobbySidebarLines.put(4, SidebarLine.BLANK);
        lobbySidebarLines.put(5, SidebarLine.leftSideOnly("Talk to §6Meg§r for"));
        lobbySidebarLines.put(6, SidebarLine.leftSideOnly("Skyblock options"));
        lobbySidebarLines.put(7, SidebarLine.leftSideOnly("or to return to the"));
        lobbySidebarLines.put(8, SidebarLine.leftSideOnly("main lobby."));
    }

    public static void showSidebar(UUID playerId) {
        megaHudService.setSidebarTitle(playerId, LOBBY_SIDEBAR_TITLE);
        megaHudService.setAllSidebarLines(playerId, lobbySidebarLines);
        megaHudService.setSidebarLine(
                playerId,
                3,
                SidebarLine.bothSides("§eSkyblock Score: ", "§5§l47")
        );
    }

}
