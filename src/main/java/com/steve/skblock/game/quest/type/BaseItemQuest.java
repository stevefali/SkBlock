package com.steve.skblock.game.quest.type;

import com.steve.skblock.game.quest.QuestRewardAction;
import org.bukkit.Material;

public class BaseItemQuest extends BaseQuest {

    Material material;

    public BaseItemQuest(
            String id,
            String title,
            String description,
            Material material,
            int targetAmount,
            int reward,
            QuestRewardAction questRewardAction,
            String nextQuestId) {

        super(id, title, description, targetAmount, reward, questRewardAction, nextQuestId);
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}
