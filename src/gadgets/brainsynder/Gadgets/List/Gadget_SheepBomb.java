package gadgets.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.Events.GadgetListener;
import gadgets.brainsynder.Gadgets.Gadget;
import gadgets.brainsynder.Utils.Cooldown;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.nms.IClearGoals;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.Reflection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gadget_SheepBomb extends Gadget implements GadgetListener {
    private List<Entity> sheeps = new ArrayList<> ();
    public Gadget_SheepBomb() {
        super(16, "sheep_bomb", Material.WOOL, (byte) 0);
    }

    @Override
    public void loadExtraTags() {
        super.loadExtraTags();
        Core.getLanguage().set(true, "Gadgets." + getIdName() + ".CooldownTime", 20);
    }

    private double time = 5.0;
    @Override
    public void run(final Player p) {
        Location loc = p.getLocation();
        loc.setY((double)(p.getLocation().getBlockY() + 1));
        final Sheep sheep = EntityRef.spawnMob(loc, Sheep.class);
        sheep.setNoDamageTicks(100000);
        sheep.setVelocity(p.getEyeLocation().getDirection().multiply(0.7D));
        IClearGoals clearGoals = Reflection.getClearGoals();
        if (clearGoals != null)
        clearGoals.clearGoals(sheep);
        sheeps.add (sheep);
        new BukkitRunnable() {
            boolean red = true;
            @Override
            public void run() {
                if (!sheep.isValid ()) {
                    cancel ();
                    Cooldown.removeCooldown(p);
                    return;
                }

                if(red) {
                    sheep.setColor(DyeColor.RED);
                } else {
                    sheep.setColor(DyeColor.WHITE);
                }
                SoundPlayer.playSound(SoundMaker.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, sheep.getLocation());

                red = !red;
                time -= 0.2;
                if(time < 0.5) {
                    ParticleRef.sendParticle(ParticleMaker.Particle.EXPLOSION_HUGE, sheep.getLocation());
                    SoundPlayer.playSound(SoundMaker.ENTITY_GENERIC_EXPLODE, sheep.getLocation());
                    sheeps.remove(sheep);
                    Random rand = new Random ();
                    final List<Item> items = new ArrayList<> ();
                    for (int i = 0; i < 64; i++) {
                        int data = rand.nextInt (15) + 1;
                        double x = -0.5F + (float)(Math.random() * 0.9D);
                        double y = 0.5D;
                        double z = -0.5F + (float)(Math.random() * 0.9D);
                        ItemStack is = new ItemStack(Material.WOOL, 1, (byte)data);
                        ItemMeta im = is.getItemMeta();
                        im.setDisplayName(String.valueOf (Math.random()));
                        is.setItemMeta(im);
                        final Item item = p.getWorld().dropItem(sheep.getLocation(), is);
                        item.setPickupDelay(Integer.MAX_VALUE);
                        item.setVelocity((new Vector(x, y, z)));
                        items.add (item);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (Item item : items) {
                                    item.remove ();
                                }
                            }
                        }.runTaskLater(getPlugin ().getGadgetPlugin(), 20 * 3);
                    }
                    sheep.remove();
                    cancel();
                    time = 5.0;
                }
            }
        }.runTaskTimer(getPlugin().getGadgetPlugin(), 0, (long) time);
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent event) {
        if(sheeps.contains(event.getEntity())) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onShear(EntityDamageEvent event) {
        if(sheeps.contains(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
