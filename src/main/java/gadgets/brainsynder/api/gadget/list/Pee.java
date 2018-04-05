package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import simple.brainsynder.sound.SoundMaker;

public class Pee extends Gadget {
    private BukkitRunnable peeTack = null;

    public Pee(GadgetPlugin plugin) {
        super(plugin, "pee");
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if ((peeTack != null) && (!peeTack.isCancelled())) peeTack.cancel();
    }

    @Override
    public void run(User user) {
        Player p = user.getPlayer();
        peeTack = new BukkitRunnable() {
            @Override
            public void run() {
                if (isRemoved()) {
                    cancel();
                    return;
                }

                if (!getPlugin().getEntityUtils().isValid(p)) {
                    cancel();
                    clearItems();
                    return;
                }
                Location loc = p.getLocation();
                loc.add(0.0D, 0.6D, 0.0D);
                Item item = getPlugin().getEntityUtils().launchItem(loc,
                        loc.getDirection().multiply(0.4D).add(new Vector(0.0D, 0.25D, 0.0D)),
                        new ItemBuilder(Material.GOLD_BLOCK).withName(String.valueOf(Math.random())).build());
                SoundMaker.BLOCK_NOTE_XYLOPHONE.playSound(p.getLocation(), 1F, 0.5F);
                removableItems.add(item);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        item.remove();
                    }
                }.runTaskLater(getPlugin().getPlugin(), 10);
            }
        };
        peeTack.runTaskTimer(getPlugin().getPlugin(), 0, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                peeTack.cancel();
                clearItems();
            }
        }.runTaskLater(getPlugin().getPlugin(), 20*4);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.GOLD_BLOCK).withName("&ePee");
    }
}
