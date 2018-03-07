package gadgets.brainsynder.items.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.items.CustomItem;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;

public class NextPage extends CustomItem {
    public NextPage(GadgetPlugin plugin) {
        super(plugin, "next_page");
    }

    @Override
    public boolean addToInventory(int page) {
        return (getPlugin().getPages().totalPages() > page);
    }

    @Override
    public void onClick(User user, int page) {
        if (getPlugin().getPages().totalPages() <= page) return;

        getPlugin().getSelectionMenu().openMenu(user.getPlayer(), page+1);
    }

    @Override
    public int getDefaultSlot() {
        return 54;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SKULL_ITEM)
                .withData(3)
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJmOTEwYzQ3ZGEwNDJlNGFhMjhhZjZjYzgxY2Y0OGFjNmNhZjM3ZGFiMzVmODhkYjk5M2FjY2I5ZGZlNTE2In19fQ==")
                .withName("&6&l&m----&6&l>").addLore("&7Click Here to go to", "&7the next page");
    }
}
