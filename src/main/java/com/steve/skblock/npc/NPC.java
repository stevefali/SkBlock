package com.steve.skblock.npc;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.function.Consumer;


public class NPC extends ServerPlayer {

    private String speakingMessage;
    private Consumer<Player> defaultInteraction;

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


}
