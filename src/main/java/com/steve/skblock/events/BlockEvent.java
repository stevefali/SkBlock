package com.steve.skblock.events;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import com.steve.skblock.npc.NPC;
import com.steve.skblock.npc.NPCs;
import com.steve.skblock.npc.NpcSkin;
import com.steve.skblock.npc.NpcSkinDataAccess;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.*;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
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
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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

    private static final NpcSkin DEFAULT_SKIN = new NpcSkin(
            "ewogICJ0aW1lc3RhbXAiIDogMTc4NDI2OTI2NTE1NywKICAicHJvZmlsZUlkIiA6ICIzOGU1NmZlZjg4NjQ0NzBkYWZhNDM1OTExOTk3M2UyYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJzdGV2ZWZhbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xYTRhZjcxODQ1NWQ0YWFiNTI4ZTdhNjFmODZmYTI1ZTZhMzY5ZDE3NjhkY2IxM2Y3ZGYzMTlhNzEzZWI4MTBiIgogICAgfQogIH0KfQ==",
            "EMn77YzJ/Du0dg9EnataOVERmCsUsIs1ImdzaQehe537vZYycY1WXlLdW4zBNk8Hep+FJPXWMaXNal84ZZk/2OhtDzT9OnWN1JDGzDG3qvLF7rgmGNifIQh6CUlNmjGajkwlhwcSfL+RZfET+IsQ/MeoyikzACb7SYR+hwtIHWEEl84rxLzYBj9gqTfJZfUleFHBAKE04w2Ld6WYGUuO9ZvEauqemD4C8K3WxJOtxQjkRaJHUJ7JOH74Ua9Egb0PcK+jblMd/CKhyBShh86HVdaTwwoClO956DQ98d23J7i+zSOKjBDRkaXXoX2vW/Yl+WJb95p8ABwdE+151fLPWmnu6LhtiXGdxHms6aEvRVD/fLMatpb3FkU0+r8T3SpYbVN03jF8s44eq78AhoSMHHuGgKSnjP7j+KfbhiwgGPL51eW3N6GGnIiE8ADECdjc9safP59w/wRK0PJS3jph02zqtIM1imeu/sY5xouQHdnjR8l0tG5T4lUjuEHKK4uNYaqQfEwVJHYdV7GRAjc58Wm6fRNrfa270mxFJ9xz0mhHpbIhI4mSyw1htcWftBYcm4Vfy4+7B/Ta2JSqZr/L2hGLIQGt5ipSITfa6pBynlyyCOwJqAzxTThrlBOz2A7R1THgaqBTUTl4UwLKB5uOLBa4W3qn1UHQL6Q8l1P2y0I="
    );

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
            GameProfile profile = new GameProfile(uuid, "§aRandy");

//
//            String texture = "ewogICJ0aW1lc3RhbXAiIDogMTc4NDMyOTc0MjIxMywKICAicHJvZmlsZUlkIiA6ICIzOGU1NmZlZjg4NjQ0NzBkYWZhNDM1OTExOTk3M2UyYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJzdGV2ZWZhbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hZjY2NGVlOGIwMzhlZTJlYzBhMTE2YTRiODVkYjZjOTFlYWRkOGRmNDJlOTU3YzFlZTg1ZjE1YTNjMDAzZDUyIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
//            String signature = "Ih3pYUQTxPHoQjKsTrcXceDF3nCvduGfMPiRQw0Cq96bDXoDFH2ZyTPr66ZGUNVhCBCgsDtaSDl6/PU6l8/+ZOsiAHu8giP5ngtHqgxrUauz7eDGt9FcNtUU55aPLesXTWQkbD5Qfl794BUDTA535FSyvaV0lPQ3Cf0RTGedTb/4JoCc5/SZnwbk2qk/sHr87EGrpPuHXuVmzoZOIeoJdB54cuL16Ht/SilaKfktTsaT5zYY5gQqQKUIVGSUqGdb+yuwk4Lj2nxyLEfq/IH7U/BWUk3pSoqT4ukGaRLzZo1TgpBhLEf40MXCzMfKaz7rXoZ94yxeGbhlRebaFkXFlIccnUrSSgduDwePuGF8GTTnbYauge+78I03+CQsYg4XnbNiOmRlZgaT8Y6KyZPG4/XB9FhzKOfkBVYpAzkzl/TjDJXlUpJp6wYur3UViPozp86jb+kqGfwpA2wviAUMdGMrXCeMR/qavckH51t4tppy0W48SvvSVItJJPIQzgwIIdVe/eDhdwIx/wjqs6WUghamYU7WfTDYC+4LNOmLL8hG276NJ/PJqlr5W+xBl5n+Oy9H6Ik3p27mVnwnXhuv1wKu01DD+TVZbVKBzC9sxaQsuQgyFhFw9klmPHjBfDZrsA3+mltIS4LUlgnawcIRATxcWkyz16XIubtzK405dDE=";
//
//            profile.getProperties().put("textures", new Property("textures", texture, signature));


            NPC randy = new NPC(
                    minecraftServer,
                    serverLevel,
                    profile,
                    ClientInformation.createDefault(),
//                    player -> player.sendMessage("Ouch! That hurt!")
                    null
            );

            randy.setSpeakingMessage("Hey there! I'm Randy.");


            NPCs.npcMap.put("Randy_skyblock_lobby", randy);

            randysId = randy.getId();
            System.out.println("Randy id: " + randysId);

