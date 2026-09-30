package com.steve.skblock.commands;

import com.steve.skblock.worldoperation.IslandSetter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SetIslandCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (args.length != 2) {
            sender.sendMessage("§ePlease specify a world and a section number");
            return false;
        }
        World world = Bukkit.getWorld(args[0]);
        if (world == null) {
            sender.sendMessage("§eError: can't find world named " + args[0]);
            return false;
        }

        try {
            int section = Integer.parseInt(args[1]);
            if (section < 0 || section > 8) {
                sender.sendMessage("§ePlease specify a section number between 0 and 8");
                return false;
            }

            IslandSetter.setIslandInSection(section, world, (Player) sender);

        } catch (NumberFormatException e) {
            sender.sendMessage("§ePlease specify a section number between 0 and 8");
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

        if (!sender.hasPermission("skyblock.admin")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return Bukkit.getWorlds().stream().map(WorldInfo::getName).toList();
        }
        if (args.length == 2) {
            return Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8");
        }

        return List.of();
    }
}
