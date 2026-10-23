package com.steve.skblock.events.routed;

import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
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
    private static int currentSound = 0;

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

        if (type == Material.BLUE_WOOL) {
            currentSound++;
            if (currentSound > sounds.size() - 1) {
                currentSound = 0;
            }
        }
        if (type == Material.YELLOW_WOOL) {
            currentSound--;
            if (currentSound < 0) {
                currentSound = sounds.size() - 1;
            }
        }
        if (type == Material.GREEN_WOOL) {
            player.playSound(player.getLocation(), sounds.get(currentSound), 2.0f, 1.0f);
            player.sendMessage("Index: " + currentSound + ", Sound: " + sounds.get(currentSound).toString());
        }
        if (type == Material.RED_WOOL) {
            player.stopAllSounds();
        }


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




}
