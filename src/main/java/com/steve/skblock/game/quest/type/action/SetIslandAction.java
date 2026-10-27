package com.steve.skblock.game.quest.type.action;

import com.steve.skblock.game.quest.QuestRewardAction;
import com.steve.skblock.worldoperation.IslandSetter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.steve.skblock.worlds.SkyblockWorldFactory.WORLD_NAME_PREFIX;

public class SetIslandAction implements QuestRewardAction {

    private final int islandLevel;

    public SetIslandAction(int islandLevel) {
        this.islandLevel = islandLevel;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public void performAction(Player player) {
        IslandSetter.setIslandInSection(islandLevel, Bukkit.getWorld(WORLD_NAME_PREFIX + player.getUniqueId()), player);
    }
}
