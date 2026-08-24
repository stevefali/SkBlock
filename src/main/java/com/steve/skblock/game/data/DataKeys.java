package com.steve.skblock.game.data;

import com.steve.skblock.Skblock;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class DataKeys {

    private static final Skblock plugin = JavaPlugin.getPlugin(Skblock.class);

    public static final NamespacedKey SKYBLOCK_SCORE = keyFor("score");
    public static final NamespacedKey PROGRESS_JSON = keyFor("progress_json");


    private static NamespacedKey keyFor(String name) {
        return new NamespacedKey(plugin, name);
    }

}
