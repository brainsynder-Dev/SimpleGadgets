package gadgets.brainsynder.api;

import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.utilities.EntityUtils;
import gadgets.brainsynder.utilities.Utils;
import org.bukkit.plugin.Plugin;
import simple.brainsynder.utils.ObjectPager;

import java.util.List;

public interface GadgetPlugin {

    Plugin getPlugin();

    List<Gadget> getGadgets ();

    EntityUtils getEntityUtils ();

    Utils getUtilities ();

    ObjectPager<Gadget> getPages();
}
