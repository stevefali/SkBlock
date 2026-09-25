package com.steve.skblock.events.routed;

import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import com.steve.skblock.util.CobbleGenerator;
//import net.minecraft.network.chat.Component;
import org.bukkit.Material;
//import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.plugin.Plugin;


import java.util.logging.Logger;


public class CobbleGenerationEvent implements Listener {


    private Logger logger;
    private Plugin plugin;
    private final SessionRegistry sessionRegistry;


    public CobbleGenerationEvent(Plugin plugin, SessionRegistry sessionRegistry) {
        this.logger = plugin.getLogger();
        this.plugin = plugin;
        this.sessionRegistry = sessionRegistry;
    }

    @EventHandler(ignoreCancelled = true)
    public void onGenerateCobblestone(BlockFormEvent event) {

        SkyblockSession skyblockSession = sessionRegistry.getSession(event.getBlock().getWorld());
        if (skyblockSession != null) {
            skyblockSession.onGenerateCobble(event);
            return;
        }

        if (event.getNewState().getType() == Material.COBBLESTONE) {
            event.getNewState().setType(CobbleGenerator.determineBlock());
        }


//        System.out.println("Block generated at: " + event.getBlock().getLocation().toVector());

        /* ClientboundSetTitleTextPacket titlePack = new ClientboundSetTitleTextPacket(IChatBaseComponent.a("§6YOOO MAAAANNNN"));

        ClientboundSetTitlesAnimationPacket titlesAnimationPacket = new ClientboundSetTitlesAnimationPacket(7, 33, 7);

        List<Player> players = event.getBlock().getWorld().getPlayersSeeingChunk(event.getBlock().getChunk()).stream().toList();

        for (Player player : players) {
            CraftPlayer craftPlayer = (CraftPlayer) player;
            craftPlayer.getHandle().f.b(titlePack);
            craftPlayer.getHandle().f.b(titlesAnimationPacket);
        }*/
    }

}
