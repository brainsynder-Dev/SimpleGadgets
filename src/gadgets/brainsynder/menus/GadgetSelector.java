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

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.gadget.Gadget;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.storage.IStorage;

public class GadgetSelector {

    public static void openMenu(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, "Gadgets: " + page + "/" + Core.get().getPages().getMaxPages());
        for (int i = 0; i < (inv.getSize()); i++) {
            ItemMaker maker = new ItemMaker(Material.STAINED_GLASS_PANE, (byte) 8);
            maker.setName(" ");
            inv.setItem(i, maker.create());
        }


        IStorage<String> slots = Core.getSlots().copy();
        while (slots.hasNext()) {
            int i = -1;
            try {
                i = Integer.parseInt(slots.next());
            } catch (NumberFormatException ignored) {}

            if ((i > -1) && (i <= 54)) {
                inv.setItem((i - 1), new ItemStack(Material.AIR));
            }
        }
        inv.setItem(Core.get().getRemoveGadget().getSlot(), Core.get().getRemoveGadget().getItem());
        if (Core.get().getPages().getMaxPages() > page) {
            inv.setItem(Core.get().getNext().getSlot(), Core.get().getNext().getItem());
        }
        if (page > 1) {
            inv.setItem(Core.get().getBack().getSlot(), Core.get().getBack().getItem());
        }

        for (String s : Core.get().getPages().getPage(page)) {
            Gadget gadget = Gadget.getByIdName (s);
            inv.addItem(gadget.getItemStack());
        }

        player.openInventory(inv);
    }
}
