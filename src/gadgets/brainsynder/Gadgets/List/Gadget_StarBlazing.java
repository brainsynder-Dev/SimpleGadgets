package gadgets.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.trailer.AsyncTrailer;
import simple.brainsynder.trailer.IAsyncTrailer;

import java.util.ArrayList;
import java.util.List;

public class Gadget_StarBlazing extends Gadget {
    public Gadget_StarBlazing() {
        super(25, "star_blazing", Material.NETHER_STAR, (byte) 0);
    }

    private List<Item> items = new ArrayList<>();
    @Override
    public void run(final Player p) {
        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(String.valueOf(Math.random()));
        item.setItemMeta(itemMeta);
        final Item drop = p.getWorld().dropItem(p.getEyeLocation(), item);
        drop.setPickupDelay(Integer.MAX_VALUE);
        Vector vector = RandomRef.calculatePath(p);
        drop.setVelocity(vector);
        RandomRef.noArc(drop, vector);
        SoundPlayer.playSound(SoundMaker.ENTITY_EXPERIENCE_ORB_PICKUP, drop.getLocation());
        final IAsyncTrailer<Entity> trailer = new AsyncTrailer<>();
        trailer.setTarget(drop);
        trailer.setPlayer(p);
        final IStorage<ParticleMaker> iStorage = new StorageList<>();
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, Color.RED));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, Color.ORANGE));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, Color.YELLOW));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, Color.GREEN));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, Color.AQUA));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, Color.BLUE));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, Color.PURPLE));
        trailer.setParticles(iStorage);
        trailer.start((JavaPlugin) getPlugin().getGadgetPlugin());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!drop.isValid()) {
                    cancel();
                    return;
                }
                trailer.clear();
                for (int i = 0; i < 7; i++) {
                    double x = -0.5F + (float)(Math.random() * 0.9D);
                    double y = 0.5D;
                    double z = -0.5F + (float)(Math.random() * 0.9D);

                    ItemStack is = new ItemStack(Material.NETHER_STAR, 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(String.valueOf (Math.random()));
                    is.setItemMeta(im);

                    final Item fb = p.getWorld().dropItem(drop.getLocation(), is);
                    SoundPlayer.playSound(SoundMaker.ENTITY_EXPERIENCE_ORB_PICKUP, fb.getLocation(), 0.5F, 5.0F);
                    fb.setVelocity(new Vector(x, y, z));
                    fb.setMetadata("takeable", new FixedMetadataValue(Core.get(), "takeable"));
                    final IAsyncTrailer<Entity> trailer = new AsyncTrailer<>();
                    trailer.setTarget(fb);
                    trailer.setPlayer(p);
                    final IStorage<ParticleMaker> storage = new StorageList<>();
                    storage.add(iStorage.get(i));
                    trailer.setParticles(storage);
                    trailer.start((JavaPlugin) getPlugin().getGadgetPlugin());
                    items.add(fb);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            trailer.clear();
                            fb.remove();
                        }
                    }.runTaskLater(getPlugin().getGadgetPlugin(), 50);
                }
                drop.remove();
            }
        }.runTaskLater(getPlugin().getGadgetPlugin(), 15);
    }

    private void launchStars (Location location) {
        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(String.valueOf(Math.random()));
        item.setItemMeta(itemMeta);
        final Item drop = location.getWorld().dropItem(location, item);
        drop.setPickupDelay(Integer.MAX_VALUE);
        double x = -0.5F + (float) (Math.random() * 0.3D);
        double y = 0.5D;
        double z = -0.5F + (float) (Math.random() * 0.3D);
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
