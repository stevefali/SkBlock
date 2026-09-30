package com.steve.skblock.events.routed;

import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;


public class BlockEvent implements Listener {

    private final Plugin plugin;
    private final SessionRegistry sessionRegistry;

    private BossBar bossBar =Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);

    public BlockEvent(Plugin plugin, SessionRegistry sessionRegistry) {
        this.plugin = plugin;
        this.sessionRegistry = sessionRegistry;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {

        SkyblockSession skyblockSession = sessionRegistry.getSession(event.getBlock().getWorld());
        if (skyblockSession != null) {
            skyblockSession.onBlockBreak(event);
            return;
        }

        Material type = event.getBlock().getType();
        Player player = event.getPlayer();

//        TODO: Remove this!!
        if (type == Material.BLACK_WOOL) {
            player.sendMessage("You broke that in the lobby!");

        }

        if (type == Material.BLUE_WOOL) {
            player.sendMessage(player.getWorld().getBiome(player.getLocation()).toString());
        }

       /* if (type == Material.GOLD_BLOCK) {
            bossBar.addPlayer(player);
        }
        if (type == Material.BLUE_WOOL) {
            bossBar.setTitle("Test Boss bar title");
        }
        if (type == Material.YELLOW_WOOL) {
            bossBar.setTitle("§aGreen words?");
        }
        if (type == Material.LIGHT_BLUE_WOOL) {
            bossBar.setTitle("§lBold words?");
        }
        if (type == Material.GREEN_WOOL) {
            bossBar.setProgress((double) 0 / 1);
        }
        if (type == Material.GRAY_WOOL) {
            bossBar.setProgress((double) 3 / 5);
        }
        if (type == Material.WHITE_WOOL) {
            bossBar.setProgress((double) 5 / 5);
        }*/

    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlaced(BlockPlaceEvent event) {
        SkyblockSession skyblockSession = sessionRegistry.getSession(event.getBlock().getWorld());
        if (skyblockSession != null) {
            skyblockSession.onBlockPlaced(event);
        }
    }


}
