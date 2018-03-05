package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
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

public class BananaCannon extends Gadget {
    public BananaCannon(GadgetPlugin plugin) {
        super(plugin, "banana_cannon");
    }

    @Override
    public void run(User player) {
        Player p = player.getPlayer();

        ItemMaker maker = new ItemMaker(Material.INK_SACK, (byte)11);
        maker.setName(String.valueOf(Math.random()));
        final Item drop = p.getWorld().dropItem(p.getEyeLocation(), maker.create());
        drop.setPickupDelay(Integer.MAX_VALUE);
        Vector vector = getPlugin().getUtilities().calculatePath(p);
        drop.setVelocity(vector);
        removableItems.add(drop);
        getPlugin().getUtilities().noArc(drop, vector);
        SoundMaker.ENTITY_EXPERIENCE_ORB_PICKUP.playSound(drop.getLocation());
        IAsyncTrailer<Entity> trailer = new AsyncTrailer<>();
        trailer.setTarget(drop);
        trailer.setPlayer(p);
        IStorage<ParticleMaker> iStorage = new StorageList<>();
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, Color.YELLOW));
        trailer.setParticles(iStorage);
        trailer.start((JavaPlugin) getPlugin().getPlugin());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (isRemoved()) {
                    cancel();
                    trailer.clear();
                    return;
                }

                if (!getPlugin().getEntityUtils().isValid(drop)) {
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
                    SoundMaker.ENTITY_CHICKEN_EGG.playSound(fb.getLocation(), 0.5F, 5.0F);
                    fb.setVelocity(new Vector(x, y, z));
                    fb.setCustomNameVisible(true);
                    fb.setCustomName("§e§lBANANA");
                    fb.setMetadata("banana", new FixedMetadataValue(getPlugin().getPlugin(), "banana"));
                    IAsyncTrailer<Entity> trailer = new AsyncTrailer<>();
                    trailer.setTarget(fb);
                    trailer.setPlayer(p);
                    IStorage<ParticleMaker> storage = new StorageList<>();
                    storage.add(iStorage.get(i));
                    trailer.setParticles(storage);
                    trailer.start((JavaPlugin) getPlugin().getPlugin());
                    removableItems.add(fb);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            trailer.clear();
                            if (getPlugin().getEntityUtils().isValid(fb)) fb.remove();
                        }
                    }.runTaskLater(getPlugin().getPlugin(), 50);
                }
                drop.remove();
            }
        }.runTaskLater(getPlugin().getPlugin(), 15);

    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.INK_SACK, 1)
                .withData(11)
                .withName("&eBanana Cannon");
    }
}
