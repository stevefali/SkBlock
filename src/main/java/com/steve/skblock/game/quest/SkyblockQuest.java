package com.steve.skblock.game.quest;

import org.bukkit.entity.Player;

public interface SkyblockQuest {

    String getId();

    String getTitle();

    String getDescription();

    int getTargetAmount();

    int getReward();

    void performRewardAction(Player player);

    String getRewardMessage();

    QuestRewardAction getRewardAction();

    String getNextQuestId();
}
