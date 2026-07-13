package com.steve.skblock.events;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkLevel;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

public class BlockEvent implements Listener {

    private Plugin plugin;
    private static final String BUNGEE_CHANNEL = "BungeeCord";

    public BlockEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        /*if (event.getBlock().getType() == Material.STONE) {
            Player player = event.getPlayer();
            Location location = player.getLocation();

//            System.out.println("Pitch: " + location.getPitch());
//            System.out.println("Yaw: " + location.getYaw());

            *//*ClientboundPlayerRotationPacket rotationPacket = new ClientboundPlayerRotationPacket(location.getYaw(), location.getPitch() + 90);

            CraftPlayer craftPlayer = (CraftPlayer) player;
            craftPlayer.getHandle().f.b(rotationPacket);*//*
        }*/


//        if (event.getBlock().getType() == Material.CRYING_OBSIDIAN) {
//            ByteArrayDataOutput out = ByteStreams.newDataOutput();
//            out.writeUTF("Connect");
//            out.writeUTF("lobby");
//            event.getPlayer().sendPluginMessage(plugin, BUNGEE_CHANNEL, out.toByteArray());
//        }

        if (event.getBlock().getType() == Material.IRON_BLOCK) {
//            System.out.println("World: " + event.getPlayer().getWorld().getName());
            for (World world : Bukkit.getServer().getWorlds()) {
                System.out.println(world.getName());
            }
        }


        if (event.getBlock().getType() == Material.DIAMOND_BLOCK) {
//            System.out.println("World: " + event.getPlayer().getWorld().getName());

            CraftPlayer craftPlayer = (CraftPlayer) event.getPlayer();

            Component titleComponent = Component.literal("§9Preparing your island...");
            Component emptyComponent = Component.literal("");
            ClientboundSetTitleTextPacket titlePacket = new ClientboundSetTitleTextPacket(emptyComponent);
            ClientboundSetSubtitleTextPacket subtitleTextPacket = new ClientboundSetSubtitleTextPacket(titleComponent);

            ClientboundSetTitlesAnimationPacket animationPacket = new ClientboundSetTitlesAnimationPacket(7, 15, 7);

            craftPlayer.getHandle().connection.send(titlePacket);
            craftPlayer.getHandle().connection.send(subtitleTextPacket);
            craftPlayer.getHandle().connection.send(animationPacket);

            ClientboundSetActionBarTextPacket actionBarTextPacket = new ClientboundSetActionBarTextPacket(titleComponent);
            ClientboundSetActionBarTextPacket emptyActionbarPacket = new ClientboundSetActionBarTextPacket(emptyComponent);

            craftPlayer.getHandle().connection.send(actionBarTextPacket);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                craftPlayer.getHandle().connection.send((emptyActionbarPacket));
            }, 20L);


        }

    }

}
