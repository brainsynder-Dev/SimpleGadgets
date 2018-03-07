/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package gadgets.brainsynder.commands.list;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.GadgetPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simple.brainsynder.commands.BaseCommand;

public class Command_Gadgets extends BaseCommand {
    private GadgetPlugin plugin;
    public Command_Gadgets(GadgetPlugin plugin) {
        super("gadgets");
        this.plugin = plugin;
    }

    @Override
    public void consoleSendCommandEvent(CommandSender sender, String[] args) {
        System.out.println ("SimpleGadgets >> You must be a player to open the Gadgets menu.");
    }

    @Override
    public void playerSendCommandEvent(Player player, String[] args) {
        plugin.getSelectionMenu().openMenu (player, 1);
        player.sendMessage (Core.getLanguage().getString("Messages.OpenMenu", true));
    }
}
