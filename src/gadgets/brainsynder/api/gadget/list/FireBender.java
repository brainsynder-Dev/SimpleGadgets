package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.event.GadgetListener;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ParticleMaker;

import java.util.Arrays;

public class FireBender extends Gadget implements GadgetListener {
    private boolean isUsing = false;
    public FireBender(GadgetPlugin plugin) {
        super(plugin, "fire_bender");
    }

    @Override
    public void onRemove() {
        super.onRemove();
        isUsing = false;
    }

    @Override
    public void run(User user) {
        Player p = user.getPlayer();
        p.setFireTicks(95);
        isUsing = true;
        BukkitRunnable flameParticles = new BukkitRunnable() {
            @Override
            public void run() {
                if (isRemoved()) {
                    cancel();
                    p.setFireTicks(0);
                    return;
                }
                if (!getPlugin().getEntityUtils().isValid(p)) {
                    cancel();
                    return;
                }

                Location location = p.getLocation().add(0.0D, 1.0D, 0.0D);
                new ParticleMaker(ParticleMaker.Particle.LAVA, 2, 0, 0.2, 0)
                        .sendToLocation(location);
                new ParticleMaker(ParticleMaker.Particle.SMOKE_NORMAL, 2, 0, 0.2, 0)
                        .sendToLocation(location);
            }
        };
        flameParticles.runTaskTimer(getPlugin().getPlugin(), 0, 2);
        new BukkitRunnable() {
            @Override
            public void run() {
                isUsing = false;
                flameParticles.cancel();
            }
        }.runTaskLater(getPlugin().getPlugin(), 20 * 5);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.FLINT).withName("&eFire Bender").withLore(Arrays.asList("&cIt's Lit Fam ;)"));
    }

    public boolean isUsing() {
        return isUsing;
    }
}
