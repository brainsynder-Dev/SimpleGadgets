/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package gadgets.brainsynder.listeners;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.menus.GadgetSelector;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class MenuListener implements Listener {

	@EventHandler
	public void onClick (InventoryClickEvent e) {
		if (e.getInventory ().getType () != InventoryType.CHEST) return;

		if (e.getWhoClicked () instanceof Player) {
			Player player = (Player)e.getWhoClicked ();
			if (e.getInventory ().getTitle ().startsWith ("Gadgets: ")) {
                User user = Core.get().getManager().getUser(player);
				if (e.getCurrentItem () == null || e.getCurrentItem ().getType () == Material.AIR) return;

				int currentPage;
				String[] menuArgs = e.getInventory ().getTitle ().split (" ");
				String[] pageArgs = menuArgs[ 1 ].split ("/");
				try {
					currentPage = Integer.parseInt (pageArgs[ 0 ]);
				}
				catch ( NumberFormatException ignored ) {
					currentPage = 1;
				}
				e.setCancelled (true);

				if (e.getSlot() == (Core.get().getRemoveGadget().getSlot())) {
					if (user.hasGadget())
						user.removeGadget();
					return;
				}
				if ((e.getSlot() == (Core.get().getBack().getSlot()))) {
					GadgetSelector.openMenu (player, ( currentPage - 1 ));
					return;
				}
				if ((e.getSlot() == (Core.get().getNext().getSlot()))) {
					GadgetSelector.openMenu (player, ( currentPage + 1 ));
					return;
				}

				if (!Core.getSlots().contains(String.valueOf((e.getSlot() + 1)))) return;

				Gadget gadget = Core.get().getManager().getByItem (e.getCurrentItem(), true);
				if (gadget == null) return;

				gadget.load(); // This is needed in order for all the gadget details to load
                if (gadget.hasPermission(player)) {
					user.setGadget(gadget);
					return;
				}

				player.sendMessage(Core.getLanguage().getString("Messages.No-Permission", true));
			}
		}
	}
}
