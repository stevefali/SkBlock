package com.steve.skblock.npc;

import com.steve.MegaNPCs.api.NpcService;
import com.steve.skblock.Skblock;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

public class NpcFactory {

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
