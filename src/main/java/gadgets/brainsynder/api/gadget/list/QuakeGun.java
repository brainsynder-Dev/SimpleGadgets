package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;

public class QuakeGun extends Gadget {
    public QuakeGun(GadgetPlugin plugin) {
        super(plugin, "quake_gun");
    }

    @Override
    public void run(User user) {
        for (Location location : getPlugin().getUtilities().getStraightLine(user.getPlayer(), 10)) {
            SoundMaker.ENTITY_FIREWORK_LAUNCH.playSound(location);
            new ParticleMaker(ParticleMaker.Particle.SPELL_INSTANT, 5, 0.1)
                    .sendToLocation(location);
        }
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.DIAMOND_HOE).withName("&eQuakeGun");
    }
}
