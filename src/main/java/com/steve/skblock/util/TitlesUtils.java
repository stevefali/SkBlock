package com.steve.skblock.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitlesUtils {

    public static void sendTitle(Player player, String titleText, int fadeIn, int stay, int fadeOut) {
        ServerGamePacketListenerImpl connection = getConnection(player);

        connection.send(new ClientboundSetTitleTextPacket(Component.literal(titleText)));
        connection.send(new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));
    }

    public static void sendSubtitle(Player player, String subtitleText, int fadeIn, int stay, int fadeOut) {
        ServerGamePacketListenerImpl connection = getConnection(player);

        connection.send(new ClientboundSetTitleTextPacket(Component.empty()));
        connection.send(new ClientboundSetSubtitleTextPacket(Component.literal(subtitleText)));
        connection.send(new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));
    }

    public static void sendTitleAndSubtitle(Player player, String titleText, String subtitleText, int fadeIn, int stay, int fadeOut) {
        ServerGamePacketListenerImpl connection = getConnection(player);

        connection.send(new ClientboundSetTitleTextPacket(Component.literal(titleText)));
        connection.send(new ClientboundSetSubtitleTextPacket(Component.literal(subtitleText)));
        connection.send(new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));
    }

    private static ServerGamePacketListenerImpl getConnection(Player player) {
        return ((CraftPlayer) player).getHandle().connection;
    }

}
