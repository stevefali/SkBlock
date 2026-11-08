package com.steve.skblock.events.unrouted;


import com.steve.skblock.Skblock;
import com.steve.skblock.game.SkyblockSession;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.util.Vector;

import static com.steve.skblock.Skblock.SKYBLOCK_LOBBY_NAME;

public class ProtectServerEvents implements Listener {

    private static final String ADMIN_PERMISSION = "skyblock.admin";


    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (!isPlayerRestricted(event.getPlayer())) {
            return;
        }

        if (event.getAction() == Action.PHYSICAL) {
            event.setCancelled(true);
            return;
        }

        event.setUseInteractedBlock(Event.Result.DENY);

        ItemStack itemStack = event.getItem();
        if (itemStack != null && isRestrictedItem(itemStack.getType())) {
            event.setUseItemInHand(Event.Result.DENY);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractWithEntity(PlayerInteractEntityEvent event) {
        if (!isPlayerRestricted(event.getPlayer())) {
            return;
        }
        if (isEntityProtected(event.getRightClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        if (!isEntityProtected(event.getEntity())) {
            return;
        }

        Entity damager = event.getDamager();
        if (damager instanceof Projectile projectile && projectile.getShooter() instanceof Entity shooter) {
            damager = shooter;
        }
        if (damager instanceof Player player && isPlayerRestricted(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onHangingBreak(HangingBreakEvent event) {
        if (event instanceof HangingBreakByEntityEvent byEntityEvent
                && byEntityEvent.getRemover() instanceof Player player
                && !isPlayerRestricted(player)) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onHangingPlaced(HangingPlaceEvent event) {
        Player player = event.getPlayer();
        if (player != null && isPlayerRestricted(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onArmorStandInteract(PlayerArmorStandManipulateEvent event) {
        if (isPlayerRestricted(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBookEdited(PlayerEditBookEvent event) {
        int maxPages = 50;
        int maxPageLength = 1024;
        int maxTitleLength = 32;

        BookMeta bookMeta = event.getNewBookMeta();

        if (bookMeta.getPageCount() > maxPages) {
            event.setCancelled(true);
            return;
        }

        for (String page : bookMeta.getPages()) {
            if (page.length() > maxPageLength) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.isSigning() && bookMeta.hasTitle() && bookMeta.getTitle().length() > maxTitleLength) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (isPlayerRestricted(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onRegenerate(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player player && isPlayerRestricted(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemConsume(PlayerItemConsumeEvent event) {
        if (isPlayerRestricted(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDropItem(PlayerDropItemEvent event) {
        if (isPlayerRestricted(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player && isPlayerRestricted(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerHurt(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {

                SkyblockSession skyblockSession = Skblock.getSessionRegistry().getSession(player.getWorld());
                if (skyblockSession != null) {
                    skyblockSession.onPlayerFallInVoid(event);
                    return;
                }

                player.setFallDistance(0);
                player.setVelocity(new Vector(0, 0, 0));
                player.teleport(Skblock.getLobbySpawn());
            }
            if (isInLobby(player.getWorld())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectile(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player player && isPlayerRestricted(player)) {
            event.setCancelled(true);
        }
    }


    private boolean isInLobby(World world) {
        return world.getName().equals(SKYBLOCK_LOBBY_NAME);
    }

    private boolean isPlayerRestricted(Player player) {
        return isInLobby(player.getWorld()) && !player.hasPermission(ADMIN_PERMISSION);
    }

    private boolean isRestrictedItem(Material material) {
        return material.isBlock()
                || material.name().endsWith("_BUCKET")
                || material.name().endsWith("_SPAWN_EGG")
                || material == Material.ITEM_FRAME
                || material == Material.GLOW_ITEM_FRAME
                || material == Material.PAINTING
                || material == Material.ARMOR_STAND
                || isItemLaunchable(material);
    }

    private boolean isItemLaunchable(Material material) {
        return switch (material) {
            case SNOWBALL, EGG, SPLASH_POTION, LINGERING_POTION, EXPERIENCE_BOTTLE, BOW, CROSSBOW, TRIDENT, FIRE_CHARGE, WIND_CHARGE, ENDER_PEARL,
                 FISHING_ROD -> true;
            default -> false;
        };
    }

    private boolean isEntityProtected(Entity entity) {
        return entity instanceof Hanging || entity instanceof ArmorStand;
    }

}
