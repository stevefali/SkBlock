package com.steve.skblock.game;

import com.steve.MegaNPCs.api.NpcInteractionEvent;
import com.steve.skblock.Skblock;
import com.steve.skblock.game.data.SkyblockDataStore;
import com.steve.skblock.game.quest.SkyblockQuest;
import com.steve.skblock.game.quest.type.CraftingQuest;
import com.steve.skblock.game.quest.type.HarvestQuest;
import com.steve.skblock.game.quest.type.PlacingQuest;
import com.steve.skblock.game.quest.type.TalkToRandyQuest;
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
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
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
        Player player = getPlayer();
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.5f, 2.5f);

        SkyblockQuest currentQuest = getCurrentQuest();
        if (skyblockProgress.getCurrentQuestProgress() >= currentQuest.getTargetAmount()) {
            completeQuest(skyblockProgress, currentQuest);
        }
        SkyblockSidebar.updateQuestSidebarLines(worldOwnerPlayerId, this);
        progressDirty = true;
    }

    public void onBlockBreak(BlockBreakEvent event) {
//        Player player = event.getPlayer();

        if (getCurrentQuest() instanceof HarvestQuest harvestQuest && event.getBlock().getType() == harvestQuest.getMaterial()) {
            advanceQuest(1);
        }

        //      TODO: Remove this!!
        if (event.getBlock().getType() == Material.ORANGE_WOOL) {
//            addScore(1);
            advanceQuest(1);
        }
    }


    public void onGenerateCobble(BlockFormEvent event) {

    }

    public void onNpcInteract(NpcInteractionEvent event) {
        if (getCurrentQuest() instanceof TalkToRandyQuest) {
            event.setCancelled(true);
            advanceQuest(1);
        }
    }

    public void onItemCrafted(ItemStack craftedItemStack) {
        if (getCurrentQuest() instanceof CraftingQuest craftingQuest && craftedItemStack.getType() == craftingQuest.getMaterial()) {
            advanceQuest(craftedItemStack.getAmount());
        }
    }

    public void onBlockPlaced(BlockPlaceEvent event) {
        if (getCurrentQuest() instanceof PlacingQuest placingQuest && event.getBlockPlaced().getType() == placingQuest.getMaterial()) {
            advanceQuest(1);
        }
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
        TitlesUtils.sendSubtitle(player, "§6Completed: " + currentQuest.getDescription(), 7, 60, 7);
        addScore(currentQuest.getReward());
        if (currentQuest.getRewardAction() != null) {
            currentQuest.performRewardAction(player);
            player.sendMessage(currentQuest.getRewardMessage());
        }

        Bukkit.getScheduler().runTaskLater(
                plugin, () -> {
                    skyblockProgress.onQuestComplete(currentQuest.getNextQuestId());
                    SkyblockSidebar.updateQuestSidebarLines(worldOwnerPlayerId, this);
                }, 40L
        );
    }

    private SkyblockQuest getCurrentQuest() {
        return Skblock.getQuestRegistry().getQuest(skyblockProgress.getCurrentQuestId());
    }

    private Player getPlayer() {
        return Bukkit.getPlayer(worldOwnerPlayerId);
    }

    /**
     * THIS SHOULD ONLY BE USED FOR FORCE-SETTING THE SCORE!
     */
    public void forceSetScore(int score) {
        this.skyblockScore = score;
        saveAll();
    }

}
