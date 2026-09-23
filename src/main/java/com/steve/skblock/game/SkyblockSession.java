package com.steve.skblock.game;

import com.steve.skblock.Skblock;
import com.steve.skblock.game.data.SkyblockDataStore;
import com.steve.skblock.game.quest.SkyblockQuest;
import com.steve.skblock.sidebar.SkyblockSidebar;
import com.steve.skblock.util.TitlesUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

import static com.steve.skblock.worlds.SkyblockWorldFactory.WORLD_NAME_PREFIX;

public class SkyblockSession {

    private final UUID worldId;
    private final UUID worldOwnerPlayerId;
    private final SkyblockDataStore skyblockDataStore;

    private int skyblockScore;
    private boolean scoreDirty = false;

    private SkyblockProgress skyblockProgress;
    private boolean progressDirty = false;

    private static final Plugin plugin = JavaPlugin.getPlugin(Skblock.class);


    public SkyblockSession(World world, SkyblockDataStore skyblockDataStore) {
        if (!world.getName().startsWith(WORLD_NAME_PREFIX)) {
            throw new IllegalArgumentException(
                    "Error: Skyblock world " + world.getName() + "'s name is invalid for Skyblock worlds"
            );
        }

        this.worldId = world.getUID();
        this.worldOwnerPlayerId = UUID.fromString(StringUtils.substringAfter(world.getName(), WORLD_NAME_PREFIX));
        this.skyblockDataStore = skyblockDataStore;

        this.skyblockScore = skyblockDataStore.loadSkyblockScore(worldOwnerPlayerId);
        this.skyblockProgress = skyblockDataStore.loadProgress(worldOwnerPlayerId);
    }

    public void saveAll() {
        skyblockDataStore.saveSkyblockScore(worldOwnerPlayerId, skyblockScore);
        skyblockDataStore.saveProgress(worldOwnerPlayerId, skyblockProgress);
        scoreDirty = false;
        progressDirty = false;
    }

    public void saveDirty() {
        if (scoreDirty) {
            skyblockDataStore.saveSkyblockScore(worldOwnerPlayerId, skyblockScore);
            scoreDirty = false;
        }
        if (progressDirty) {
            skyblockDataStore.saveProgress(worldOwnerPlayerId, skyblockProgress);
            progressDirty = true;
        }
    }

    public void addScore(int amount) {
        this.skyblockScore += amount;
        this.scoreDirty = true;
        SkyblockSidebar.setSkyblockScoreLine(worldOwnerPlayerId, this.skyblockScore);
    }

    public void advanceQuest(int amount) {
        skyblockProgress.setCurrentQuestProgress(skyblockProgress.getCurrentQuestProgress() + amount);

        SkyblockQuest currentQuest = Skblock.getQuestRegistry().getQuest(skyblockProgress.getCurrentQuestId());
        if (skyblockProgress.getCurrentQuestProgress() >= currentQuest.getTargetAmount()) {
            completeQuest(skyblockProgress, currentQuest);
        }
        SkyblockSidebar.updateQuestSidebarLines(worldOwnerPlayerId, this);
        progressDirty = true;
    }

    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getType() == Material.BLACK_WOOL) {
            player.sendMessage("You broke that in a skyblock world! Score: " + skyblockScore);
        }
        if (event.getBlock().getType() == Material.ORANGE_WOOL) {
//            addScore(1);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.5f, 2.5f);
            advanceQuest(1);
        }
    }

    public void onGenerateCobble(BlockFormEvent event) {

    }


    private World getWorld() {
        World world = Bukkit.getWorld(worldId);
        if (world == null) {
            throw new NullPointerException("World with id " + worldId + " is null!");
        }
        return world;
    }

    public int getSkyblockScore() {
        return this.skyblockScore;
    }

    public SkyblockProgress getSkyblockProgress() {
        return this.skyblockProgress;
    }

    private void completeQuest(SkyblockProgress currentProgress, SkyblockQuest currentQuest) {
        Player player = Bukkit.getPlayer(worldOwnerPlayerId);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        TitlesUtils.sendSubtitle(player, "§6Quest Completed: " + currentQuest.getTitle(), 7, 40, 7);
        addScore(currentQuest.getReward());
        if (currentQuest.getRewardAction() != null) {
            currentQuest.performRewardAction(player);
            player.sendMessage(currentQuest.getRewardMessage());
        }
        skyblockProgress.onQuestComplete(currentQuest.getNextQuestId());

        Bukkit.getScheduler().runTaskLater(
                plugin, () -> {
                    SkyblockSidebar.updateQuestSidebarLines(worldOwnerPlayerId, this);
                }, 40L
        );
    }

}
