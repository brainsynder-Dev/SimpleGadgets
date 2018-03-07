package gadgets.brainsynder.items;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    private List<CustomItem> items = new ArrayList<>();

    public void register (CustomItem item) {
        item.loadDefaults();
        item.save();
        item.reload();
        item.load();
        items.add(item);
    }

    public CustomItem getItem (ItemStack itemstack) {
        for (CustomItem item : items) {
            if (item.getItemBuilder().isSimilar(itemstack)) return item;
        }

        return null;
    }
}
