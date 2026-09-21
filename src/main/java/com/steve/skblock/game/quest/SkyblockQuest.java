package com.steve.skblock.game.quest;

public interface SkyblockQuest {

    String getId();

    String getTitle();

    String getDescription();

    int getTargetAmount();

    String getNextQuestId();
}
