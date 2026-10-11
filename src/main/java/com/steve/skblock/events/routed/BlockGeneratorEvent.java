package com.steve.skblock.events.routed;

import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;



public class BlockGeneratorEvent implements Listener {


    private Logger logger;
    private Plugin plugin;
    private final SessionRegistry sessionRegistry;


    public BlockGeneratorEvent(Plugin plugin, SessionRegistry sessionRegistry) {
        this.logger = plugin.getLogger();
        this.plugin = plugin;
        this.sessionRegistry = sessionRegistry;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockForm(BlockFormEvent event) {

        if (event.getNewState().getType() != Material.COBBLESTONE) {
            return;
        }

        SkyblockSession skyblockSession = sessionRegistry.getSession(event.getBlock().getWorld());
        if (skyblockSession != null) {
            skyblockSession.onGenerateBlock(event);
            return;
        }

//        if (event.getNewState().getType() == Material.COBBLESTONE) {
//            event.getNewState().setType(CobbleGenerator.determineBlock());
//        }


//        System.out.println("Block generated at: " + event.getBlock().getLocation().toVector());

    }

}
