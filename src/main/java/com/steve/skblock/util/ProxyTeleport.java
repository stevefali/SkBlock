package com.steve.skblock.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ProxyTeleport {

    private static final String BUNGEE_CHANNEL = "BungeeCord";

    public static final String LOBBY_SERVER = "lobby";

    public static void teleportPlayer(Plugin plugin, Player player, String serverName) {
        if (player != null) {
            player.sendMessage("Sending you to " + serverName + "...");

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(serverName);
            player.sendPluginMessage(plugin, BUNGEE_CHANNEL, out.toByteArray());
        }
    }
}
