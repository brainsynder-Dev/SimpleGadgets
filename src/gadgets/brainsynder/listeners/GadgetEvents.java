package gadgets.brainsynder.listeners;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.event.gadget.GadgetActivateEvent;
import gadgets.brainsynder.api.event.gadget.GadgetProjectileHitEvent;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.Cooldown;
import gadgets.brainsynder.utilities.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import simple.brainsynder.nms.ITitleMessage;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.Reflection;

public class GadgetEvents implements Listener {

    public GadgetEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(this, Core.get());
    }

    @EventHandler
        private void onSpawn(EntitySpawnEvent e) {
            if (e.isCancelled()) {
                if (EntityUtils.spawnMe && e.getEntity().hasMetadata("Spawnable")) {
                    e.setCancelled(false);
                    EntityUtils.spawnMe = false;
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
                SoundMaker.ENTITY_GENERIC_EAT.playSound(e.getItem().getLocation());
                e.getItem().remove();
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 1));
                return;
            }
            if (e.getItem().hasMetadata("banana")) {
                e.setCancelled(true);
                SoundMaker.BLOCK_TRIPWIRE_CLICK_ON.playSound(e.getItem().getLocation());
                e.getItem().remove();
                ITitleMessage message = Reflection.getTitleMessage();
                message.sendMessage(e.getPlayer(), 0, 1, 0, "§e§lBANANA!");
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
            User user = Core.get().getManager().getUser(player);
            Gadget gadget = user.getGadget();
            e.setCancelled(true);
            GadgetActivateEvent event = new GadgetActivateEvent(gadget, player);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled())
                return;
            if (!Cooldown.hasCooldown(player, gadget)) {
                Cooldown.giveCooldown(player, gadget);
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    gadget.onBlockClick(user, e.getClickedBlock());
                    return;
                }
                gadget.run(user);
            }
        }

        @EventHandler
        public void onMove(PlayerMoveEvent event) {
            Player player = event.getPlayer();
            if (((event.getFrom().getBlockX() != event.getTo().getBlockX()) ||
                    (event.getFrom().getBlockY() != event.getTo().getBlockY()) ||
                    (event.getFrom().getBlockZ() != event.getTo().getBlockZ()))) {

                User user = Core.get().getManager().getUser(player);
                Gadget gadget = user.getGadget();
                if (gadget == null) {
                    return;
                }
                gadget.onUserMove(user);
            }
        }

        @EventHandler
        private void onProjHit(ProjectileHitEvent e) {
            if (!e.getEntity().hasMetadata("GadgetProj")) {
                return;
            }
            if (e.getEntity().getShooter() instanceof Player) {
                Player player = (Player) e.getEntity().getShooter();
                User user = Core.get().getManager().getUser(player);
                Gadget gadget = user.getGadget();
                if (gadget == null) {
                    return;
                }
                GadgetProjectileHitEvent event = new GadgetProjectileHitEvent(gadget, e.getEntity());
                Bukkit.getServer().getPluginManager().callEvent(event);
                gadget.onProjectileHit(user, event.getProjectile());
                event.getProjectile().remove();
            }
        }

        @EventHandler
        private void onDrop(PlayerDropItemEvent e) {
            Player player = e.getPlayer();
            User user = Core.get().getManager().getUser(player);
            Gadget gadget = user.getGadget();
            ItemStack dropped = e.getItemDrop().getItemStack();
            if (dropped.isSimilar(gadget.getItem().build())) {
                e.setCancelled(true);
                user.removeGadget();
            }
        }

        @EventHandler
        private void onLeave(PlayerQuitEvent e) {
            Player player = e.getPlayer();
            User user = Core.get().getManager().getUser(player);
            Gadget gadget = user.getGadget();
            if (player.getInventory().contains(gadget.getItem().build())) {
                player.getInventory().remove(gadget.getItem().build());
            }
        }

        @EventHandler
        private void onKick(PlayerKickEvent e) {
            Player player = e.getPlayer();
            User user = Core.get().getManager().getUser(player);
            Gadget gadget = user.getGadget();
            if (player.getInventory().contains(gadget.getItem().build())) {
                player.getInventory().remove(gadget.getItem().build());
            }
        }

        @EventHandler
        private void onDeath(PlayerDeathEvent e) {
            Player player = e.getEntity();

            User user = Core.get().getManager().getUser(player);
            Gadget gadget = user.getGadget();
            if (e.getDrops().contains(gadget.getItem().build())) {
                e.getDrops().remove(gadget.getItem().build());
            }
            if (player.getInventory().contains(gadget.getItem().build())) {
                player.getInventory().remove(gadget.getItem().build());
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
        }
}
