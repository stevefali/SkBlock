package com.steve.skblock.game.quest.type;

import com.steve.skblock.game.quest.QuestRewardAction;
import org.bukkit.TreeType;

import java.util.List;

public class GrowTreeQuest extends BaseQuest{

    List<TreeType> treeTypes;

    public GrowTreeQuest(
            String id,
            String title,
            String description,
            List<TreeType> treeTypes,
            int targetAmount,
            int reward,
            QuestRewardAction questRewardAction,
            String nextQuestId) {
        super(id, title, description, targetAmount, reward, questRewardAction, nextQuestId);
        this.treeTypes = treeTypes;
    }

    public List<TreeType> getTreeTypes() {
        return this.treeTypes;
    }

}
