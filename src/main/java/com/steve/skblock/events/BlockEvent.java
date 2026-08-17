package com.steve.skblock.events;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;

import com.steve.skblock.Skblock;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.*;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.*;

public class BlockEvent implements Listener {

    private Plugin plugin;
    private static final String BUNGEE_CHANNEL = "BungeeCord";


    public BlockEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {


        if (event.getBlock().getType() == Material.CRYING_OBSIDIAN) {
            for (String worldName : Skblock.getNpcIds().keySet()) {
                for (UUID uuid : Skblock.getNpcIds().get(worldName)) {
                    System.out.println(worldName + ": " + uuid);
                }
            }

            World world = event.getBlock().getWorld();
            List<Entity> ents = world.getEntities().stream().filter(entity -> entity.getType() == EntityType.ARMOR_STAND).toList();
            System.out.println("Armor stands: " + ents.size());
//            for (Entity ent : ents) {
//                System.out.println("location: " + ent.getLocation());
////                ent.remove();
//            }

//            Skblock.getNpcService().removeAllNpcsInWorld(world.getName());
//            if (Skblock.getNpcIds().get(world.getName()) != null) {
//                for (UUID uuid : Skblock.getNpcIds().get(world.getName())) {
//                    Skblock.getNpcIds().remove(world.getName());
//                }
//            }


        }

        if (event.getBlock().getType() == Material.BROWN_WOOL) {
            World world = event.getBlock().getWorld();
            List<Entity> ents = world.getEntities().stream().filter(entity -> entity.getType() == EntityType.ARMOR_STAND).toList();
            for (Entity ent : ents) {
                ent.remove();
            }
        }

        if (event.getBlock().getType() == Material.YELLOW_WOOL) {
            World world = event.getPlayer().getWorld();
            Skblock.getNpcService().removeAllNpcsInWorld(world.getName());
        }


    }


}
