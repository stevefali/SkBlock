package com.steve.skblock.commands;

import com.steve.skblock.util.ProxyTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class HomeCommand implements CommandExecutor {

    private Plugin plugin;

    public HomeCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NonNull @NotNull String[] strings) {

        if (!(commandSender instanceof Player player)) {
            return false;
        }

        ProxyTeleport.teleportPlayer(plugin, player, ProxyTeleport.LOBBY_SERVER);

        return true;
    }
}
