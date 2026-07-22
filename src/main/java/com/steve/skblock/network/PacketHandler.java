package com.steve.skblock.network;

import com.steve.skblock.npc.NPCs;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

public class PacketHandler extends ChannelDuplexHandler {

    private final Player player;
    private Plugin plugin;
    private long lastCallTime = 0;
    private static final long COOLDOWN_MILLIS = 200;

    public PacketHandler(Player player, Plugin plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {

        if (msg instanceof ServerboundInteractPacket packet) {
            try {

                Field idField = ServerboundInteractPacket.class.getDeclaredField("b");
                idField.setAccessible(true);
                int id = (int) idField.get(packet);

                if (NPCs.npcIds.containsKey(id)) {
                    long now = System.currentTimeMillis();
                    if (now - lastCallTime >= COOLDOWN_MILLIS) {
                        lastCallTime = now;
                        System.out.println("Interact packet! EntityId: " + id);

                        Bukkit.getScheduler().runTask(plugin, () -> {
//                    ServerLevel level = ((CraftWorld) player.getWorld()).getHandle();
//                    System.out.println("Interact packet! IsAttack?: " + packet.isAttack());
                            NPCs.npcMap.get(NPCs.npcIds.get(id)).speak(player);

                        });
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.channelRead(context, msg);
    }


    @Override
    public void write(ChannelHandlerContext context, Object msg, ChannelPromise promise) throws Exception {

        super.write(context, msg, promise);
    }


}
