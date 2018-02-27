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
import gadgets.brainsynder.files.JSONFile;
import gadgets.brainsynder.utilities.ItemBuilder;
import old.brainsynder.Gadgets.List.*;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;

import java.io.File;

public abstract class Gadget extends JSONFile {
    public static final Gadget FUN_CANNON = new Gadget_FunCannon();
    public static final Gadget BAT_BLASTER = new Gadget_BatBlaster();
    public static final Gadget TRAIL_BLAZER = new Gadget_TrailBlazer();
    public static final Gadget CONFETTI = new Gadget_Confetti();
    public static final Gadget EXPLOSIVE_SNOWBALL = new Gadget_ExplosiveSnowball();
    public static final Gadget WATER_BOMB = new Gadget_WaterBomb();
    public static final Gadget FIREWORKS = new Gadget_Firework();
    public static final Gadget MELON_BLASTER = new Gadget_MelonBlaster();
    public static final Gadget QUAKE_GUN = new Gadget_QuakeGun();
    public static final Gadget WINTER_BREEZE = new Gadget_WinterBreeze();
    public static final Gadget NETHER_BLAZE = new Gadget_NetherBlaze();
    public static final Gadget PAINT_SPRAYER = new Gadget_Paintsprayer();
    public static final Gadget PAINT_TRAIL = new Gadget_PaintTrail();
    public static final Gadget FIRE_BENDER = new Gadget_FireBender();
    public static final Gadget NATURE_WIND = new Gadget_NatureWind();
    public static final Gadget SHEEP_BOMB = new Gadget_SheepBomb();
    public static final Gadget FALL_SCARE = new Gadget_FallScare();
    public static final Gadget ROCKET = new Gadget_Rocket();
    public static final Gadget PARTICLE_LAZER = new Gadget_Lazer();
    public static final Gadget POOP_BOMB = new Gadget_PoopBomb();
    public static final Gadget NINJA_VANISH = new Gadget_NinjaVanish();
    public static final Gadget BIRTHDAY_CANNON = new Gadget_BirthdayCannon();
    public static final Gadget BBQ_CANNON = new Gadget_BBQ();
    public static final Gadget FREEZE_BOMB = new Gadget_FreezeBomb();
    public static final Gadget STAR_BLAZING = new Gadget_StarBlazing();
    public static final Gadget BANANA_CANNON = new Gadget_BananaCannon();
    public static final Gadget GRAVITY_SURGE = new Gadget_GravitySurge();
    public static final Gadget PANCAKE_CANNON = new Gadget_PancakeCannon();
    public static final Gadget FIREWORK_CANNON = new Gadget_FireworkCannon();

    private String idName;
    private GadgetPlugin plugin;
    private String _PERMISSION_, _NAME_;
    private boolean _ENABLED_;
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

    public int getCooldown() {
        return _COOLDOWN_;
    }

    public void run(Player player) {}
    public void onBlockClick(Player player, Block block) {
        run(player);
    }
    public void onUserMove(Player player) {}
    public void onProjectileHit(Projectile projectile) {}

    public ItemBuilder getItem() {
        return _BUILDER_;
    }
    public abstract ItemBuilder getDefaultItem();

    /**
     *
     * @return idName is the Identifier name for the gadget (All lowercase and no spaces)
     */
    public String getIdName() {
        return this.idName;
    }

    public static class Listeners implements Listener {
        public Listeners() {
            Bukkit.getServer().getPluginManager().registerEvents(this, Core.get());
        }

        /*@EventHandler
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
