package com.steve.skblock.sidebar;

import com.steve.MegaHUD.api.MegaHudService;
import com.steve.MegaHUD.api.SidebarLine;
import com.steve.skblock.Skblock;
import com.steve.skblock.game.SkyblockProgress;
import com.steve.skblock.game.SkyblockSession;
import com.steve.skblock.game.quest.SkyblockQuest;
import com.steve.skblock.util.NameFormatter;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

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
    private static final int ISLAND_INDEX = 8;
    private static final int PERCENTAGES_START_INDEX = 9;

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
                    updateQuestSidebarLines(playerId, skyblockSession);

                    Bukkit.getScheduler().runTaskLater(
                            JavaPlugin.getPlugin(Skblock.class), () -> {
                                Location playerLocation = Bukkit.getPlayer(playerId).getLocation();
                                NamespacedKey playerBiome = skyblockWorld.getBiome(playerLocation).getKey();
                                setIslandLine(playerId, NameFormatter.format(playerBiome.getKey()));
                            }, 1L
                    );
                }
            }
        }
        setSkyblockScoreLine(playerId, playerScore);
    }

    public static void updateQuestSidebarLines(
            UUID playerId,
            SkyblockSession skyblockSession  /* SkyblockProgress currentProgress, SkyblockQuest currentQuest*/) {
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

    public static void updateIslandLines(UUID playerId, String islandName, Map<Material, String> percentages) {
        setIslandLine(playerId, islandName);
        setPercentageLines(playerId, percentages);
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
        skyblockSidebarBaseLines.put(7, SidebarLine.BLANK);
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

    public static void setIslandLine(UUID playerId, String islandName) {
        hudService.setSidebarLine(playerId, ISLAND_INDEX, SidebarLine.leftSideOnly("Island: §b§l" + islandName));
    }

    public static void setPercentageLines(UUID playerId, Map<Material, String> percentages) {
        for (int i = 5; i < 11; i++) {
            hudService.removeSidebarLine(playerId, PERCENTAGES_START_INDEX + i);
        }
        int index = 0;
        for (Map.Entry<Material, String> entry : percentages.entrySet()) {
            hudService.setSidebarLine(
                    playerId,
                    PERCENTAGES_START_INDEX + index,
//                    SidebarLine.bothSides("§7" + NameFormatter.format(entry.getKey().name()) + ": ", "§e" + entry.getValue() + "%")
                    SidebarLine.rightSideOnly("§7" + NameFormatter.format(entry.getKey().name()) + ": " + entry.getValue() + "%")
            );
            index++;
        }
    }

}
