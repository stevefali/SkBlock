package com.steve.skblock.npc;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;


public class NPC extends ServerPlayer {

    private String speakingMessage;

    public NPC(MinecraftServer server, ServerLevel level, GameProfile gameProfile, ClientInformation clientInformation) {
        super(server, level, gameProfile, clientInformation);
    }

    public void speak(Player player) {
        player.sendMessage(speakingMessage);
    }

    public void setSpeakingMessage(String message) {
        this.speakingMessage = message;
    }


}
