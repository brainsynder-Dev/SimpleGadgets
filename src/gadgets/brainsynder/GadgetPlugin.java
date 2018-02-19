package gadgets.brainsynder;

import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.plugin.Plugin;
import simple.brainsynder.utils.PageMaker;

import java.util.List;

public interface GadgetPlugin {

    Plugin getGadgetPlugin ();

    List<Gadget> getGadgets ();

    PageMaker getGadgetPageMaker ();
}
