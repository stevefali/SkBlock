package com.steve.skblock.game.quest;

import org.bukkit.entity.Player;

public interface QuestRewardAction {

    String getMessage();

    void performAction(Player player);
}
