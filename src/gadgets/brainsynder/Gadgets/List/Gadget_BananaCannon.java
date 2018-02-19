package gadgets.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.trailer.AsyncTrailer;
import simple.brainsynder.trailer.IAsyncTrailer;

import java.util.ArrayList;
import java.util.List;

public class Gadget_BananaCannon extends Gadget {
    public Gadget_BananaCannon() {
        super(26, "banana_cannon", Material.INK_SACK, (byte) 11);
    }

    private List<Item> items = new ArrayList<>();
    @Override
    public void run(final Player p) {
        ItemMaker maker = new ItemMaker(Material.INK_SACK, (byte)11);
        maker.setName(String.valueOf(Math.random()));
        final Item drop = p.getWorld().dropItem(p.getEyeLocation(), maker.create());
        drop.setPickupDelay(Integer.MAX_VALUE);
        Vector vector = RandomRef.calculatePath(p);
        drop.setVelocity(vector);
        RandomRef.noArc(drop, vector);
        SoundPlayer.playSound(SoundMaker.ENTITY_EXPERIENCE_ORB_PICKUP, drop.getLocation());
        final IAsyncTrailer<Entity> trailer = new AsyncTrailer<>();
        trailer.setTarget(drop);
        trailer.setPlayer(p);
        final IStorage<ParticleMaker> iStorage = new StorageList<>();
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, Color.YELLOW));
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
                for (int i = 0; i < 5; i++) {
                    double x = -0.5F + (float)(Math.random() * 0.9D);
                    double y = 0.5D;
                    double z = -0.5F + (float)(Math.random() * 0.9D);

                    ItemMaker maker = new ItemMaker(Material.INK_SACK, (byte)11);
                    maker.setName(String.valueOf(Math.random()));

                    final Item fb = p.getWorld().dropItem(drop.getLocation(), maker.create());
                    SoundPlayer.playSound(SoundMaker.ENTITY_CHICKEN_EGG, fb.getLocation(), 0.5F, 5.0F);
                    fb.setVelocity(new Vector(x, y, z));
                    fb.setCustomNameVisible(true);
                    fb.setCustomName("§e§lBANANA");
                    fb.setMetadata("banana", new FixedMetadataValue(Core.get(), "banana"));
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
                            if (fb.isValid())
                            fb.remove();
                        }
                    }.runTaskLater(getPlugin().getGadgetPlugin(), 50);
                }
                drop.remove();
            }
        }.runTaskLater(getPlugin().getGadgetPlugin(), 15);
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
