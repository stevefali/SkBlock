package com.steve.skblock.events.routed;

import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class BlockEvent implements Listener {

    private final Plugin plugin;
    private final SessionRegistry sessionRegistry;
    private static final String ADMIN_PERMISSION = "skyblock.admin";

    private static ArrayList<Sound> sounds = new ArrayList<>(List.of(
//            Sound.BLOCK_ENCHANTMENT_TABLE_USE,
//            Sound.BLOCK_GLASS_BREAK,
//            Sound.BLOCK_HEAVY_CORE_BREAK,
            Sound.BLOCK_NOTE_BLOCK_CHIME,
//            Sound.BLOCK_NOTE_BLOCK_FLUTE,
//            Sound.BLOCK_NOTE_BLOCK_SNARE,
//            Sound.BLOCK_NOTE_BLOCK_XYLOPHONE,
//            Sound.BLOCK_NOTE_BLOCK_BANJO,
//            Sound.BLOCK_NOTE_BLOCK_BASEDRUM,
//            Sound.BLOCK_NOTE_BLOCK_BASS,
//            Sound.BLOCK_NOTE_BLOCK_BELL,
//            Sound.BLOCK_NOTE_BLOCK_COW_BELL,
            Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO,
//            Sound.BLOCK_NOTE_BLOCK_FLUTE,
//            Sound.BLOCK_NOTE_BLOCK_GUITAR,
//            Sound.BLOCK_NOTE_BLOCK_HARP,
//            Sound.BLOCK_NOTE_BLOCK_PLING,
//            Sound.BLOCK_NOTE_BLOCK_BIT,
            Sound.BLOCK_RESPAWN_ANCHOR_CHARGE
//            Sound.ENTITY_ALLAY_HURT,
//            Sound.ENTITY_BLAZE_HURT,
//            Sound.ENTITY_BREEZE_DEFLECT,
//            Sound.ENTITY_DOLPHIN_HURT,
//            Sound.ENTITY_PANDA_SNEEZE,
//            Sound.ITEM_OMINOUS_BOTTLE_DISPOSE
    ));
    private static int currentTree = 0;

    private static ArrayList<TreeType> treeTypes = new ArrayList<>(List.of(TreeType.values()));


    public BlockEvent(Plugin plugin, SessionRegistry sessionRegistry) {
        this.plugin = plugin;
        this.sessionRegistry = sessionRegistry;
        sounds.sort(Comparator.comparing(Sound::toString));
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {

        SkyblockSession skyblockSession = sessionRegistry.getSession(event.getBlock().getWorld());
        if (skyblockSession != null) {
            skyblockSession.onBlockBreak(event);
            return;
        }

        Player player = event.getPlayer();
        if (!player.hasPermission(ADMIN_PERMISSION)) {
            event.setCancelled(true);
            return;
        }

        Material type = event.getBlock().getType();


//        TODO: Remove this!!
        if (type == Material.BLACK_WOOL) {
            player.sendMessage("You broke that in the lobby!");

        }

/*

        if (type == Material.BLUE_WOOL) {
            currentTree++;
            if (currentTree > treeTypes.size() - 1) {
                currentTree = 0;
            }
        }
        if (type == Material.YELLOW_WOOL) {
            currentTree--;
            if (currentTree < 0) {
                currentTree = treeTypes.size() - 1;
            }
        }
        if (type == Material.GREEN_WOOL) {
//            player.playSound(player.getLocation(), sounds.get(currentTree), 2.0f, 1.0f);
//             player.getWorld().generateTree(event.getBlock().getLocation(), treeTypes.get(currentTree));
            Location treeLocation = event.getBlock().getLocation().add(new Vector(0, -1, 0));
            boolean success = player.getWorld().generateTree(treeLocation, TreeType.ACACIA);
//            player.sendMessage("Index: " + currentTree + ", Sound: " + sounds.get(currentTree).toString());
            player.sendMessage("Index: " + currentTree + ", Tree: " + treeTypes.get(currentTree).toString() + " success: " + success);
        }
        if (type == Material.RED_WOOL) {
            player.stopAllSounds();
        }

*/


    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlaced(BlockPlaceEvent event) {
        SkyblockSession skyblockSession = sessionRegistry.getSession(event.getBlock().getWorld());
        if (skyblockSession != null) {
            skyblockSession.onBlockPlaced(event);
            return;
        }

        Player player = event.getPlayer();
        if (!player.hasPermission(ADMIN_PERMISSION)) {
            event.setCancelled(true);
            Bukkit.getScheduler().runTask(plugin, player::updateInventory);
            return;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemDrop(BlockDropItemEvent event) {
        SkyblockSession skyblockSession = sessionRegistry.getSession(event.getBlock().getWorld());
        if (skyblockSession != null) {
            skyblockSession.onBlockDropItem(event);
            return;
        }
    }


    // TODO: Remove this!
    @EventHandler
    public void onSleepNight(TimeSkipEvent event) {
        if (event.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP) {
            plugin.getLogger().info("Time skipped: " + event.getSkipAmount());
        }
    }


}
