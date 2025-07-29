package com.steve.skblock.events;

import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvent implements Listener {

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        CraftPlayer player = (CraftPlayer) event.getPlayer();


//        player.sendMessage("Welcome to Skyblock!!");


        ClientboundSetTitleTextPacket titlePack = new ClientboundSetTitleTextPacket(IChatBaseComponent.a("§6Welcome to your Island!"));

        ClientboundSetTitlesAnimationPacket titlesAnimationPacket = new ClientboundSetTitlesAnimationPacket(7, 33, 7);

        player.getHandle().f.b(titlePack);
        player.getHandle().f.b(titlesAnimationPacket);
    }
}
