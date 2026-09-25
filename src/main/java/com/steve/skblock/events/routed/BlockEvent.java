package com.steve.skblock.events.routed;

import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;


public class BlockEvent implements Listener {

    private final Plugin plugin;
    private final SessionRegistry sessionRegistry;


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

    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlaced(BlockPlaceEvent event) {
        SkyblockSession skyblockSession = sessionRegistry.getSession(event.getBlock().getWorld());
        if (skyblockSession != null) {
            skyblockSession.onBlockPlaced(event);
        }
    }


}
