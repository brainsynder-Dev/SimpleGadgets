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
import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.items.CustomItem;
import gadgets.brainsynder.menus.GadgetSelector;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class MenuListener implements Listener {
	private GadgetPlugin plugin;

	public MenuListener(GadgetPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onClick (InventoryClickEvent e) {
		if (e.getInventory ().getType () != InventoryType.CHEST) return;

		if (e.getWhoClicked () instanceof Player) {
			Player player = (Player)e.getWhoClicked ();
			if (!(e.getInventory().getHolder() instanceof GadgetSelector.Handler)) return;
			if (e.getInventory ().getTitle ().startsWith ("Gadgets: ")) {
                User user = plugin.getManager().getUser(player);
				if (e.getCurrentItem () == null || e.getCurrentItem ().getType () == Material.AIR) return;

				int currentPage = plugin.getSelectionMenu().getPage(player);
				e.setCancelled (true);
				e.setResult(Event.Result.DENY);

				CustomItem item = plugin.getItemManager().getItem(e.getCurrentItem());

				if (item != null) {
					item.onClick(user, currentPage);
					return;
				}
				// If the first check fails... then get the CustomItem from the Slot number
				item = plugin.getItemManager().getItem(e.getSlot());
				if (item != null) {
					item.onClick(user, currentPage);
					return;
				}

				Gadget gadget = plugin.getManager().getByItem (e.getCurrentItem(), true);
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
