package com.steve.skblock.game.quest.type.action;

import com.steve.skblock.game.quest.QuestRewardAction;
import org.bukkit.entity.Player;

public class CustomAction implements QuestRewardAction {

    private QuestRewardAction questRewardAction;

    public CustomAction(QuestRewardAction questRewardAction) {
        this.questRewardAction = questRewardAction;
    }

    @Override
    public String getMessage() {
        return questRewardAction.getMessage();
    }

    @Override
    public void performAction(Player player) {
        questRewardAction.performAction(player);
    }
}
