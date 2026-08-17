package com.steve.skblock;

import com.steve.MegaNPCs.api.NpcService;
import com.steve.skblock.commands.*;
import com.steve.skblock.events.*;
import com.steve.skblock.npc.NpcFactory;
import com.steve.skblock.util.ProxyTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

public final class Skblock extends JavaPlugin {


    private PlayerEvent playerEvent;
    private CobbleGenerationEvent cobbleGenerationEvent;
    private BlockEvent blockEvent;
    private PortalEvents portalEvents;

    private static NpcService npcService;
    private static Location lobbySpawn;
    private static Plugin plugin;
    private Logger logger;

    private static final Map<String, List<UUID>> NPC_IDS = new HashMap<>();
    private static final double NPC_TITLE_SCALE = 2.0;
    private static final String SKYBLOCK_LOBBY_NAME = "skyblock_lobby";



    @Override
    public void onEnable() {
        // Plugin startup logic


        npcService = Bukkit.getServicesManager().load(NpcService.class);
        lobbySpawn = new Location(Bukkit.getWorld(SKYBLOCK_LOBBY_NAME), 0.5, 65, 0.5, 30.0F, 0.0F);
        plugin = this;
        logger = this.getLogger();

        playerEvent = new PlayerEvent(this, lobbySpawn);
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


        World skyblockLobbyWorld = Bukkit.getWorld("skyblock_lobby");
        if (skyblockLobbyWorld != null) {
            skyblockLobbyWorld.setSpawnLocation(lobbySpawn);
            skyblockLobbyWorld.setPVP(false);
            skyblockLobbyWorld.setDifficulty(Difficulty.PEACEFUL);
        }

        for (World world : Bukkit.getWorlds()) {
            createNpcs(world);
            if (npcService.getNpcsInWorld(world.getName()) != null) {
                for (Player player : world.getPlayers()) {
                    NpcFactory.showNPCs(world.getName(), player);
                }
            }
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic


        for (World world : Bukkit.getWorlds()) {
            npcService.removeAllNpcsInWorld(world.getName());
            logger.info("Removed NPCs for world " + world.getName());
        }
        NPC_IDS.clear();
        npcService = null;


        getServer().getMessenger().unregisterOutgoingPluginChannel(this);

        HandlerList.unregisterAll(this);

    }


    public static void createNpcs(World world) {
        String worldName = world.getName();

        if (NPC_IDS.get(worldName) == null) {
            List<UUID> worldNpcIds = new ArrayList<>();

            if (worldName.equals("skyblock_lobby") && npcService != null) {

                UUID megId = NpcFactory.makeNpc(
                        new Location(world, -6.5, 65.5, 5.5, -105.0F, 8.0F),
                        "Meg",
                        "Meg", player -> {
                            ProxyTeleport.teleportPlayer(plugin, player, ProxyTeleport.LOBBY_SERVER);
                        },
                        "§6Meg \n§aMain Lobby",
                        NPC_TITLE_SCALE
                );
                worldNpcIds.add(megId);
                npcService.setDefaultNameVisible(megId, false);

            } else {

                // Skyblock worlds
                UUID randyId = NpcFactory.makeNpc(
                        new Location(world, -3.5, 65, -3.5, -40.0F, 0.0F),
                        "Randy",
                        "Randy",
                        player -> {
                            player.sendMessage("Welcome to your skyblock World, " + player.getName());
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                player.teleport(lobbySpawn);
                            }, 40L);
                        },
                        null,
                        NPC_TITLE_SCALE,
                        "§6Randy", "§aSkyblock Lobby"
                );
                worldNpcIds.add(randyId);
            }
            NPC_IDS.put(worldName, worldNpcIds);
        }
    }

    public static NpcService getNpcService() {
        return npcService;
    }

    public static Map<String, List<UUID>> getNpcIds() {
        return NPC_IDS;
    }
}
