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

public class PoopBomb extends Gadget {
    public PoopBomb(GadgetPlugin plugin) {
        super(plugin, "poop_bomb");
    }

    @Override
    public void run(User user) {
        Player p = user.getPlayer();

        ItemMaker maker = new ItemMaker(Material.WOOL, (byte)12);
        maker.setName(String.valueOf(Math.random()));
        final Item drop = p.getWorld().dropItem(p.getEyeLocation(), maker.create());
        drop.setPickupDelay(Integer.MAX_VALUE);
        Vector vector = getPlugin().getUtilities().calculatePath(p);
        drop.setVelocity(vector);
        removableItems.add(drop);
        getPlugin().getUtilities().noArc(drop, vector);
        SoundMaker.ENTITY_PLAYER_BURP.playSound(drop.getLocation(), 1.0F, 0.5F);
        IStorage<ParticleMaker> iStorage = new StorageList<>();
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, Color.YELLOW));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (isRemoved()) {
                    cancel();
                    return;
                }

                if (!getPlugin().getEntityUtils().isValid(drop)) {
                    cancel();
                    return;
                }

                for (int i = 0; i < 6; i++) {
                    double x = -0.5F + (float)(Math.random() * 0.9D);
                    double y = 0.5D;
                    double z = -0.5F + (float)(Math.random() * 0.9D);

                    ItemMaker maker = new ItemMaker(Material.INK_SACK, (byte)3);
                    maker.setName(String.valueOf(Math.random()));

                    final Item fb = p.getWorld().dropItem(drop.getLocation(), maker.create());
                    SoundMaker.ENTITY_GENERIC_EXPLODE.playSound(fb.getLocation(), 0.2F, 0.2F);
                    fb.setVelocity(new Vector(x, y, z));
                    fb.setMetadata("takeable", new FixedMetadataValue(getPlugin().getPlugin(), "takeable"));
                    final IAsyncTrailer<Entity> trailer = new AsyncTrailer<>();
                    trailer.setTarget(fb);
                    trailer.setPlayer(p);
                    IStorage<ParticleMaker> iStorage = new StorageList<>();
                    iStorage.add(new ParticleMaker(ParticleMaker.Particle.CLOUD, 5, 0.2, 0.2, 0.2));
                    trailer.setParticles(iStorage);
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
        return new ItemBuilder(Material.WOOL).withData(12).withName("&ePoop Bomb");
    }
}
