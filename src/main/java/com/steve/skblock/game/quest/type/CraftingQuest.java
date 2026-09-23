package com.steve.skblock.game.quest.type;

import com.steve.skblock.game.quest.QuestRewardAction;
import org.bukkit.Material;

public class CraftingQuest extends BaseItemQuest {

    public CraftingQuest(
            String id,
            String title,
            String description,
            Material material,
            int targetAmount,
            int reward,
            QuestRewardAction questRewardAction,
            String nextQuestId) {
        super(id, title, description, material, targetAmount, reward, questRewardAction, nextQuestId);
    }

}
