package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.SkullMaker;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.StringAPI;

public class PancakeCannon extends Gadget {
    public PancakeCannon(GadgetPlugin plugin) {
        super(plugin, "pancake_cannon");
    }

    @Override
    public void run(User user) {
        Player player = user.getPlayer();
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i >= 8) {
                    cancel();
                    return;
                }
                if (!getPlugin().getEntityUtils().isValid(player)) {
                    cancel();
                    return;
                }
                StringAPI texture = new StringAPI("http://textures.minecraft.net/texture/77dac695a1cac324cbc2a2871b4c77ccfca5481e7a7cd3c6f350bde4bb8827d4");
                Item item = getPlugin().getEntityUtils().launchItem(player, new SkullMaker().setUrl(texture.toSkinURL()).setName("" + Math.random()).create());
                removableItems.add(item);
                SoundMaker.UI_BUTTON_CLICK.playSound(player.getLocation());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!getPlugin().getEntityUtils().isValid(item)) {
                            cancel();
                            return;
                        }
                        item.remove();
                    }
                }.runTaskLaterAsynchronously(getPlugin().getPlugin(), 15);
                i++;
            }
        }.runTaskTimer(getPlugin().getPlugin(), 0, 10);

    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.INK_SACK).withData(8).withName("&ePancake Cannon");
    }
}
