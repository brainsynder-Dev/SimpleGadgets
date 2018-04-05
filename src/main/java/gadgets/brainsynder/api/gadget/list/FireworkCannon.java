package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class FireworkCannon extends Gadget {
    public FireworkCannon(GadgetPlugin plugin) {
        super(plugin, "firework_cannon");
    }

    @Override
    public void run(User user) {

        List<Location> locations = getPlugin().getUtilities().getStraightLine(user.getPlayer(), 10);
        int i = 1;
        for (Location l : locations) {
            if (l.getBlock() != null) {
                if (l.getBlock().getType().isSolid()) {
                    final org.bukkit.entity.Firework fw = getPlugin().getEntityUtils().spawnMob(l, org.bukkit.entity.Firework.class);
                    FireworkMeta meta = fw.getFireworkMeta();
                    meta.addEffect(getPlugin().getUtilities().randomEffect());
                    fw.setFireworkMeta(meta);
                    fw.setMetadata("NODAMAGE", new FixedMetadataValue(getPlugin().getPlugin(), "NODAMAGE"));
                    new BukkitRunnable() {
                        @Override public void run() {
                            fw.detonate();
                        }
                    }.runTaskLaterAsynchronously(getPlugin().getPlugin(), 2);
                    break;
                }
            }
            if ((i >= locations.size())) {
                final org.bukkit.entity.Firework fw = getPlugin().getEntityUtils().spawnMob(l, org.bukkit.entity.Firework.class);
                FireworkMeta meta = fw.getFireworkMeta();
                meta.addEffect(getPlugin().getUtilities().randomEffect());
                fw.setFireworkMeta(meta);
                fw.setMetadata("NODAMAGE", new FixedMetadataValue(getPlugin().getPlugin(), "NODAMAGE"));
                new BukkitRunnable() {
                    @Override public void run() {
                        fw.detonate();
                    }
                }.runTaskLaterAsynchronously(getPlugin().getPlugin(), 2);
                break;
            }
            i++;
        }
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.FIREWORK).withName("&eFirework Cannon");
    }
}
