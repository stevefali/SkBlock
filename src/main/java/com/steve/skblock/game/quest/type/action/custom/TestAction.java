package com.steve.skblock.game.quest.type.action.custom;

import com.steve.skblock.game.quest.QuestRewardAction;
import org.bukkit.entity.Player;

public class TestAction implements QuestRewardAction {

    @Override
    public String getMessage() {
        return "§bThis is the TestAction class's message";
    }

    @Override
    public void performAction(Player player) {
        player.sendMessage("§6§lTestAction class custom action performed!");
    }
}