//            ServerPlayer alsoRandy = (ServerPlayer) serverLevel.getPlayerByUUID(uuid);
//
//            System.out.println("Also Randy's id: " + alsoRandy.getId());


            randy.setPos(location.getX(), location.getY(), location.getZ());
//            randy.setXRot(-15);
//            randy.setYRot(220);
//            randy.setYHeadRot(220);


//            ItemStack nmsStack = ((CraftItemStack) bukkitStack)
//
//            Item item = Items.DIAMOND_PICKAXE;
//            ItemStack randysItemStack = new ItemStack(item, 1);

//            randy.setItemInHand(InteractionHand.MAIN_HAND, randysItemStack);

            System.out.println(randy.level().getWorld().getName());
            System.out.println(randy.displayName);
            System.out.println(randy.getUUID());

            NpcSkinDataAccess.load(plugin)
                    .exceptionally(throwable -> {
                        throwable.printStackTrace();
                        return new HashMap<>();
                    })
                    .thenAccept(skinDataResult -> {
                        NpcSkin randySkin = (skinDataResult.get("Randy") != null)
                                ? skinDataResult.get("Randy")
                                : DEFAULT_SKIN;
                        profile.getProperties().put("textures", new Property("textures", randySkin.texture(), randySkin.signature()));

                        SynchedEntityData randyData = randy.getEntityData();
                        SynchedEntityData.DataValue<Byte> outerLayersValue = new SynchedEntityData.DataValue<>(17, EntityDataSerializers.BYTE, (byte) 0x7F);

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

//                            connection.send(new ClientboundRotateHeadPacket(randy, (byte) (randy.getYHeadRot() * 256 / 360)));

                            ClientboundSetEntityDataPacket dataPacket = new ClientboundSetEntityDataPacket(randysId, dataValuesCopy);
                            connection.send(dataPacket);

                            /*org.bukkit.inventory.ItemStack bukkitStack = new org.bukkit.inventory.ItemStack(Material.GRASS_BLOCK);
                            ItemStack nmsStack = CraftItemStack.asNMSCopy(bukkitStack);
                            List<Pair<EquipmentSlot, ItemStack>> equipmentList = new ArrayList<>();

                            net.minecraft.world.entity.EquipmentSlot.MAINHAND;

                            EquipmentSlot.HAND.


                            equipmentList.add(new Pair<>(EquipmentSlot.HAND., nmsStack));


                            connection.send(new ClientboundSetEquipmentPacket(randysId, equipmentList));
*/

//                System.out.println("Should have sent packets");

                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                connection.send(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(randy.getUUID())));
                            }, 20L);


