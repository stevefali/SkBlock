package com.steve.skblock.commands;

import com.steve.skblock.worlds.SkyblockWorldFactory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class MakeWorldCommand implements CommandExecutor {

    private Plugin plugin;

    public MakeWorldCommand(Plugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Player senda = (Player) sender;

        if (args.length == 1) {
            try {
                SkyblockWorldFactory.getOrCreateWorldPath(args[0], plugin)
                        .exceptionally(throwable -> {
                            senda.sendMessage(throwable.getMessage());
                            return null;
                        }).thenRun(() -> {
                            senda.sendMessage("§aNew world folder created for " + args[0]);
                        });
                return true;
            } catch (Exception e) {
                senda.sendMessage("Error creating world!");
                e.printStackTrace();
            }
        } else {
            senda.sendMessage("§ePlease specify a name for the world");
        }

        return false;
    }
}
