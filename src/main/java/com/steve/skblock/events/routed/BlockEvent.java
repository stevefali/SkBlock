package com.steve.skblock.events.routed;

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

import java.util.List;
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

           /* ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard scoreboard = manager.getMainScoreboard();

            Objective objective = scoreboard.registerNewObjective("test", "anything", "Skyblock");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            Score testScore = objective.getScore("§dTest Score");
            testScore.setScore(1);

            Score testScore2 = objective.getScore("Second Test score");
            testScore2.setScore(0);
//            objective.setAutoUpdateDisplay(true);
//            objective.setDisplayName("§b§lMega Randomizer");


//            Objective objective2 = scoreboard.registerNewObjective("test_2", "anything", "Another set of objectives here \nAnd another line here? \n \n Blank above?");
//            objective2.setDisplaySlot(DisplaySlot.SIDEBAR);

            player.setScoreboard(scoreboard);*/

//


        }

        if (type == Material.GREEN_WOOL) {
            Objective objective = player.getScoreboard().getObjective("test");
//            Objective objective2 = player.getScoreboard().getObjective("test_2");
            if (objective != null) {
                objective.unregister();
            }
//            if (objective2 != null) {
//                objective2.unregister();
//            }

//            player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
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
