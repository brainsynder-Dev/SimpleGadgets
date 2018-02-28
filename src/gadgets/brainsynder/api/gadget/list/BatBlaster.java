package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ParticleMaker;

import java.util.ArrayList;
import java.util.List;

public class BatBlaster extends Gadget {
    private static List<Bat> bats = new ArrayList<>();
    public BatBlaster(GadgetPlugin plugin) {
        super(plugin, "Bat Blaster");
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (!bats.isEmpty()) {
            bats.stream().filter(bat -> getPlugin().getEntityUtils().isValid(bat))
                    .forEach(Bat::remove);
            bats.clear();
        }
    }

    @Override
    public void run(User player) {
        Player p = player.getPlayer();

        for (int i = 0; i < 10; i++) {
            Bat bat = getPlugin().getEntityUtils().spawnMob(p.getEyeLocation(), Bat.class);
            if (!getPlugin().getEntityUtils().isValid(bat)) continue;
            bats.add(bat);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (isRemoved()) {
                    cancel ();
                    return;
                }

                if (bats.isEmpty()) {
                    cancel ();
                    return;
                }

                if (!getPlugin().getEntityUtils().isValid(p)) {
                    bats.stream().filter(bat -> getPlugin().getEntityUtils().isValid(bat))
                            .forEach(Bat::remove);
                    bats.clear();
                    cancel();
                    return;
                }

                Location fwe = p.getEyeLocation().clone();
                for (Bat bat : bats) {
                    if (!getPlugin().getEntityUtils().isValid(bat)) continue;

                    new ParticleMaker (ParticleMaker.Particle.SMOKE_LARGE, 3,
                            0.2F, 0.2F, 0.2F).sendToLocation(bat.getLocation());
                    bat.setVelocity(fwe.getDirection().clone().multiply(0.9D));
                }
            }
        }.runTaskTimer(getPlugin().getPlugin(), 0, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                bats.stream().filter(bat -> getPlugin().getEntityUtils().isValid(bat))
                        .forEach(Bat::remove);
                bats.clear();
            }
        }.runTaskLater(getPlugin().getPlugin(), 59L);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.IRON_BARDING, 1).withName("&eBat Blaster");
    }
}
