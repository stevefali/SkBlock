package com.steve.skblock.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


public class NPC extends ServerPlayer {

    private String speakingMessage;
    private Consumer<Player> defaultInteraction;
    private final List<Pair<EquipmentSlot, ItemStack>> equipmentList =  new ArrayList<>();

    public NPC(MinecraftServer server, ServerLevel level, GameProfile gameProfile, ClientInformation clientInformation, @Nullable Consumer<Player> defaultInteraction) {
        super(server, level, gameProfile, clientInformation);
        this.defaultInteraction = defaultInteraction;
    }

    public void speak(Player player) {
        if (defaultInteraction != null) {
            defaultInteraction.accept(player);
        } else {
            player.sendMessage(speakingMessage + " " + this.getId());
        }
    }

    public void setSpeakingMessage(String message) {
        this.speakingMessage = message;
    }

    public List<Pair<EquipmentSlot, ItemStack>> getEquipmentList() {
        return this.equipmentList;
    }

    public void equipItem(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        Optional<Pair<EquipmentSlot, ItemStack>> existingItemPair =
                equipmentList.stream().filter(itemPair -> itemPair.getFirst().equals(equipmentSlot)).findAny();
        existingItemPair.ifPresent(equipmentList::remove);
        equipmentList.add(Pair.of(equipmentSlot, itemStack));
    }


}
