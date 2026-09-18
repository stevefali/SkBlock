package com.steve.skblock.game;

import java.util.HashSet;
import java.util.Set;

public class SkyblockProgress {

    private String currentQuestId;
    private int currentQuestProgress;
    private final Set<String> completedQuestIds = new HashSet<>();

    public SkyblockProgress(String startingQuestId) {
        this.currentQuestId = startingQuestId;
        this.currentQuestProgress = 0;
    }

    public void onQuestComplete(String nextQuestId) {
        if (currentQuestId != null) {
            completedQuestIds.add(currentQuestId);
        }
        currentQuestId = nextQuestId;
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


}
