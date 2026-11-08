package com.steve.skblock.game.quest.type;

import com.steve.skblock.game.quest.QuestRewardAction;
import org.bukkit.entity.EntityType;

public class BaseMobQuest extends BaseQuest {

    EntityType entityType;

    public BaseMobQuest(
            String id,
            String title,
            String description,
            EntityType entityType,
            int targetAmount,
            int reward,
            QuestRewardAction questRewardAction,
            String nextQuestId) {
        super(id, title, description, targetAmount, reward, questRewardAction, nextQuestId);
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
