package com.steve.skblock.game;

import com.steve.skblock.game.generator.GeneratorType;

import java.util.HashSet;
import java.util.Set;

public class SkyblockProgress {

    private String currentQuestId;
    private int currentQuestProgress;
    private final Set<String> unlockedGenerators = new HashSet<>(Set.of(GeneratorType.DEFAULT.name()));

    public SkyblockProgress(String startingQuestId) {
        this.currentQuestId = startingQuestId;
        this.currentQuestProgress = 0;
    }

    private SkyblockProgress() {
    }

    public void onQuestComplete(String nextQuestId) {
        if (nextQuestId != null) {
            currentQuestId = nextQuestId;
        }
        currentQuestProgress = 0;
    }

    public String getCurrentQuestId() {
        return this.currentQuestId;
    }

    public int getCurrentQuestProgress() {
        return this.currentQuestProgress;
    }

    public void setCurrentQuestProgress(int questProgress) {
        this.currentQuestProgress = questProgress;
    }

    /**
     * THIS SHOULD ONLY BE USED FOR FORCE-SETTING THE QUEST!
     */
    public void forceSetQuest(String questId) {
        forceSetQuest(questId, 0);
    }

    /**
     * THIS SHOULD ONLY BE USED FOR FORCE-SETTING THE QUEST!
     */
    public void forceSetQuest(String questId, int questProgress) {
        this.currentQuestId = questId;
        this.currentQuestProgress = questProgress;
    }

    public boolean isGeneratorUnlocked(GeneratorType generatorType) {
        return unlockedGenerators.contains(generatorType.name());
    }

    public void unlockGenerator(GeneratorType generatorType) {
        unlockedGenerators.add(generatorType.name());
    }

}
