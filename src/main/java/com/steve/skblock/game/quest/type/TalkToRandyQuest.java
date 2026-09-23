package com.steve.skblock.game.quest.type;

import com.steve.skblock.Skblock;
import com.steve.skblock.game.quest.QuestRewardAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class TalkToRandyQuest extends BaseQuest {

    private static final Plugin plugin = JavaPlugin.getPlugin(Skblock.class);
    private final List<String> dialogueLines;


    public TalkToRandyQuest(
            String id,
            String title,
            List<String> dialogueLines,
            String description,
            int targetAmount,
            int reward,
            QuestRewardAction questRewardAction,
            String nextQuestId) {

        super(id, title, description, targetAmount, reward, questRewardAction, nextQuestId);
        this.dialogueLines = dialogueLines;
    }


    @Override
    public void performRewardAction(Player player) {
        for (int i = 1; i <= dialogueLines.size(); i++) {
            int lineNumber = i;
            Bukkit.getScheduler().runTaskLater(
                    plugin, () -> {
                        player.sendMessage("[" + lineNumber + "/" + dialogueLines.size() + "] " + dialogueLines.get(lineNumber - 1));
                    }, 20L * lineNumber
            );
        }
        super.performRewardAction(player);
    }


    public List<String> getDialogueLines() {
        return this.dialogueLines;
    }
}
