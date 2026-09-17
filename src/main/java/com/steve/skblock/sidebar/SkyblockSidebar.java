package com.steve.skblock.sidebar;

import com.steve.MegaHUD.api.MegaHudService;
import com.steve.MegaHUD.api.SidebarLine;
import com.steve.skblock.Skblock;
import com.steve.skblock.game.SkyblockSession;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.steve.skblock.Skblock.SKYBLOCK_LOBBY_NAME;
import static com.steve.skblock.worlds.SkyblockWorldFactory.WORLD_NAME_PREFIX;

public class SkyblockSidebar {

    private static final Map<Integer, SidebarLine> lobbySidebarLines = new HashMap<>();
    private static final Map<Integer, SidebarLine> skyblockSidebarLines = new HashMap<>();
    private static final String SIDEBAR_TITLE = "§a§lSKYBLOCK";
    private static MegaHudService hudService;

    private static final int SKYBLOCK_SCORE_INDEX = 3;

    public static void register(MegaHudService megaHudService) {
        hudService = megaHudService;
        populateLobbyMap();
        populateSkyblockMap();
    }


    public static void showSidebar(UUID playerId, String worldName) {
        Map<Integer, SidebarLine> sidebarBaseLines = (worldName.equals(SKYBLOCK_LOBBY_NAME)
                ? lobbySidebarLines
                : skyblockSidebarLines
        );

        hudService.setSidebarTitle(playerId, SIDEBAR_TITLE);
        hudService.setAllSidebarLines(playerId, sidebarBaseLines);

        int playerScore = 0;
        World skyblockWorld = Bukkit.getWorld(WORLD_NAME_PREFIX + playerId.toString());
        if (skyblockWorld != null) {
            SkyblockSession skyblockSession = Skblock.getSessionRegistry().getSession(skyblockWorld);
            if (skyblockSession != null) {
                playerScore = skyblockSession.getSkyblockScore();
            }
        }
        setSkyblockScoreLine(playerId, playerScore);
    }


    private static void populateLobbyMap() {
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

    private static void populateSkyblockMap() {
        skyblockSidebarLines.put(0, SidebarLine.BLANK);
        skyblockSidebarLines.put(1, SidebarLine.rightSideOnly("§bYour Skyblock World  "));
        skyblockSidebarLines.put(2, SidebarLine.BLANK);
        skyblockSidebarLines.put(3, SidebarLine.BLANK);
        skyblockSidebarLines.put(4, SidebarLine.BLANK);
    }

    public static void setSkyblockScoreLine(UUID playerId, int score) {
        hudService.setSidebarLine(
                playerId,
                SKYBLOCK_SCORE_INDEX,
                SidebarLine.bothSides("§eSkyblock Score: ", "§5§l" + score)
        );
    }

}
