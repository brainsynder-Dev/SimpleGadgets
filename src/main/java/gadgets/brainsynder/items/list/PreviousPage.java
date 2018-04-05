package gadgets.brainsynder.items.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.items.CustomItem;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;

public class PreviousPage extends CustomItem {
    public PreviousPage(GadgetPlugin plugin) {
        super(plugin, "previous_page");
    }

    @Override
    public boolean addToInventory(int page) {
        return (page > 1);
    }

    @Override
    public void onClick(User user, int page) {
        if (page <= 1) return;

        getPlugin().getSelectionMenu().openMenu(user.getPlayer(), page-1);
    }

    @Override
    public int getDefaultSlot() {
        return 46;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SKULL_ITEM)
                .withData(3)
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI1OTliZDk4NjY1OWI4Y2UyYzQ5ODg1MjVjOTRlMTlkZGQzOWZhZDA4YTM4Mjg0YTE5N2YxYjcwNjc1YWNjIn19fQ==")
                .withName("&6&l<&m----").addLore("&7Click Here to go to", "&7the previous page");
    }
}
