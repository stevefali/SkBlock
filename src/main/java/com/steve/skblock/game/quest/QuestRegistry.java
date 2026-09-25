package com.steve.skblock.game.quest;

import com.steve.skblock.game.quest.type.CraftingQuest;
import com.steve.skblock.game.quest.type.HarvestQuest;
import com.steve.skblock.game.quest.type.PlacingQuest;
import com.steve.skblock.game.quest.type.TalkToRandyQuest;
import com.steve.skblock.game.quest.type.action.CustomAction;
import com.steve.skblock.game.quest.type.action.GiveItemAction;
import com.steve.skblock.game.quest.type.action.SetIslandAction;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.List;
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
    private static final String REWARD_KEY = "reward";
    private static final String NEXT_KEY = "next";
    private static final String DIALOGUE_KEY = "dialogue";
    private static final String ACTION_MATERIAL_KEY = "action_material";
    private static final String MATERIAL_QUANTITY_KEY = "material_quantity";
    private static final String ACTION_KEY = "action";
    private static final String ACTION_TYPE_KEY = "action_type";
    private static final String ISLAND_LEVEL_KEY = "island_level";
    private static final String CUSTOM_ACTION_NAME_KEY = "custom_action_name";

    private static final String CUSTOM_ACTIONS_CLASSES_PATH = "com.steve.skblock.game.quest.type.action.custom.";

    private static final Map<String, QuestTypeFactory> questTypeFactories = new HashMap<>();
    private static final Map<String, SkyblockQuest> skyblockQuests = new HashMap<>();

    private static final Map<String, ActionTypeFactory> actionTypeFactories = new HashMap<>();

    public QuestRegistry(Plugin plugin) {
        this.plugin = plugin;
        FileConfiguration fileConfiguration = plugin.getConfig();
        this.questsSection = fileConfiguration.getConfigurationSection("quests");
        this.startingQuestId = fileConfiguration.getString("starting_quest");

        registerActionTypes();
        registerTypeFactories();
        registerQuests();
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
                        section.getInt(REWARD_KEY),
                        buildRewardActionFromConfig(section.getConfigurationSection(ACTION_KEY)),
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
                        section.getInt(REWARD_KEY),
                        buildRewardActionFromConfig(section.getConfigurationSection(ACTION_KEY)),
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
                        section.getInt(REWARD_KEY),
                        buildRewardActionFromConfig(section.getConfigurationSection(ACTION_KEY)),
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
                        section.getInt(REWARD_KEY),
                        buildRewardActionFromConfig(section.getConfigurationSection(ACTION_KEY)),
                        section.getString(NEXT_KEY)
                )
        );

        plugin.getLogger().info("Skyblock quest types registered");
    }

    private void registerActionTypes() {
        actionTypeFactories.put(
                "give_item", section -> new GiveItemAction(
                        Material.getMaterial(section.getString(ACTION_MATERIAL_KEY)),
                        section.getInt(MATERIAL_QUANTITY_KEY)
                )
        );
        actionTypeFactories.put(
                "set_island", section -> new SetIslandAction(
                        section.getInt(ISLAND_LEVEL_KEY)
                )
        );
        actionTypeFactories.put(
                "custom", section -> new CustomAction(
                        createCustomActionInstance(section.getString(CUSTOM_ACTION_NAME_KEY))
                )
        );

        plugin.getLogger().info("Skyblock quest action types registered");
    }

    private QuestRewardAction buildRewardActionFromConfig(ConfigurationSection actionSection) {
        if (actionSection == null) {
            return null;
        }
        String actionType = actionSection.getString(ACTION_TYPE_KEY);

        ActionTypeFactory actionTypeFactory = actionTypeFactories.get(actionType);
        if (actionTypeFactory != null) {
            return actionTypeFactory.build(actionSection);
        }
        plugin.getLogger().warning("Warning: Skyblock Quest registry failed to find action type " + actionType);
        return null;
    }


    private QuestRewardAction createCustomActionInstance(String className) {
        Class<?> customImplementerClass;
        try {
            customImplementerClass = Class.forName(CUSTOM_ACTIONS_CLASSES_PATH + className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(
                    "Custom action class not found: " + CUSTOM_ACTIONS_CLASSES_PATH + className + ". Check class name in config.yml", e
            );
        }

        MethodHandle constructor;
        try {
            constructor = MethodHandles.lookup().findConstructor(customImplementerClass, MethodType.methodType(void.class));
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Can't instantiate class " + className + " by class name: Class requires a no-arg public constructor", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Can't access constructor of class " + className + ". Is it public?", e);
        }

        try {
            return (QuestRewardAction) constructor.invoke();
        } catch (Throwable throwable) {
            throw new RuntimeException("Error instantiating CustomRewardAction class " + className + " from class name in config.yml", throwable);
        }
    }

    public List<String> getAllQuestIds() {
        return skyblockQuests.keySet().stream().toList();
    }

}
