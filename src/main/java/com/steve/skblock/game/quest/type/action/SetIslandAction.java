package com.steve.skblock.game.quest.type.action;

import com.steve.skblock.game.quest.QuestRewardAction;
import org.bukkit.entity.Player;

public class SetIslandAction implements QuestRewardAction {

    private final int islandLevel;

    public SetIslandAction(int islandLevel) {
        this.islandLevel = islandLevel;
    }

    @Override
    public String getMessage() {
        // TODO: Implement 'getName' in Island Factory.
        return "";
    }

    @Override
    public void performAction(Player player) {
        // TODO: Implement Island Factory with method for this.
    }
}
