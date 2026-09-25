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

}
