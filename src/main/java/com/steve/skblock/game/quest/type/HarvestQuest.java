package com.steve.skblock.game.quest.type;

import com.steve.skblock.game.quest.SkyblockQuest;
import org.bukkit.Material;

public class HarvestQuest implements SkyblockQuest {

    private final String id;
    private final String title;
    private final String description;
    private final Material material;
    private final int targetAmount;
    private final String nextQuestId;

    public HarvestQuest(String id, String title, String description, Material material, int targetAmount, String nextQuestId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.material = material;
        this.targetAmount = targetAmount;
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
    public String getNextQuestId() {
        return this.nextQuestId;
    }

    public Material getMaterial() {
        return material;
    }
}
