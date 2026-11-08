package com.steve.skblock.events.unrouted;

import com.steve.skblock.Skblock;
import com.steve.skblock.game.SessionRegistry;
import com.steve.skblock.game.SkyblockSession;
import com.steve.skblock.npc.NpcFactory;
import com.steve.skblock.sidebar.SkyblockSidebar;
import com.steve.skblock.util.TitlesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.steve.skblock.Skblock.SKYBLOCK_LOBBY_NAME;


public class PlayerEvent implements Listener {

    private Plugin plugin;

    private static final String WORLD_NAME_PREFIX = "skyblock_";
    private static final String EFFECTS_TAG = "effects";
    private final Location lobbySpawn;
    private final SessionRegistry sessionRegistry;

    public PlayerEvent(Plugin plugin, Location lobbySpawn, SessionRegistry sessionRegistry) {
        this.plugin = plugin;
        this.lobbySpawn = lobbySpawn;
        this.sessionRegistry = sessionRegistry;
    }


    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        player.teleport(lobbySpawn);
        event.setJoinMessage(null);

        NpcFactory.showNPCs(player.getWorld().getName(), player);

        TitlesUtils.sendTitle(player, "§6Welcome to Skyblock", 7, 40, 7);

        SkyblockSidebar.showSidebar(player.getUniqueId(), SKYBLOCK_LOBBY_NAME);
        Bukkit.getScheduler().runTask(plugin, this::updatePopulation);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();

        event.setQuitMessage(null);

        String worldName = WORLD_NAME_PREFIX + event.getPlayer().getUniqueId();
        removeWorldNpcs(worldName);

        Bukkit.getScheduler().runTask(plugin, this::updatePopulation);

        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            pauseEffects(event.getPlayer(), world);

            Bukkit.getScheduler().runTaskLater(
                    plugin, () -> {
                        if (world.getPlayers().isEmpty()
                                && Bukkit.getPlayer(playerId) == null
                        ) {
                            sessionRegistry.getSession(world).saveAll();
                            Bukkit.unloadWorld(worldName, true);
                        }
                    }, 20L * 30
            );
        }

    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World toWorld = event.getPlayer().getWorld();

        if (toWorld.getName().equals(SKYBLOCK_LOBBY_NAME)) {
            player.teleport(lobbySpawn);
            TitlesUtils.sendTitleAndSubtitle(player, "§6Skyblock Lobby", "", 7, 40, 7);
            SkyblockSession skyblockSession = sessionRegistry.getSession(event.getFrom());
            if (skyblockSession != null) {
                skyblockSession.hideBossBar();
                pauseEffects(player, event.getFrom());
            }
        } else {
            SkyblockSession skyblockSession = sessionRegistry.getSession(toWorld);
            if (skyblockSession != null) {
                skyblockSession.showBossBar(player);
                skyblockSession.updateBossBar();
                unpauseEffects(player, toWorld);
            }
        }

        NpcFactory.showNPCs(toWorld.getName(), player);
        SkyblockSidebar.showSidebar(player.getUniqueId(), toWorld.getName());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
    }



    private void removeWorldNpcs(String worldName) {
        Skblock.getNpcService().removeAllNpcsInWorld(worldName);
        Skblock.getNpcIds().remove(worldName);
        if (Bukkit.getWorld(worldName) != null) {
            Skblock.getNpcService().removeOrphansFromWorld(worldName);
        }
    }

    private void pauseEffects(Player player, World skyblockWorld) {
        PersistentDataContainer dataContainer = skyblockWorld.getPersistentDataContainer();
        NamespacedKey effectsKey = getEffectsKey(player.getUniqueId());
        if (dataContainer.has(effectsKey, PersistentDataType.STRING)) {
            return;
        }

        List<PotionEffect> effects = List.copyOf(player.getActivePotionEffects());
        if (effects.isEmpty()) {
            return;
        }

        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set(EFFECTS_TAG, effects);
        dataContainer.set(effectsKey, PersistentDataType.STRING, yamlConfiguration.saveToString());

        for (PotionEffect effect : effects) {
            player.removePotionEffect(effect.getType());
        }
    }

    private void unpauseEffects(Player player, World skyblockWorld) {
        PersistentDataContainer dataContainer = skyblockWorld.getPersistentDataContainer();
        NamespacedKey effectsKey = getEffectsKey(player.getUniqueId());
        String pausedEffects = dataContainer.get(effectsKey, PersistentDataType.STRING);
        if (pausedEffects == null) {
            return;
        }

        dataContainer.remove(effectsKey);
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        try {
            yamlConfiguration.loadFromString(pausedEffects);
        } catch (InvalidConfigurationException e) {
            plugin.getLogger().warning("Error retrieving effects for player " + player.name());
            return;
        }

        for (Object object : yamlConfiguration.getList(EFFECTS_TAG, List.of())) {
            if (object instanceof PotionEffect potionEffect) {
                player.addPotionEffect(potionEffect);
            }
        }

    }

    private NamespacedKey getEffectsKey(UUID playerId) {
        return new NamespacedKey(plugin, "effects_" + playerId);
    }


    private void updatePopulation() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        SkyblockSidebar.setPopulation(onlinePlayers.size());

        for (Player player : onlinePlayers) {
            SkyblockSidebar.setPopulationLine(player.getUniqueId());
        }
    }

}
