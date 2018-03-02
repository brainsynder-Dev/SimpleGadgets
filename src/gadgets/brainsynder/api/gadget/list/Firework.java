package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.meta.FireworkMeta;

public class Firework extends Gadget {
    public Firework(GadgetPlugin plugin) {
        super(plugin, "fireworks");
    }

    @Override
    public void run(User user) {
        org.bukkit.entity.Firework fw = getPlugin().getEntityUtils().spawnMob(user.getPlayer().getLocation(), org.bukkit.entity.Firework.class);
        FireworkMeta meta = fw.getFireworkMeta();
        meta.addEffect(getPlugin().getUtilities().randomEffect());
        meta.setPower(1);
        fw.setFireworkMeta(meta);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.FIREWORK).withName("&eFireworks");
    }
}
