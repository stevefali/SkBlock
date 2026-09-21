package com.steve.skblock.game.quest.type;

import com.steve.skblock.game.quest.SkyblockQuest;

import java.util.List;

public class TalkToRandyQuest implements SkyblockQuest {

    private final String id;
    private final String title;
    private final List<String> dialogueLines;
    private final String description;
    private final int targetAmount;
    private final String nextQuestId;

    public TalkToRandyQuest(String id, String title, List<String> dialogueLines, String description, int targetAmount, String nextQuestId) {
        this.id = id;
        this.title = title;
        this.dialogueLines = dialogueLines;
        this.description = description;
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

    public List<String> getDialogueLines() {
        return this.dialogueLines;
    }
}
