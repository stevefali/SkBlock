package com.steve.skblock.game.quest;

import org.bukkit.configuration.ConfigurationSection;

public interface ActionTypeFactory {
    QuestRewardAction build(ConfigurationSection section);
}
