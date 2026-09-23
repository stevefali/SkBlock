package com.steve.skblock.game;

public class SkyblockProgress {

    private String currentQuestId;
    private int currentQuestProgress;

    public SkyblockProgress(String startingQuestId) {
        this.currentQuestId = startingQuestId;
        this.currentQuestProgress = 0;
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


}
