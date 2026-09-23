package com.steve.skblock.game.quest.type.action;

import com.steve.skblock.game.quest.QuestRewardAction;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveItemAction implements QuestRewardAction {

    private final Material material;
    private final int quantity;

    public GiveItemAction(Material material, int quantity) {
        this.material = material;
        this.quantity = quantity;
    }

    @SuppressWarnings({"removal", "deprecation"})
    @Override
    public String getMessage() {
        String key = material.getTranslationKey();
        TranslatableComponent component = new TranslatableComponent(key);
        return "§a" + quantity + " " + component.toPlainText() + " added to your inventory";
    }

    @Override
    public void performAction(Player player) {
        player.getInventory().addItem(new ItemStack(material, quantity));
    }

}
