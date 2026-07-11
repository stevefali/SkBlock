package com.steve.skblock.commands;

import com.steve.skblock.worlds.SkyblockWorldFactory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class LoadWorldCommand implements CommandExecutor {

    Plugin plugin;

    public LoadWorldCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if (args.length == 1) {
            try {
                SkyblockWorldFactory.loadPlayerWorld(args[0], plugin)
                        .exceptionally(throwable -> {
                            sender.sendMessage("§e" + throwable.getMessage());
                            return null;
                        })
                        .thenApply(worldName -> {
                            if (worldName != null) {
                                sender.sendMessage("§aLoaded world " + worldName);
                                return worldName;
                            } else {
                                sender.sendMessage("§eThere was a problem loading the world");
                                return null;
                            }
                        });
                        return true;
            } catch (Exception e) {
                sender.sendMessage("§eError loading world: " + e.getMessage());
                return false;
            }
        }

        return false;
    }
}
