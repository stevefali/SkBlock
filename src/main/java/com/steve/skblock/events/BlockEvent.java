package com.steve.skblock.events;

import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockEvent implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.STONE) {
            Player player = event.getPlayer();
            Location location = player.getLocation();

//            System.out.println("Pitch: " + location.getPitch());
//            System.out.println("Yaw: " + location.getYaw());

            /*ClientboundPlayerRotationPacket rotationPacket = new ClientboundPlayerRotationPacket(location.getYaw(), location.getPitch() + 90);

            CraftPlayer craftPlayer = (CraftPlayer) player;
            craftPlayer.getHandle().f.b(rotationPacket);*/
        }
        ;
    }

}
