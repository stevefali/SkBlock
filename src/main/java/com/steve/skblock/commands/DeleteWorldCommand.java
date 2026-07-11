package com.steve.skblock.commands;

import com.steve.skblock.worlds.SkyblockWorldFactory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DeleteWorldCommand implements CommandExecutor, TabCompleter {

    private Plugin plugin;

    public DeleteWorldCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if (args.length == 1) {
            SkyblockWorldFactory.deleteWorld(args[0], plugin)
                    .exceptionally(throwable -> {
                        sender.sendMessage("§c" + throwable.getMessage());
                        return null;
                    }).thenApply(deletionResult -> {
                        if (deletionResult.success()) {
                            sender.sendMessage("§a" + deletionResult.message());
                        } else {
                            sender.sendMessage("§c" + deletionResult.message());
                        }
                        return deletionResult;
                    });
        } else {
            sender.sendMessage("§cPlease specify a world to delete!");
            return false;
        }

        return true;
    };

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1) {
            File worldsFolder = new File("./SkyblockWorlds");
            return Arrays.stream(worldsFolder.listFiles()).map(file -> file.getName().split("_")[1]).toList();
        }

        return List.of();
    }
}
