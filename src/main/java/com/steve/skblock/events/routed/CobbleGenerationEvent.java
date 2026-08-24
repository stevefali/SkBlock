package com.steve.skblock.events.routed;

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


    Logger logger;

    Plugin plugin;


    public CobbleGenerationEvent(Plugin plugin) {
        this.logger = plugin.getLogger();
        this.plugin = plugin;
    }

    @EventHandler
    public void onGenerateCobblestone(BlockFormEvent event) {
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
