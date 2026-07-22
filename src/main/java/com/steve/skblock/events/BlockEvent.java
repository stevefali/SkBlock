package com.steve.skblock.events;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.steve.skblock.npc.NPC;
import com.steve.skblock.npc.NPCs;
import com.steve.skblock.npc.NpcSkin;
import com.steve.skblock.npc.NpcSkinDataAccess;
import io.netty.channel.Channel;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.network.Connection;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.*;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import sun.misc.Unsafe;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class BlockEvent implements Listener {

    private Plugin plugin;
    private static final String BUNGEE_CHANNEL = "BungeeCord";


    private static int randysId;

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


        if (event.getBlock().getType() == Material.CRYING_OBSIDIAN) {

            Location location = event.getBlock().getState().getLocation();
            World world = event.getBlock().getWorld();

            MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();

            ServerLevel serverLevel = ((CraftWorld) world).getHandle();

            ServerPlayer viewer = ((CraftPlayer) event.getPlayer()).getHandle();

//            GameProfile profile = new GameProfile(UUID.fromString("ac5a510c-85d1-4d76-adcb-1dea80bdbe1e"), "LegendOfLink469");
//            GameProfile profile = new GameProfile(UUID.fromString("ac5a510c-85d1-4d76-adcb-1dea80bdbe1f"), "Randy");
//            GameProfile profile = new GameProfile(UUID.fromString("38e56fef-8864-470d-afa4-359119973e2a"), "Randy");
            UUID uuid = UUID.randomUUID();
            GameProfile profile = new GameProfile(uuid, "Randy");

//
//            String texture = "ewogICJ0aW1lc3RhbXAiIDogMTc4NDMyOTc0MjIxMywKICAicHJvZmlsZUlkIiA6ICIzOGU1NmZlZjg4NjQ0NzBkYWZhNDM1OTExOTk3M2UyYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJzdGV2ZWZhbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hZjY2NGVlOGIwMzhlZTJlYzBhMTE2YTRiODVkYjZjOTFlYWRkOGRmNDJlOTU3YzFlZTg1ZjE1YTNjMDAzZDUyIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
//            String signature = "Ih3pYUQTxPHoQjKsTrcXceDF3nCvduGfMPiRQw0Cq96bDXoDFH2ZyTPr66ZGUNVhCBCgsDtaSDl6/PU6l8/+ZOsiAHu8giP5ngtHqgxrUauz7eDGt9FcNtUU55aPLesXTWQkbD5Qfl794BUDTA535FSyvaV0lPQ3Cf0RTGedTb/4JoCc5/SZnwbk2qk/sHr87EGrpPuHXuVmzoZOIeoJdB54cuL16Ht/SilaKfktTsaT5zYY5gQqQKUIVGSUqGdb+yuwk4Lj2nxyLEfq/IH7U/BWUk3pSoqT4ukGaRLzZo1TgpBhLEf40MXCzMfKaz7rXoZ94yxeGbhlRebaFkXFlIccnUrSSgduDwePuGF8GTTnbYauge+78I03+CQsYg4XnbNiOmRlZgaT8Y6KyZPG4/XB9FhzKOfkBVYpAzkzl/TjDJXlUpJp6wYur3UViPozp86jb+kqGfwpA2wviAUMdGMrXCeMR/qavckH51t4tppy0W48SvvSVItJJPIQzgwIIdVe/eDhdwIx/wjqs6WUghamYU7WfTDYC+4LNOmLL8hG276NJ/PJqlr5W+xBl5n+Oy9H6Ik3p27mVnwnXhuv1wKu01DD+TVZbVKBzC9sxaQsuQgyFhFw9klmPHjBfDZrsA3+mltIS4LUlgnawcIRATxcWkyz16XIubtzK405dDE=";
//
//            profile.getProperties().put("textures", new Property("textures", texture, signature));


            NPC randy = new NPC(
                    minecraftServer,
                    serverLevel,
                    profile,
                    ClientInformation.createDefault()
            );

            randy.setSpeakingMessage("Hey there! I'm Randy.");



            NPCs.npcMap.put("Randy_skyblock_lobby", randy);

            randysId = randy.getId();
            System.out.println("Randy id: " + randysId);

//            ServerPlayer alsoRandy = (ServerPlayer) serverLevel.getPlayerByUUID(uuid);
//
//            System.out.println("Also Randy's id: " + alsoRandy.getId());


            randy.setPos(location.getX(), location.getY(), location.getZ());
            randy.setXRot(0);
            randy.setYRot(0);

            System.out.println(randy.level().getWorld().getName());
            System.out.println(randy.displayName);
            System.out.println(randy.getUUID());

            NpcSkinDataAccess.load(plugin)
                    .exceptionally(throwable -> {
                        throwable.printStackTrace();
                        return new HashMap<>();
                    })
                    .thenAccept(skinDataResult -> {
                        NpcSkin randySkin = skinDataResult.get("Randy");
                        profile.getProperties().put("textures", new Property("textures", randySkin.texture(), randySkin.signature()));

                        SynchedEntityData randyData = randy.getEntityData();
                        SynchedEntityData.DataValue outerLayersValue = new SynchedEntityData.DataValue<>(17, EntityDataSerializers.BYTE, (byte) 0x7F);

                        List<SynchedEntityData.DataValue<?>> dataValuesCopy = new ArrayList<>();
                        dataValuesCopy.addAll(randyData.getNonDefaultValues());
                        dataValuesCopy.add(outerLayersValue);

                        ServerGamePacketListenerImpl connection = viewer.connection;

                        try {
                            EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions = EnumSet.of(
                                    ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
                                    ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED,
                                    ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY
                            );

                            ClientboundPlayerInfoUpdatePacket.Entry entry = new ClientboundPlayerInfoUpdatePacket.Entry(
                                    randy.getUUID(),
                                    randy.getGameProfile(),
                                    true,
                                    0,
                                    GameType.SURVIVAL,
                                    null,
                                    true,
                                    0,
                                    null
                            );

                            ClientboundPlayerInfoUpdatePacket infoPack = makePlayerInfoUpdatePacket(actions, List.of(entry));

                            connection.send(infoPack);

                            connection.send(new ClientboundAddEntityPacket(randy, 0, randy.blockPosition()));

                            ClientboundSetEntityDataPacket dataPacket = new ClientboundSetEntityDataPacket(randysId, dataValuesCopy);
                            connection.send(dataPacket);

//                System.out.println("Should have sent packets");

                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                connection.send(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(randy.getUUID())));
                            }, 20L);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });


            //            ClientboundPlayerInfoUpdatePacket infoPack = ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(npcCollection);
