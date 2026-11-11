package com.steve.skblock.game.quest.type.action;

import com.steve.skblock.game.quest.QuestRewardAction;
import org.bukkit.entity.Player;

public class EmptyAction implements QuestRewardAction {
    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public void performAction(Player player) {
        return;
    }
}
