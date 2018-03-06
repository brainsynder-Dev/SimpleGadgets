package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.gadget.GadgetInfo;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.trailer.AsyncTrailer;
import simple.brainsynder.trailer.IAsyncTrailer;

import java.util.Arrays;

@GadgetInfo(cancelFall = true)
public class Rocket extends Gadget {
    private boolean isUsing = false;
    public Rocket(GadgetPlugin plugin) {
        super(plugin, "rocket");
    }

    @Override
    public void loadExtraTags() {
        setDefault("cooldown", 15);
    }

    @Override
    public void run(User user) {
        Player p = user.getPlayer();
        p.setMetadata("NOFALL", new FixedMetadataValue(getPlugin().getPlugin(), "NOFALL"));
        Firework firework = getPlugin().getEntityUtils().spawnMob(p.getLocation(), Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.setPower(2);
        firework.setFireworkMeta(fireworkMeta);
        firework.setPassenger(p);
        removableEntities.add(firework);
        isUsing = true;

        IAsyncTrailer<Entity> trailer = new AsyncTrailer<>();
        trailer.setTarget(firework);
        trailer.setPlayer(p);
        IStorage<ParticleMaker> iStorage = new StorageList<>();
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.LAVA, 5, 0.2));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.CLOUD, 5, 0.2));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.FLAME, 5, 0.2));
        trailer.setParticles(iStorage);
        trailer.start((JavaPlugin) getPlugin().getPlugin());

        checkGround(p);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!getPlugin().getEntityUtils().isValid(p)) return;
                if (!getPlugin().getEntityUtils().isValid(firework)) return;
                firework.eject();
                new ParticleMaker(ParticleMaker.Particle.EXPLOSION_HUGE, 5, 1)
                        .sendToLocation(firework.getLocation());
                firework.remove();
            }
        }.runTaskLater(getPlugin().getPlugin(), 20 * 4);
    }

    private void checkGround(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!getPlugin().getEntityUtils().isValid(p)) {
                    isUsing = false;
                    return;
                }
                if (!isUsing) return;
                if (getPlugin().getBlockUtils().isOnGround(p)) {
                    isUsing = false;
                    return;
                }
                checkGround(p);
            }
        }.runTaskLater(getPlugin().getPlugin(), 20);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.FIREWORK)
                .withName("&eRocket")
                .withLore(Arrays.asList("&7Be like SpaceX"));
    }

    public boolean isUsing() {
        return isUsing;
    }
}
