package com.steve.skblock.commands;

import com.steve.skblock.Skblock;
import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import com.steve.skblock.sidebar.SkyblockSidebar;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.steve.skblock.worlds.SkyblockWorldFactory.WORLD_NAME_PREFIX;

public class ForceSetPlayerQuestCommand implements CommandExecutor, TabCompleter {

    private final SessionRegistry sessionRegistry = Skblock.getSessionRegistry();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (args.length < 2) {
            sender.sendMessage("§4Please specify a player and a quest ID");
            return false;
        }

        if (args.length > 3) {
            sender.sendMessage("§4Please specify a player, quest Id, and (optional) quest progress");
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage("§4No player found with name " + args[0]);
            return false;
        }

        World world = Bukkit.getWorld(WORLD_NAME_PREFIX + player.getUniqueId());
        if (world == null) {
            sender.sendMessage("§4No skyblock world found for player " + args[0]);
            return false;
        }

        SkyblockSession skyblockSession = sessionRegistry.getSession(world);
        if (skyblockSession == null) {
            sender.sendMessage("§4Error retrieving Skyblock data for player " + args[0]);
            return false;
        }

        if (Skblock.getQuestRegistry().getQuest(args[1]) == null) {
            sender.sendMessage("§4Can't find quest with id " + args[1]);
            return false;
        }

        int questProgress = 0;
        if (args.length == 3) {
            try {
                questProgress = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cQuest progress value must be an integer");
            }
            if (questProgress < 0 || questProgress > Skblock.getQuestRegistry().getQuest(args[1]).getTargetAmount()) {
                sender.sendMessage("§cInvalid quest progress value");
                questProgress = 0;
            }
        }

        skyblockSession.getSkyblockProgress().forceSetQuest(args[1], questProgress);
        skyblockSession.saveAll();

        SkyblockSidebar.updateQuestSidebarLines(player.getUniqueId(), skyblockSession);

        player.sendMessage("§aYour current quest has been set to " + Skblock.getQuestRegistry().getQuest(args[1]).getTitle());
        if (!(sender instanceof Player senderPlayer) || !senderPlayer.getUniqueId().equals(player.getUniqueId())) {
            sender.sendMessage("§aSet quest for " + args[0] + " to " + args[1]);
        }

        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String @NotNull [] args) {

        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }

        if (args.length == 2) {
            return Skblock.getQuestRegistry().getAllQuestIds();
        }

        return List.of();
    }
}
