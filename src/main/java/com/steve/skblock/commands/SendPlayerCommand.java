package com.steve.skblock.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SendPlayerCommand implements CommandExecutor, TabCompleter {

    private Plugin plugin;

    public SendPlayerCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 1) {
            sender.sendMessage("Please specify a world and player");
            return false;
        }
        if (args.length > 0) {
            if (plugin.getServer().getWorld(args[0]) != null) {
                if (args.length == 2) {
                    World world = plugin.getServer().getWorld(args[0]);
                    if (plugin.getServer().getPlayer(args[1]) != null) {
                        // Bingo
                        Location location = new Location(world, 0, 65, 0);
                        Player player = plugin.getServer().getPlayer(args[1]);
                        Location spawnLocation = world.getSpawnLocation();

                        player.teleport(spawnLocation);


                    } else {
                        sender.sendMessage("No player found with name " + args[1]);
                    }
                } else {
                    sender.sendMessage("Please specify exactly 1 world and 1 player");
                    return false;
                }
            } else {
                sender.sendMessage("Be sure to specify a correct world name");
                return false;
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1) {
            List<String> worldNames = plugin.getServer().getWorlds().stream().map(WorldInfo::getName).toList();
            return worldNames;
        } if (args.length == 2){
            List<String> onlinePlayers = plugin.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
            return onlinePlayers;
        }

        return List.of();

    }
}
