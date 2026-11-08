package com.steve.skblock.game.quest.type.action;

import com.steve.skblock.Skblock;
import com.steve.skblock.game.quest.QuestRewardAction;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class RandySpeakAction implements QuestRewardAction {

    private static final Plugin plugin = JavaPlugin.getPlugin(Skblock.class);
    private final List<String> lines;

    public RandySpeakAction(List<String> lines) {
        this.lines = lines;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public void performAction(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.5f, 3.5f);
        for (int i = 1; i <= lines.size(); i++) {
            int lineNumber = i;
            Bukkit.getScheduler().runTaskLater(
                    plugin, () -> {
                        player.sendMessage("§6§oRandy:§r [" + lineNumber + "/" + lines.size() + "] " + lines.get(lineNumber - 1));
                    }, 30L * lineNumber
            );
        }
    }
}
