/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package gadgets.brainsynder.api.gadget;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.files.JSONFile;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class Gadget extends JSONFile {
    protected List<Item> removableItems = new ArrayList<>();
    protected List<Entity> removableEntities = new ArrayList<>();
    private String idName;
    private GadgetPlugin plugin;
    private String _PERMISSION_, _NAME_;
    private boolean _ENABLED_, removed = false;
    private int _COOLDOWN_;
    private ItemBuilder _BUILDER_;

    public void load () {
        _COOLDOWN_ = getInteger("cooldown");
        _ENABLED_ = getBoolean("enabled");
        _PERMISSION_ = getString("permission", false);
        _NAME_ = getString("name", false);
        _BUILDER_ = ItemBuilder.fromJSON(getObject("item"));
    }


    public Gadget(GadgetPlugin plugin, String idName) {
        super(new File(new File(plugin.getPlugin().getDataFolder().toString()+File.separator+"Gadgets"), idName+".json"));
        this.plugin = plugin;
        this.idName = idName.toLowerCase().replace(" ", "_");
    }

    public GadgetPlugin getPlugin () {
        return plugin;
    }

    public void loadExtraTags() {}

    /**
     * Does the player have permission to use this gadget?
     */
    public boolean hasPermission(Player player) {
        if (!Core.getLanguage().getBoolean("Needs-Permission"))
            return true;

        return (player.hasPermission(getPermission())) || (player.hasPermission("SimpleGadgets.gadget.*"));
    }
    public String getPermission() {
        return _PERMISSION_;
    }

    public String getName() {
        return _NAME_;
    }

    public boolean isEnabled() {
        return _ENABLED_;
    }

    public boolean isRemoved() {
        return removed;
    }

    public int getCooldown() {
        return _COOLDOWN_;
    }

    /**
     * This method is fired when the player removes their gadget
     *
     * Used to clean up... aka kill mobs/lists/ETC...
     */
    public void onRemove () {
        removed = true;
        if (!removableItems.isEmpty()) {
            removableItems.stream()
                    .filter(item -> getPlugin().getEntityUtils().isValid(item))
                    .forEach(Item::remove);
            removableItems.clear();
        }
        if (!removableEntities.isEmpty()) {
            removableEntities.stream()
                    .filter(entity -> getPlugin().getEntityUtils().isValid(entity))
                    .forEach(Entity::remove);
            removableEntities.clear();
        }
    }

    /**
     * This method is run when the player RightClicks their gadget item
     *
     * @param user
     *      The players' User instance
     */
    public void run(User user) {}

    /**
     * This method is run when the player Right Clicks a block with their gadget item
     *
     * @param user
     *      The players' User instance
     * @param block
     *      The block the player clicked (Right Clicked)
     */
    public void onBlockClick(User user, Block block) {
        run(user);
    }

    /**
     * This method is run when the player moves when their gadget is activated
     *
     * @param user
     *      The players' User instance
     */
    public void onUserMove(User user) {}

    /**
     * This method is called when the projectile their gadget shot lands
     *
     * @param user
     *      The players' User instance
     * @param projectile
     *      The Projectile the player shot
     */
    public void onProjectileHit(User user, Projectile projectile) {}
    public void onProjectileHit(User user, Projectile projectile, Location location) {}

    /**
     * This is the ItemBuilder method that can be used to checking the items
     */
    public ItemBuilder getItem() {
        return _BUILDER_;
    }

    /**
     * This is for loading the default item into the Gadget File...
     *
     * DO NOT USE
     */
    @Deprecated
    public abstract ItemBuilder getDefaultItem();

    public boolean hasGadgetInfo () {
        return getClass().isAnnotationPresent(GadgetInfo.class);
    }

    public GadgetInfo getGadgetInfo () {
        if (hasGadgetInfo()) return getClass().getAnnotation(GadgetInfo.class);
        return null;
    }

    /**
     *
     * @return idName is the Identifier name for the gadget (All lowercase and no spaces)
     */
    public String getIdName() {
        return this.idName;
    }

    //TODO: Make this its own class, and redo the code to fit the new system.
    public static class Listeners implements Listener {
        public Listeners() {
            Bukkit.getServer().getPluginManager().registerEvents(this, Core.get());
        }

        /*
        @EventHandler
        private void onSpawn(EntitySpawnEvent e) {
            if (e.isCancelled()) {
                if (spawnMe && e.getEntity().hasMetadata("Spawnable")) {
                    e.setCancelled(false);
                    spawnMe = false;
                }
            }
        }

        @EventHandler
        private void onBlockBreak(BlockBreakEvent e) {
            if (e.getBlock().hasMetadata("GadgetNoBlockBreak"))
                e.setCancelled(true);
        }

        @EventHandler
        public void pick(PlayerPickupItemEvent e) {
            if (e.getItem().hasMetadata("takeable")) e.setCancelled(true);
            if (e.getItem().hasMetadata("eatable")) {
                e.setCancelled(true);
                SoundPlayer.playSound(SoundMaker.ENTITY_GENERIC_EAT, e.getItem().getLocation());
                e.getItem().remove();
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 1));
                return;
            }
            if (e.getItem().hasMetadata("banana")) {
                e.setCancelled(true);
                SoundPlayer.playSound(SoundMaker.BLOCK_TRIPWIRE_CLICK_ON, e.getItem().getLocation());
                e.getItem().remove();
                ITitleMessage message = Reflection.getTitleMessage();
                message.sendMessage(e.getPlayer(), 0, 1, 0, "§e§lBANANA!");
                return;
            }
        }

        @EventHandler
        public void pick(InventoryPickupItemEvent e) {
            if ((e.getItem().hasMetadata("takeable")) || (e.getItem().hasMetadata("eatable"))) e.setCancelled(true);
        }

        @EventHandler
        public void onBlockChangeState(EntityChangeBlockEvent event) {
            if (event.getEntity().hasMetadata("GadgetFB")) {
                event.setCancelled(true);
                FallingBlock fb = (FallingBlock) event.getEntity();
                fb.getWorld().spigot().playEffect(fb.getLocation(), Effect.STEP_SOUND, fb.getBlockId(), fb.getBlockData(), 0.0F, 0.0F, 0.0F, 0.0F, 1, 32);
                event.getEntity().remove();
            }
        }

        @EventHandler
        private void onInteract(PlayerInteractEvent e) {
            if ((e.getAction() == Action.LEFT_CLICK_BLOCK) || (e.getAction() == Action.LEFT_CLICK_AIR)) {
                return;
            }
            Player player = e.getPlayer();
            if (player.getItemInHand() == null) {
                return;
            }
            if (player.getItemInHand().getType() == Material.AIR) {
                return;
            }
            if (!Variables.gadgetMap.containsKey(player.getName())) {
                return;
            }
            Gadget gadget = Variables.gadgetMap.getKey(player.getName());
            if (!Variables.isGadgetItem(gadget, player.getItemInHand())) {
                return;
            }
            e.setCancelled(true);
            GadgetActivateEvent event = new GadgetActivateEvent(gadget, player);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled())
                return;
            if (!Cooldown.hasCooldown(player, gadget)) {
                Cooldown.giveCooldown(player, gadget);
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    gadget.onBlockClick(player, e.getClickedBlock());
                    return;
                }
                gadget.run(player);
            }
        }

        @EventHandler
        public void onMove(PlayerMoveEvent event) {
            Player player = event.getPlayer();
            if (((event.getFrom().getBlockX() != event.getTo().getBlockX()) ||
                    (event.getFrom().getBlockY() != event.getTo().getBlockY()) ||
                    (event.getFrom().getBlockZ() != event.getTo().getBlockZ()))) {
                if (!Variables.gadgetMap.containsKey(player.getName())) {
                    return;
                }
                Gadget gadget = Variables.gadgetMap.getKey(player.getName());
                if (gadget == null) {
                    return;
                }
                gadget.onUserMove(player);
            }
        }

        @EventHandler
        private void onProjHit(ProjectileHitEvent e) {
            if (!e.getEntity().hasMetadata("GadgetProj")) {
                return;
            }
            if (e.getEntity().getShooter() instanceof Player) {
                Player player = (Player) e.getEntity().getShooter();
                if (!Variables.gadgetMap.containsKey(player.getName())) {
                    return;
                }
                Gadget gadget = Variables.gadgetMap.getKey(player.getName());
                if (gadget == null) {
                    return;
                }
                GadgetProjectileHitEvent event = new GadgetProjectileHitEvent(gadget, e.getEntity());
                Bukkit.getServer().getPluginManager().callEvent(event);
                gadget.onProjectileHit(event.getProjectile());
                event.getProjectile().remove();
            }
        }

        @EventHandler
        private void onDrop(PlayerDropItemEvent e) {
            Player player = e.getPlayer();
            if (!Variables.gadgetMap.containsKey(player.getName())) {
                return;
            }
            Gadget gadget = Variables.gadgetMap.getKey(player.getName());
            ItemStack dropped = e.getItemDrop().getItemStack();
            if (dropped.isSimilar(gadget.getItemStack())) {
                e.setCancelled(true);
                Variables.removeGadget(player);
            }
        }

        @EventHandler
        private void onLeave(PlayerQuitEvent e) {
            Player player = e.getPlayer();
            if (!Variables.gadgetMap.containsKey(player.getName())) {
                return;
            }
            Gadget gadget = Variables.gadgetMap.getKey(player.getName());
            if (player.getInventory().contains(gadget.getItemStack())) {
                player.getInventory().remove(gadget.getItemStack());
            }
        }

        @EventHandler
        private void onKick(PlayerKickEvent e) {
            Player player = e.getPlayer();
            if (!Variables.gadgetMap.containsKey(player.getName())) {
                return;
            }
            Gadget gadget = Variables.gadgetMap.getKey(player.getName());
            if (player.getInventory().contains(gadget.getItemStack())) {
                player.getInventory().remove(gadget.getItemStack());
            }
        }

        @EventHandler
        private void onDeath(PlayerDeathEvent e) {
            Player player = e.getEntity();
            if (!Variables.gadgetMap.containsKey(player.getName())) {
                return;
            }
            Gadget gadget = Variables.gadgetMap.getKey(player.getName());
            if (e.getDrops().contains(gadget.getItemStack())) {
                e.getDrops().remove(gadget.getItemStack());
            }
            if (player.getInventory().contains(gadget.getItemStack())) {
                player.getInventory().remove(gadget.getItemStack());
            }
        }

        @EventHandler
        private void onDamage(EntityDamageByEntityEvent e) {
            if (e.getDamager() instanceof Projectile) {
                Projectile proj = (Projectile) e.getDamager();
                if (proj.hasMetadata("GadgetProj")) {
                    e.setCancelled(true);
                }
            }
        }*/
    }
}
