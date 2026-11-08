package com.steve.skblock.game.quest.type;

import com.steve.skblock.game.quest.QuestRewardAction;
import com.steve.skblock.util.Vector2D;
import com.steve.skblock.worldoperation.IslandSetter;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class TravelQuest extends BaseQuest {

    private static final double xHalf = 9;
    private static final double yHalf = 20;
    private static final double zHalf = 8;

    BoundingBox regionBox;

    public TravelQuest(
            String id,
            String title,
            String description,
            int islandSection,
            int targetAmount,
            int reward,
            QuestRewardAction questRewardAction,
            String nextQuestId) {
        super(id, title, description, targetAmount, reward, questRewardAction, nextQuestId);

        Vector2D origin2D = IslandSetter.getIslandOrigin(islandSection);
        this.regionBox = BoundingBox.of(new Vector(origin2D.x(), 65, origin2D.y()), xHalf, yHalf, zHalf);
    }

    public BoundingBox getRegionBox() {
        return this.regionBox;
    }
}
