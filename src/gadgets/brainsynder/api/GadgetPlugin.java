package gadgets.brainsynder.api;

import gadgets.brainsynder.GadgetManager;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.utilities.BlockUtils;
import gadgets.brainsynder.utilities.EntityUtils;
import gadgets.brainsynder.utilities.Utils;
import gadgets.brainsynder.utilities.VelocityUtils;
import org.bukkit.plugin.Plugin;
import simple.brainsynder.utils.ObjectPager;

public interface GadgetPlugin {

    Plugin getPlugin();

    GadgetManager getManager();

    EntityUtils getEntityUtils ();

    BlockUtils getBlockUtils ();

    VelocityUtils getVelocityUtils ();

    Utils getUtilities ();

    ObjectPager<Gadget> getPages();
}
