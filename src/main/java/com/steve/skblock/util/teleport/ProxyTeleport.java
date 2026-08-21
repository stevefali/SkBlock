package com.steve.skblock.util.teleport;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.steve.skblock.util.TitlesUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ProxyTeleport {

    private static final String BUNGEE_CHANNEL = "BungeeCord";

    public static final String LOBBY_SERVER = "lobby";

    public static void teleportPlayer(Plugin plugin, Player player, String serverName) {
        if (player != null) {
            TitlesUtils.sendSubtitle(player, "Sending you to main lobby...", 5, 30, 5);

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(serverName);
            player.sendPluginMessage(plugin, BUNGEE_CHANNEL, out.toByteArray());
        }
    }
}