//            ClientboundPlayerInfoUpdatePacket infoPack = ClientboundPlayerInfoUpdatePacket.createSinglePlayerInitializing(randy, false);

//            if (infoPack != null) {
//                System.out.println("It's not null!");
//            }
//            if (infoPack.isReady()) {
//                System.out.println("It's ready");
//            }


        }

        if (event.getBlock().getType() == Material.ORANGE_WOOL) {

            if (randysId != 0) {
                ServerGamePacketListenerImpl connection = ((CraftPlayer) event.getPlayer()).getHandle().connection;

                ClientboundRemoveEntitiesPacket removeEntitiesPacket = new ClientboundRemoveEntitiesPacket(randysId);

                connection.send(removeEntitiesPacket);

                NPCs.npcMap.remove("Randy_skyblock_lobby");
            } else {
                System.out.println("No valid id found");
            }

        }


        if (event.getBlock().getType() == Material.IRON_BLOCK) {
//            System.out.println("World: " + event.getPlayer().getWorld().getName());
//            for (World world : Bukkit.getServer().getWorlds()) {
//                System.out.println(world.getName());
//            }

//            File directory = new File("./");
//            for (File file : directory.listFiles()) {
//                System.out.println(file.getName());
//            }

            try {
                System.out.println("Randy's name from npcIds: " + NPCs.npcIds.get(NPCs.npcMap.get("Randy_skyblock_lobby").getId()) );
                System.out.println(NPCs.npcMap.get("Randy_skyblock_lobby").getUUID());
            } catch (Exception e) {
                System.out.println("Can't find NPC with that ID");
            }

        }


        if (event.getBlock().getType() == Material.DIAMOND_BLOCK) {

            Location location = event.getBlock().getState().getLocation();

            LivingEntity randy = (LivingEntity) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
            randy.setAI(false);
//            randy.setInvulnerable(true);
            randy.setSilent(true);
            randy.setCollidable(false);

//            randy.customName((net.kyori.adventure.text.Component) Component.literal("§7Randy"));
            randy.setCustomNameVisible(true);
            randy.setRemoveWhenFarAway(false);

            ((CraftEntity) randy).setCustomName("§aRandy");
            ((CraftEntity) randy).setInvulnerable(true);

            if (randy.getAttribute(Attribute.KNOCKBACK_RESISTANCE) != null) {
                randy.getAttribute(Attribute.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
            }





            /*CraftPlayer craftPlayer = (CraftPlayer) event.getPlayer();

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
            }, 20L);*/


        }

    }

    private ClientboundPlayerInfoUpdatePacket makePlayerInfoUpdatePacket(
            EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions,
            List<ClientboundPlayerInfoUpdatePacket.Entry> entries
    ) throws Exception {

        Unsafe unsafe = getUnsafe();
        ClientboundPlayerInfoUpdatePacket packet =
                (ClientboundPlayerInfoUpdatePacket) unsafe.allocateInstance(ClientboundPlayerInfoUpdatePacket.class);

        Field actionsField = null;
        Field entriesField = null;
        for (Field field : ClientboundPlayerInfoUpdatePacket.class.getDeclaredFields()) {
            if (field.getType() == EnumSet.class) {
                actionsField = field;
            }
            if (field.getType() == List.class) {
                entriesField = field;
            }
        }

        actionsField.setAccessible(true);
        entriesField.setAccessible(true);

        actionsField.set(packet, actions);
        entriesField.set(packet, entries);

        return packet;
    }

    private static Unsafe getUnsafe() throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return (Unsafe) field.get(null);
    }

}
