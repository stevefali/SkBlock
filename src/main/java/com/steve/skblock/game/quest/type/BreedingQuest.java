package com.steve.skblock.game.quest.type;

import com.steve.skblock.game.quest.QuestRewardAction;
import org.bukkit.entity.EntityType;

public class BreedingQuest extends BaseMobQuest {
    public BreedingQuest(
            String id,
            String title,
            String description,
            EntityType entityType,
            int targetAmount,
            int reward,
            QuestRewardAction questRewardAction,
            String nextQuestId) {
        super(id, title, description, entityType, targetAmount, reward, questRewardAction, nextQuestId);
    }
}
