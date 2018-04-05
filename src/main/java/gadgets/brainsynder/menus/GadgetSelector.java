/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package gadgets.brainsynder.menus;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.items.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.storage.IStorage;

import java.util.HashMap;
import java.util.Map;

public class GadgetSelector {
    private GadgetPlugin plugin;
    private static Map<String, Integer> pageMap = new HashMap<>();

    public GadgetSelector (GadgetPlugin plugin) {
        this.plugin = plugin;
    }

    public void openMenu(Player player, int page) {
        pageMap.put(player.getUniqueId().toString(), page);
        Inventory inv = Bukkit.createInventory(new Handler (), 54, "Gadgets: " + page + "/" + plugin.getPages().totalPages());
        for (int i = 0; i < (inv.getSize()); i++) {
            ItemMaker maker = new ItemMaker(Material.STAINED_GLASS_PANE, (byte) 8);
            maker.setName(" ");
            inv.setItem(i, maker.create());
        }


        IStorage<String> slots = plugin.getSlots().copy();
        while (slots.hasNext()) {
            int i = -1;
            try {
                i = Integer.parseInt(slots.next());
            } catch (NumberFormatException ignored) {}

            if ((i > -1) && (i <= 54)) {
                inv.setItem((i - 1), new ItemStack(Material.AIR));
            }
        }

        for (CustomItem item : plugin.getItemManager().getItems()) {
            if (item.addToInventory(page)) inv.setItem(item.getSlot(), item.getItemBuilder().build());
        }

        for (Gadget gadget : plugin.getPages().getPage(page)) {
            inv.addItem(gadget.getItem().build());
        }

        player.openInventory(inv);
    }

    public int getPage (Player player) {
        return pageMap.getOrDefault(player.getUniqueId().toString(), 1);
    }

    public static class Handler implements InventoryHolder{
        @Override
        public Inventory getInventory() {
            return null;
        }
    }
}