//                            connection.send(new ClientboundRotateHeadPacket(randy, (byte) ((randy.getYRot() % 360) * 256 / 360)));
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


                NPCs.npcMap.remove("Randy_skyblock_lobby");

                ServerGamePacketListenerImpl connection = ((CraftPlayer) event.getPlayer()).getHandle().connection;

                ClientboundRemoveEntitiesPacket removeEntitiesPacket = new ClientboundRemoveEntitiesPacket(randysId);

                connection.send(removeEntitiesPacket);
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
                ServerPlayer serverPlayer = ((CraftPlayer) event.getPlayer()).getHandle();
                ServerGamePacketListenerImpl connection = serverPlayer.connection;

                EquipmentSlot bukkitHand = EquipmentSlot.HAND;

                net.minecraft.world.entity.EquipmentSlot convertedHand = CraftEquipmentSlot.getNMS(bukkitHand);

                org.bukkit.inventory.ItemStack bukkitStack = new org.bukkit.inventory.ItemStack(Material.GRASS_BLOCK);
                ItemStack nmsStack = CraftItemStack.asNMSCopy(bukkitStack);

                List<Pair<net.minecraft.world.entity.EquipmentSlot, ItemStack>> equipmentList =
                        NPCs.npcMap.get("Randy_skyblock_lobby").getEquipmentList();

                equipmentList.remove(new Pair<>(convertedHand, nmsStack));


            } catch (Exception e) {
                System.out.println("Can't find NPC with that ID");
            }

        }


        if (event.getBlock().getType() == Material.DIAMOND_BLOCK) {



            /*EquipmentSlot[] allBukkitSlots = EquipmentSlot.values();

            net.minecraft.world.entity.EquipmentSlot nmsHand = net.minecraft.world.entity.EquipmentSlot.MAINHAND;
            net.minecraft.world.entity.EquipmentSlot[] allNmsSlots = net.minecraft.world.entity.EquipmentSlot.values();

            System.out.println("---------------");
            System.out.println("Bukkit hand slot: " + bukkitHand.name() + ", " + bukkitHand.toString());
            System.out.println("Bukkit slots: ");
            for (var slot : allBukkitSlots) {
                System.out.println(slot.name() + ", " + slot.toString());
            }
            System.out.println("---------------");
            System.out.println("NMS hand slot: " + nmsHand.name() + ", " + nmsHand.getName() + ", " + nmsHand.toString());
            System.out.println("NMS slots: ");
            for (var slot : allNmsSlots) {
                System.out.println(slot.name() + ", " + slot.getName() + ", " + slot.toString());
            }
            System.out.println("---------------");*/

            try {
               /* ServerPlayer serverPlayer = ((CraftPlayer) event.getPlayer()).getHandle();
                ServerGamePacketListenerImpl connection = serverPlayer.connection;

                EquipmentSlot bukkitHand = EquipmentSlot.HAND;

                net.minecraft.world.entity.EquipmentSlot convertedHand = CraftEquipmentSlot.getNMS(bukkitHand);

                org.bukkit.inventory.ItemStack bukkitStack = new org.bukkit.inventory.ItemStack(Material.GRASS_BLOCK);
                ItemStack nmsStack = CraftItemStack.asNMSCopy(bukkitStack);

                List<Pair<net.minecraft.world.entity.EquipmentSlot, ItemStack>> equipmentList =
                        NPCs.npcMap.get("Randy_skyblock_lobby").getEquipmentList();
                equipmentList.add(new Pair<>(convertedHand, nmsStack));



//                List<Pair<net.minecraft.world.entity.EquipmentSlot, ItemStack>> emptyList = new ArrayList<>();
//                emptyList.add(new Pair<>(net.minecraft.world.entity.EquipmentSlot.HEAD, new ItemStack(Items.NETHERITE_HELMET)));

//            connection.send(new ClientboundSetEquipmentPacket(randysId, equipmentList));
                connection.send(new ClientboundSetEquipmentPacket(
                        randysId,
                        NPCs.npcMap.get("Randy_skyblock_lobby").getEquipmentList()
                ));
*/

                NPC rand = NPCs.npcMap.get("Randy_skyblock_lobby");
                rand.equipItem(
                        net.minecraft.world.entity.EquipmentSlot.MAINHAND,
                        new ItemStack(Items.GOLD_BLOCK)
                );

                ServerPlayer serverPlayer = ((CraftPlayer) event.getPlayer()).getHandle();
                ServerGamePacketListenerImpl connection = serverPlayer.connection;
                connection.send(new ClientboundSetEquipmentPacket(rand.getId(), rand.getEquipmentList()));


            } catch (Exception e) {
                System.out.println("Did you forget to spawn NPC first??");
            }






            /*Location location = event.getBlock().getState().getLocation();

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
            }*/





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


        if (event.getBlock().getType() == Material.YELLOW_WOOL) {
            NPC randi = NPCs.npcMap.get("Randy_skyblock_lobby");
            randi.equipItem(
                    net.minecraft.world.entity.EquipmentSlot.MAINHAND,
                    ItemStack.EMPTY
            );

            ServerPlayer serverPlayer = ((CraftPlayer) event.getPlayer()).getHandle();
            ServerGamePacketListenerImpl connection = serverPlayer.connection;
            connection.send(new ClientboundSetEquipmentPacket(randi.getId(), randi.getEquipmentList()));
        }


        if (event.getBlock().getType() == Material.PURPLE_WOOL) {

            NPC npc = NPCs.npcMap.get("Randy_skyblock_lobby");

//            npc.setPos(-1.5, 65, -1.5);
//            npc.setPosRaw(-1.5, 65, -1.5);
//            double x = (double) Mth.lfloor(-1.5 * (double)4096.0F) * 2.44140625E-4;
//            double y = (double) Mth.lfloor(-1.5 * (double)4096.0F) * 2.44140625E-4;

//            npc.setPos(x, 65.0, y);
//            npc.noPhysics = true;
            npc.setPos(-1.0, 65, -1.5);

            System.out.println(npc.position());

//            npc.setXRot(-15);
            npc.setYRot(0);
            npc.setYHeadRot(0);

            showNpcTo(npc.getId(), event.getPlayer());

//            short px = (short) (0.5 * 4096);
//            short py = (short) (0);
//            short pz = (short) (0.5 * 4096);
//
////            byte ry = (byte) ((npc.getYRot() * 256.0F) / 360.0F);
////            byte rx = (byte) ((npc.getXRot() * 256.0F) / 360.0F);
////            byte ry = (byte) ((npc.getYRot() % 360) * 256 / 360);
////            byte rx = (byte) ((npc.getXRot() % 360) * 256 / 360);
//            byte ry = (byte) npc.getYRot();
//            byte rx = (byte) npc.getXRot();
//

////            connection.send(new ClientboundMoveEntityPacket.PosRot(npc.getId(), px, py, pz, ry, rx, true));
//
//            connection.send(new ClientboundMoveEntityPacket.Pos(npc.getId(), px, py, pz, true));
//
//            var rotPack = new ClientboundMoveEntityPacket.Rot(npc.getId(), ry, rx, true);
//            connection.send(rotPack);
////            connection.send(new ClientboundRotateHeadPacket(npc, (byte) ((npc.getYRot() % 360) * 256 / 360)));
////            connection.send(new ClientboundRotateHeadPacket(npc, (byte) ((npc.getYRot() * 256.0F) / 360.0F)));
////            connection.send(new ClientboundRotateHeadPacket(npc, (byte) npc.getYRot()));
////            connection.send(new ClientboundRotateHeadPacket(npc, (byte) npc.getYHeadRot()));
//            var headPack = new ClientboundRotateHeadPacket(npc, ry);
//            connection.send(headPack);
//
//            System.out.println("NPC: " + npc.getYRot() + ", " + npc.getYHeadRot());
//            System.out.println("NPC: " + (byte) npc.getYRot() + ", " + (byte) npc.getYHeadRot());
//            System.out.println("Packs: " + rotPack.getyRot() + ", " + headPack.getYHeadRot() );
//            System.out.println("Packs: " + (byte) rotPack.getyRot() + ", " + (byte) headPack.getYHeadRot() );


//            PositionMoveRotation positionMoveRotation = new PositionMoveRotation(npc.position(), Vec3.ZERO, npc.getYRot(), npc.getXRot());
//
//            ClientboundTeleportEntityPacket teleportPack = new ClientboundTeleportEntityPacket(
//                    npc.getId(),
//                    positionMoveRotation,
//                    Collections.emptySet(),
//                    true
//            );
//
//            ServerPlayer serverPlayer = ((CraftPlayer) event.getPlayer()).getHandle();
//            ServerGamePacketListenerImpl connection = serverPlayer.connection;
//            connection.send(teleportPack);



            /*NPC randee = NPCs.npcMap.get("Randy_skyblock_lobby");
            randee.setXRot(-15);
            randee.setYRot(100);
            randee.setYHeadRot(100);

//            randee.setPos(randee.position().add(3, 0, 3));

            ServerPlayer serverPlayer = ((CraftPlayer) event.getPlayer()).getHandle();
            ServerGamePacketListenerImpl connection = serverPlayer.connection;
//            connection.send(new ClientboundPlayerRotationPacket(randee.getYRot(), randee.getXRot()));
//            Vec3 newPos = new Vec3(3, 65, 3);
            short px = (short) (randee.getX() * 4096);
            short py = (short) (randee.getY() * 4096);
            short pz = (short) (randee.getZ() * 4096);
//
//            byte ry = (byte) ((randee.getYRot() * 256.0F) / 360.0F);
//            byte rx = (byte) ((randee.getXRot() * 256.0F) / 360.0F);



//            Location nwLocation = new Location(randee.level().getWorld(), 1, 65, 1);
            randee.setPos(1, 65, 1);


//            connection.send(new ClientboundAddEntityPacket(randee, 0, randee.blockPosition()));

            byte yRot = (byte) randee.getYRot();
            byte xRot = (byte) randee.getXRot();
            connection.send(new ClientboundMoveEntityPacket.PosRot(
                    randee.getId(),
                    px,
                    py,
                    pz,
                    yRot,
                    xRot,
                    true
            ));


//            connection.send(new ClientboundMoveEntityPacket.Pos(
//                    randee.getId(),
//                    (short) (randee.getX() + 3),
//                    (short) randee.yya,
//                    (short) (randee.zza + 3),
//                    false
//            ));
            connection.send(new ClientboundRotateHeadPacket(randee, (byte) ((randee.getYRot() % 360) * 256 / 360)));


             */
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


    public void showNpcTo(int npcId, Player viewer) {
        NPC npc = NPCs.npcMap.get(NPCs.npcIds.get(npcId));
        if (npc == null) {
            System.err.println("Error sending packets to show NPC: NPC is null");
            return;
        }
        try {

            List<SynchedEntityData.DataValue<?>> dataValuesCopy = new ArrayList<>(npc.getEntityData().getNonDefaultValues());
            SynchedEntityData.DataValue<Byte> outerSkinLayersValue =
                    new SynchedEntityData.DataValue<>(17, EntityDataSerializers.BYTE, (byte) 0x7F);
            dataValuesCopy.add(outerSkinLayersValue);

            EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions = EnumSet.of(
                    ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
                    ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED,
                    ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY
            );

            ClientboundPlayerInfoUpdatePacket.Entry entry = new ClientboundPlayerInfoUpdatePacket.Entry(
                    npc.getUUID(),
                    npc.getGameProfile(),
                    true,
                    0,
                    GameType.SURVIVAL,
                    null,
                    true,
                    0,
                    null
            );

            ServerPlayer serverPlayer = ((CraftPlayer) viewer).getHandle();
            ServerGamePacketListenerImpl connection = serverPlayer.connection;

            ClientboundPlayerInfoUpdatePacket infoUpdatePacket = makePlayerInfoUpdatePacket(actions, List.of(entry));

            connection.send(infoUpdatePacket);
            connection.send(new ClientboundAddEntityPacket(npc, 0, npc.blockPosition()));
            connection.send(new ClientboundSetEntityDataPacket(npcId, dataValuesCopy));

            if (!npc.getEquipmentList().isEmpty()) {
                connection.send(new ClientboundSetEquipmentPacket(npcId, npc.getEquipmentList()));
            }

            PositionMoveRotation positionMoveRotation = new PositionMoveRotation(npc.position(), Vec3.ZERO, npc.getYRot(), npc.getXRot());
            ClientboundTeleportEntityPacket teleportPack = new ClientboundTeleportEntityPacket(
                    npc.getId(),
                    positionMoveRotation,
                    Collections.emptySet(),
                    true
            );
            connection.send(teleportPack);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                connection.send(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(npc.getUUID())));
            }, 20L);

//            if (!npc.shownToPlayers.contains(viewer.getUniqueId())) {
//                npc.shownToPlayers.add(viewer.getUniqueId());
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
