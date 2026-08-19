package com.steve.skblock.npc;

import com.steve.MegaNPCs.api.NpcService;
import com.steve.skblock.Skblock;
import com.steve.skblock.util.ProxyTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class NpcFactory {

    private static final double NPC_TITLE_SCALE = 2.0;

    public static void createNpcs(World world, Plugin plugin) {
        String worldName = world.getName();

        NpcService npcService = Skblock.getNpcService();
        if (npcService == null) {
            plugin.getLogger().warning("Error creating NPCs for " + worldName + ": NpcService is null");
            return;
        }

        if (Skblock.getNpcIds().get(worldName) == null) {
            List<UUID> worldNpcIds = new ArrayList<>();

            if (worldName.equals("skyblock_lobby")) {

                UUID megId = NpcFactory.makeNpc(
                        new Location(world, -6.5, 65.5, 5.5, -105.0F, 8.0F),
                        "Meg",
                        "Meg", player -> {
                            ProxyTeleport.teleportPlayer(plugin, player, ProxyTeleport.LOBBY_SERVER);
                        },
                        "§6Meg \n§aMain Lobby",
                        NPC_TITLE_SCALE
                );
                worldNpcIds.add(megId);
                npcService.setDefaultNameVisible(megId, false);

            } else {

                // Skyblock worlds
                UUID randyId = NpcFactory.makeNpc(
                        new Location(world, -3.5, 65, -3.5, -40.0F, 0.0F),
                        "Randy",
                        "Randy",
                        player -> {
                            player.sendMessage("Welcome to your skyblock World, " + player.getName());
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                player.teleport(Skblock.getLobbySpawn());
                            }, 40L);
                        },
                        null,
                        NPC_TITLE_SCALE,
                        "§6Randy", "§aSkyblock Lobby"
                );
                worldNpcIds.add(randyId);
            }
            Skblock.getNpcIds().put(worldName, worldNpcIds);
        }
        npcService.removeOrphansFromWorld(worldName);
    }

    public static UUID makeNpc(Location location, String displayName, String skinName, @Nullable Consumer<Player> onInteract, @Nullable String title, double titleScale , String... nameLines) {
        NpcService npcService = Skblock.getNpcService();
        UUID npcId = npcService.createNpc(
                location,
                displayName,
                skinName,
                onInteract
        );
        npcService.setNpcRotation(npcId, location.getPitch(), location.getYaw(), location.getYaw());

        if (nameLines.length > 0) {
            npcService.setNameLines(npcId, nameLines);
        }

        if (title != null) {
            npcService.setTitle(npcId, title, titleScale);
        }

        return npcId;
    }

    public static void showNPCs(String worldName, Player player) {
        NpcService npcService = Skblock.getNpcService();
        if (npcService.getNpcsInWorld(worldName) != null) {
            for (UUID npcId : npcService.getNpcsInWorld(worldName)) {
                npcService.showNpcTo(npcId, player);
            }
        }
    }

}
