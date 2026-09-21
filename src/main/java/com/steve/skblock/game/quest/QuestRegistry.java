package com.steve.skblock.game.quest;

import com.steve.skblock.game.quest.type.CraftingQuest;
import com.steve.skblock.game.quest.type.HarvestQuest;
import com.steve.skblock.game.quest.type.PlacingQuest;
import com.steve.skblock.game.quest.type.TalkToRandyQuest;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class QuestRegistry {

    private final Plugin plugin;
    private final ConfigurationSection questsSection;
    private final String startingQuestId;

    private static final String TYPE_KEY = "type";
    private static final String TITLE_KEY = "title";
    private static final String MATERIAL_KEY = "material";
    private static final String AMOUNT_KEY = "amount";
    private static final String DESCRIPTION_KEY = "description";
    private static final String NEXT_KEY = "next";
    private static final String DIALOGUE_KEY = "dialogue";

    private static final Map<String, QuestTypeFactory> questTypeFactories = new HashMap<>();
    private static final Map<String, SkyblockQuest> skyblockQuests = new HashMap<>();

    public QuestRegistry(Plugin plugin) {
        this.plugin = plugin;
        FileConfiguration fileConfiguration = plugin.getConfig();
        this.questsSection = fileConfiguration.getConfigurationSection("quests");
        this.startingQuestId = fileConfiguration.getString("starting_quest");

        registerTypeFactories();
        registerQuests();
    }

    public void test(Plugin plugin) {
        plugin.getLogger().info("Testing...");
        for (String questId : questsSection.getKeys(false)) {
            plugin.getLogger().info("QuestId: " + questId);
        }
        plugin.getLogger().info("Start: " + startingQuestId);
    }

    public String getStartingQuestId() {
        return this.startingQuestId;
    }

    public SkyblockQuest getQuest(String questId) {
        return skyblockQuests.get(questId);
    }


    private void registerQuests() {
        for (String questId : questsSection.getKeys(false)) {
            ConfigurationSection section = questsSection.getConfigurationSection(questId);
            String questType = section.getString(TYPE_KEY);

            QuestTypeFactory questTypeFactory = questTypeFactories.get(questType);
            if (questTypeFactory == null) {
                plugin.getLogger().warning("Warning: Skyblock Quest Registry failed to find quest type: " + questType);
                continue;
            }

            SkyblockQuest skyblockQuest = questTypeFactory.build(questId, section);
            skyblockQuests.put(questId, skyblockQuest);
        }

        plugin.getLogger().info("Skyblock quests registered");
    }

    private void registerTypeFactories() {
        questTypeFactories.put(
                "harvest", (id, section) -> new HarvestQuest(
                        id,
                        section.getString(TITLE_KEY),
                        section.getString(DESCRIPTION_KEY),
                        Material.getMaterial(section.getString(MATERIAL_KEY)),
                        section.getInt(AMOUNT_KEY),
                        section.getString(NEXT_KEY)
                )
        );
        questTypeFactories.put(
                "craft", (id, section) -> new CraftingQuest(
                        id,
                        section.getString(TITLE_KEY),
                        section.getString(DESCRIPTION_KEY),
                        Material.getMaterial(section.getString(MATERIAL_KEY)),
                        section.getInt(AMOUNT_KEY),
                        section.getString(NEXT_KEY)
                )
        );
        questTypeFactories.put(
                "talk_randy", (id, section) -> new TalkToRandyQuest(
                        id,
                        section.getString(TITLE_KEY),
                        section.getStringList(DIALOGUE_KEY),
                        section.getString(DESCRIPTION_KEY),
                        section.getInt(AMOUNT_KEY),
                        section.getString(NEXT_KEY)
                )
        );
        questTypeFactories.put(
                "place", (id, section) -> new PlacingQuest(
                        id,
                        section.getString(TITLE_KEY),
                        section.getString(DESCRIPTION_KEY),
                        Material.getMaterial(section.getString(MATERIAL_KEY)),
                        section.getInt(AMOUNT_KEY),
                        section.getString(NEXT_KEY)
                )
        );

        plugin.getLogger().info("Skyblock quest types registered");
    }


}
