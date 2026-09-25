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

public class ForceSetPlayerScoreCommand implements CommandExecutor, TabCompleter {

    private final SessionRegistry sessionRegistry = Skblock.getSessionRegistry();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (args.length != 2) {
            sender.sendMessage("§4Please specify a player and a score");
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

        try {
            int score = Integer.parseInt(args[1]);
            if (score < 0) {
                sender.sendMessage("§cInvalid score value");
                return false;
            }

            skyblockSession.forceSetScore(score);

            SkyblockSidebar.setSkyblockScoreLine(player.getUniqueId(), score);

            player.sendMessage("§aYour Skyblock score has been set to " + score);
            if (!(sender instanceof Player senderPlayer) || !senderPlayer.getUniqueId().equals(player.getUniqueId())) {
                sender.sendMessage("§aSet Skyblock score for " + args[0] + " to " + score);
            }

        } catch (NumberFormatException e) {
            sender.sendMessage("§cScore value must be an integer");
            return false;
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

        return List.of();
    }
}
