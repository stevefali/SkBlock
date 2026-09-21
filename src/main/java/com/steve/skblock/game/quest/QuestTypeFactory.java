package com.steve.skblock.game.quest;

import org.bukkit.configuration.ConfigurationSection;

public interface QuestTypeFactory {

    SkyblockQuest build(String id, ConfigurationSection section);

}
