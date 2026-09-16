package com.steve.skblock.events.routed;

import com.steve.MegaHUD.api.MegaHudService;
import com.steve.MegaHUD.api.SidebarLine;
import com.steve.skblock.Skblock;
import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import com.steve.skblock.game.data.DataKeys;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.minecraft.network.chat.numbers.NumberFormatTypes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BlockEvent implements Listener {

    private final Plugin plugin;
    private final SessionRegistry sessionRegistry;


    public BlockEvent(Plugin plugin, SessionRegistry sessionRegistry) {
        this.plugin = plugin;
        this.sessionRegistry = sessionRegistry;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {

        SkyblockSession session = sessionRegistry.getSession(event.getBlock().getWorld());
        if (session != null) {
            session.onBlockBreak(event);
            return;
        }

        Material type = event.getBlock().getType();
        Player player = event.getPlayer();

        if (type == Material.BLACK_WOOL) {
            player.sendMessage("You broke that in the lobby!");

        }

        MegaHudService megaHudService = Skblock.getMegaHudService();
        if (type == Material.BLUE_WOOL) {
            megaHudService.setSidebarTitle(player.getUniqueId(), "§a§lSkyblock");
            megaHudService.setSidebarLine(player.getUniqueId(), 0, SidebarLine.rightSideOnly("Right side only"));
            megaHudService.setSidebarLine(player.getUniqueId(), 1, SidebarLine.BLANK);
            megaHudService.setSidebarLine(player.getUniqueId(), 2, SidebarLine.bothSides("§bThis is...", "§e...The Lobby!"));
            megaHudService.setSidebarLine(player.getUniqueId(), 3, SidebarLine.leftSideOnly("§nCool, Huh?"));
        }

        if (type == Material.YELLOW_WOOL) {
            megaHudService.removeSidebarLine(player.getUniqueId(), 2);
        }

        if (type == Material.RED_WOOL) {
            megaHudService.removeSidebar(player.getUniqueId());
        }

        if (type == Material.LIGHT_BLUE_WOOL) {
            megaHudService.setSidebarLine(player.getUniqueId(), 4, SidebarLine.rightSideOnly("Extra"));
        }

        if (type == Material.ORANGE_WOOL) {
            megaHudService.removeAllSidebarLines(player.getUniqueId());
        }

        if (type == Material.CYAN_WOOL) {
            Map<Integer, SidebarLine> sidebarLines = new HashMap<>();
            sidebarLines.put(0, SidebarLine.leftSideOnly("§kHey there"));
            sidebarLines.put(2, SidebarLine.rightSideOnly("§aCurrent progress: §l21/25"));
            sidebarLines.put(1, SidebarLine.bothSides("", ""));
            sidebarLines.put(3, SidebarLine.BLANK);
            sidebarLines.put(4, SidebarLine.BLANK);
            sidebarLines.put(5, SidebarLine.BLANK);
            sidebarLines.put(6, SidebarLine.leftSideOnly("Some other score: §411"));

            megaHudService.setAllSidebarLines(player.getUniqueId(), sidebarLines);
        }

        if (type == Material.WHITE_WOOL) {
            Map<Integer, SidebarLine> sidebarLines = new HashMap<>();
            sidebarLines.put(0, SidebarLine.BLANK);
            sidebarLines.put(1, SidebarLine.BLANK);
            sidebarLines.put(2, SidebarLine.leftSideOnly("These are the replacements!"));

            megaHudService.setAllSidebarLines(player.getUniqueId(), sidebarLines);
        }

        if (type == Material.GREEN_WOOL) {
            Map<Integer, SidebarLine> sidebarLines = new HashMap<>();
            megaHudService.setAllSidebarLines(player.getUniqueId() ,sidebarLines);
        }




        /*if (type == Material.CRYING_OBSIDIAN) {
            System.out.println("Counting entities....");
            for (String worldName : Skblock.getNpcIds().keySet()) {
                for (UUID uuid : Skblock.getNpcIds().get(worldName)) {
                    System.out.println(worldName + ": " + uuid);
                }
            }

            World world = event.getBlock().getWorld();
            List<Entity> stands = world.getEntities().stream().filter(entity -> entity.getType() == EntityType.ARMOR_STAND && entity.getCustomName() != null).toList();
            List<Entity> displays = world.getEntities().stream().filter(entity -> entity.getType() == EntityType.TEXT_DISPLAY).toList();
            System.out.println("Armor stands: " + stands.size());
            System.out.println("TextDisplays: " + displays.size());
        }

        if (type == Material.BROWN_WOOL) {
            System.out.println("Removing Entities....");
            World world = event.getBlock().getWorld();
            List<Entity> stands = world.getEntities().stream().filter(entity -> entity.getType() == EntityType.ARMOR_STAND && entity.getCustomName() != null).toList();
            for (Entity stand : stands) {
                stand.remove();
            }
            List<Entity> displays = world.getEntities().stream().filter(entity -> entity.getType() == EntityType.TEXT_DISPLAY).toList();
            for (Entity display : displays) {
                display.remove();
            }
        }

        if (type == Material.YELLOW_WOOL) {
            World world = event.getPlayer().getWorld();
            Skblock.getNpcIds().remove(world.getName());
            Skblock.getNpcService().removeAllNpcsInWorld(world.getName());
        }*/


    }


}
