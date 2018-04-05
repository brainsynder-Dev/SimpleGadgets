package gadgets.brainsynder.items;

import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {
    private Map<String, CustomItem> items = new HashMap();

    public void register (CustomItem item) {
        item.loadDefaults();
        item.save();
        item.reload();
        item.load();
        items.put(item.getName(), item);
    }

    public CustomItem getItem (ItemStack itemstack) {
        for (CustomItem item : items.values()) {
            if (item.getItemBuilder().isSimilar(itemstack)) return item;
        }

        return null;
    }

    public CustomItem getItem (int slot) {
        for (CustomItem item : items.values()) {
            if (item.getSlot() == (slot+1)) return item;
        }

        return null;
    }

    public CustomItem getItem (String name) {
        return items.getOrDefault(name, null);
    }

    public Collection<CustomItem> getItems () {
        return items.values();
    }
}
