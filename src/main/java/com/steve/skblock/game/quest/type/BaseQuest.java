package com.steve.skblock.game.quest.type;

import com.steve.skblock.game.quest.QuestRewardAction;
import com.steve.skblock.game.quest.SkyblockQuest;
import org.bukkit.entity.Player;

public class BaseQuest implements SkyblockQuest {

    String id;
    String title;
    String description;
    int targetAmount;
    int reward;
    QuestRewardAction questRewardAction;
    String rewardMessage;
    String nextQuestId;


    public BaseQuest(String id, String title, String description, int targetAmount, int reward, QuestRewardAction questRewardAction, String nextQuestId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.targetAmount = targetAmount;
        this.reward = reward;
        this.questRewardAction = questRewardAction;
        this.rewardMessage = (questRewardAction != null ? questRewardAction.getMessage() : null);
        this.nextQuestId = nextQuestId;
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
        return this.targetAmount;
    }

    @Override
    public int getReward() {
        return this.reward;
    }

    @Override
    public void performRewardAction(Player player) {
        if (this.questRewardAction != null) {
            questRewardAction.performAction(player);
        }
    }

    @Override
    public String getRewardMessage() {
        return this.rewardMessage;
    }

    @Override
    public QuestRewardAction getRewardAction() {
        return this.questRewardAction;
    }

    @Override
    public String getNextQuestId() {
        return this.nextQuestId;
    }

}
