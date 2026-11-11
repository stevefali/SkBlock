package com.steve.skblock.game.quest.type;

import com.steve.skblock.game.quest.QuestRewardAction;

public class SleepQuest extends BaseQuest {

    public SleepQuest(
            String id,
            String title,
            String description,
            int targetAmount,
            int reward,
            QuestRewardAction questRewardAction,
            String nextQuestId) {
        super(id, title, description, targetAmount, reward, questRewardAction, nextQuestId);
    }
}
