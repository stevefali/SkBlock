package com.steve.skblock.game.quest.type;

import com.steve.skblock.game.quest.QuestRewardAction;
import com.steve.skblock.game.quest.SkyblockQuest;
import org.bukkit.entity.Player;

public final class QuestlineCompletedQuest implements SkyblockQuest {

    String id;
    String title;
    String description;

    public QuestlineCompletedQuest(
            String id,
            String title,
            String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getTargetAmount() {
        return 0;
    }

    @Override
    public int getReward() {
        return 0;
    }

    @Override
    public void performRewardAction(Player player) {

    }

    @Override
    public String getRewardMessage() {
        return "";
    }

    @Override
    public QuestRewardAction getRewardAction() {
        return null;
    }

    @Override
    public String getNextQuestId() {
        return this.id;
    }
}
