package gadgets.brainsynder.items.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.items.CustomItem;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;

public class RemoveGadget extends CustomItem {
    public RemoveGadget(GadgetPlugin plugin) {
        super(plugin, "remove_gadget");
    }

    @Override
    public void onClick(User user) {
        if (user.hasGadget()) user.removeGadget();
    }

    @Override
    public int getDefaultSlot() {
        return 50;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.BARRIER).withName("&cRemove Gadget");
    }
}
