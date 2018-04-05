package gadgets.brainsynder.api;

import gadgets.brainsynder.GadgetManager;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.items.ItemManager;
import gadgets.brainsynder.menus.GadgetSelector;
import gadgets.brainsynder.utilities.BlockUtils;
import gadgets.brainsynder.utilities.EntityUtils;
import gadgets.brainsynder.utilities.Utils;
import gadgets.brainsynder.utilities.VelocityUtils;
import org.bukkit.plugin.Plugin;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.utils.ObjectPager;

public interface GadgetPlugin {

    Plugin getPlugin();

    GadgetManager getManager();

    EntityUtils getEntityUtils();

    BlockUtils getBlockUtils();

    VelocityUtils getVelocityUtils();

    Utils getUtilities();

    // These methods are for the Menu
    IStorage<String> getSlots();

    ItemManager getItemManager();

    GadgetSelector getSelectionMenu();

    ObjectPager<Gadget> getPages();
}
