package com.steve.skblock.sidebar;

import com.steve.MegaHUD.api.MegaHudService;
import com.steve.MegaHUD.api.SidebarLine;
import com.steve.skblock.Skblock;
import com.steve.skblock.game.SkyblockProgress;
import com.steve.skblock.game.SkyblockSession;
import com.steve.skblock.game.quest.SkyblockQuest;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.steve.skblock.Skblock.SKYBLOCK_LOBBY_NAME;
import static com.steve.skblock.worlds.SkyblockWorldFactory.WORLD_NAME_PREFIX;

public class SkyblockSidebar {

    private static final Map<Integer, SidebarLine> lobbySidebarBaseLines = new HashMap<>();
    private static final Map<Integer, SidebarLine> skyblockSidebarBaseLines = new HashMap<>();
    private static final String SIDEBAR_TITLE = "§a§lSKYBLOCK";
    private static MegaHudService hudService;

    private static final int QUEST_INDEX = 2;
    private static final int QUEST_DESCRIPTION_INDEX = 3;
    private static final int QUEST_PROGRESS_INDEX = 4;
    private static final int SKYBLOCK_SCORE_INDEX = 6;

    public static void register(MegaHudService megaHudService) {
        hudService = megaHudService;
        populateLobbyMap();
        populateSkyblockMap();
    }


    public static void showSidebar(UUID playerId, String worldName) {
        Map<Integer, SidebarLine> sidebarBaseLines = (worldName.equals(SKYBLOCK_LOBBY_NAME)
                ? lobbySidebarBaseLines
                : skyblockSidebarBaseLines
        );

        hudService.setSidebarTitle(playerId, SIDEBAR_TITLE);
        hudService.setAllSidebarLines(playerId, sidebarBaseLines);

        int playerScore = 0;
        World skyblockWorld = Bukkit.getWorld(WORLD_NAME_PREFIX + playerId.toString());
        if (skyblockWorld != null) {
            SkyblockSession skyblockSession = Skblock.getSessionRegistry().getSession(skyblockWorld);
            if (skyblockSession != null) {
                playerScore = skyblockSession.getSkyblockScore();

                if (skyblockWorld.getName().equals(worldName)) {
                    SkyblockProgress currentProgress = skyblockSession.getSkyblockProgress();
                    if (currentProgress != null) {
                        SkyblockQuest currentQuest = Skblock.getQuestRegistry().getQuest(currentProgress.getCurrentQuestId());
                        if (currentQuest != null) {
                            setQuestLine(playerId, currentQuest.getTitle());
                            setQuestDescriptionLine(playerId, currentQuest.getDescription());
                            setQuestProgressLine(playerId, currentProgress.getCurrentQuestProgress(), currentQuest.getTargetAmount());
                        }
                    }
                }
            }
        }
        setSkyblockScoreLine(playerId, playerScore);
    }


    private static void populateLobbyMap() {
        lobbySidebarBaseLines.put(0, SidebarLine.rightSideOnly("§bSkyblock Lobby   "));
        lobbySidebarBaseLines.put(1, SidebarLine.BLANK);
//        lobbySidebarBaseLines.put(2, SidebarLine.BLANK);
//        lobbySidebarBaseLines.put(3, SidebarLine.BLANK);
//        lobbySidebarBaseLines.put(4, SidebarLine.BLANK);
        lobbySidebarBaseLines.put(5, SidebarLine.BLANK);
        lobbySidebarBaseLines.put(7, SidebarLine.BLANK);
        lobbySidebarBaseLines.put(8, SidebarLine.leftSideOnly("Talk to §6Meg§r for"));
        lobbySidebarBaseLines.put(9, SidebarLine.leftSideOnly("Skyblock options"));
        lobbySidebarBaseLines.put(10, SidebarLine.leftSideOnly("or to return to the"));
        lobbySidebarBaseLines.put(11, SidebarLine.leftSideOnly("main lobby."));
    }

    private static void populateSkyblockMap() {
        skyblockSidebarBaseLines.put(0, SidebarLine.rightSideOnly("§bYour Skyblock World  "));
        skyblockSidebarBaseLines.put(1, SidebarLine.BLANK);
//        skyblockSidebarBaseLines.put(2, SidebarLine.BLANK);
//        skyblockSidebarBaseLines.put(3, SidebarLine.BLANK);
//        skyblockSidebarBaseLines.put(4, SidebarLine.BLANK);
        skyblockSidebarBaseLines.put(5, SidebarLine.BLANK);
    }

    public static void setSkyblockScoreLine(UUID playerId, int score) {
        hudService.setSidebarLine(playerId, SKYBLOCK_SCORE_INDEX, SidebarLine.bothSides("§eSkyblock Score: ", "§5§l" + score));
    }

    public static void setQuestLine(UUID playerId, String questTitle) {
        hudService.setSidebarLine(playerId, QUEST_INDEX, SidebarLine.leftSideOnly("Quest: §e§l" + questTitle));
    }

    public static void setQuestDescriptionLine(UUID playerId, String description) {
        hudService.setSidebarLine(playerId, QUEST_DESCRIPTION_INDEX, SidebarLine.leftSideOnly("§a" + description));
    }

    public static void setQuestProgressLine(UUID playerId, int questProgress, int targetAmount) {
        hudService.setSidebarLine(playerId, QUEST_PROGRESS_INDEX, SidebarLine.leftSideOnly("Progress: §l" + questProgress + "/" + targetAmount));
    }

}
