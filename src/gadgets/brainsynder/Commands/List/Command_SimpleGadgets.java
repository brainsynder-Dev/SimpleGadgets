package gadgets.brainsynder.Commands.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simple.brainsynder.commands.BaseCommand;
import simple.brainsynder.utils.SpigotPluginHandler;

public class Command_SimpleGadgets extends BaseCommand {
    public Command_SimpleGadgets() {
        super("simplegadgets");
    }

    private void help(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§eSimpleGadgets §6>> §7Help pages §e1§6/§e1");
            sender.sendMessage("§6- §e/gadgets §c| §7Opens a menu to select your gadget.");
            sender.sendMessage("§6- §e/simplegadgets §c| §7Main SimpleGadgets command.");
            sender.sendMessage("§6- §e/simplegadgets reload §c| §7Reload the gadgets menu.");
            sender.sendMessage("§6- §e/simplegadgets version §c| §7Check the version and updates.");
            sender.sendMessage("§6- §e/simplegadgets gadgetslist §c| §7See a list of gadgets and stats about them.");
            sender.sendMessage("§6- §e/simplegadgets permissions §c| §7See a list of the gadget Permissions.");
            sender.sendMessage("§6- §e/simplegadgets author §c| §7Shows stats about the author.");
            sender.sendMessage("§eSimpleGadgets §6>> §7Help pages §e1§6/§e1");
        } else {
            Player player = (Player) sender;
            player.sendMessage("§eSimpleGadgets §6>> §7Help pages §e1§6/§e1");
            player.sendMessage("§6- §e/gadgets §c| §7Opens a menu to select your gadget.");
            player.sendMessage("§6- §e/simplegadgets §c| §7Main SimpleGadgets command.");
            if (player.hasPermission("SimpleGadgets.command.reload"))
                player.sendMessage("§6- §e/simplegadgets reload §c| §7Reload the gadgets menu.");
            if (player.hasPermission("SimpleGadgets.command.version"))
                player.sendMessage("§6- §e/simplegadgets version §c| §7Check the version and updates.");
            if (player.hasPermission("SimpleGadgets.command.gadgetslist"))
                player.sendMessage("§6- §e/simplegadgets gadgetslist §c| §7See a list of gadgets and stats about them.");
            if (player.hasPermission("SimpleGadgets.command.permissions"))
                player.sendMessage("§6- §e/simplegadgets permissions §c| §7See a list of the gadget Permissions.");
            player.sendMessage("§6- §e/simplegadgets author §c| §7Shows stats about the author.");
            player.sendMessage("§eSimpleGadgets §6>> §7Help pages §e1§6/§e1");
        }
    }

    @Override
    public void consoleSendCommandEvent(CommandSender sender, String[] args) {
        if (args.length == 0) {
            help(sender);
        } else if (args[0].equalsIgnoreCase("reload")) {
            Core.get().getGadgetPageMaker().getRaw().clear();
            for (Gadget gadget : Gadget.Variables.getGadgetList()) {
                if (gadget.isEnabled()) {
                    Core.get().getGadgetPageMaker().getRaw().add(gadget.getIdName());
                }
            }
            sender.sendMessage("§eSimpleGadgets §6>> §7Gadgets have been reloaded.");
        } else if (args[0].equalsIgnoreCase("version")) {
            for (String messages : SpigotPluginHandler.versionStats(Core.get())) {
                sender.sendMessage(messages);
            }
        } else if (args[0].equalsIgnoreCase("gadgetslist")) {
            for (String s : Core.get().getGadgetPageMaker().getRaw()) {
                Gadget gadget = Gadget.getByIdName(s);
                if (gadget != null)
                    sender.sendMessage("§6• §eId:§7" + gadget.getId() + "   §eCooldown:§7" + gadget.getCooldown() + "   §eIdName:§7" + gadget.getIdName());
            }
        } else if (args[0].equalsIgnoreCase("permissions")) {
            System.out.println("Gadget Permissions: ");
            for (Gadget gadget : Gadget.values()) {
                System.out.println("- Permission for the " + gadget.getActualName() + " gadget is " + gadget.getPermission());
            }
        } else if (args[0].equalsIgnoreCase("author")) {
            sender.sendMessage("§6Name:§e brainsynder");
            sender.sendMessage("§6Website:§e §nhttp://brainsynder.us/");
            sender.sendMessage("§6Spigot:§e §nhttp://spigotmc.org/members/brainsynder.35575");
        } else {
            help(sender);
        }
    }

    @Override
    public void playerSendCommandEvent(Player player, String[] args) {
        if (args.length == 0) {
            help(player);
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("SimpleGadgets.command.reload")) {
                player.sendMessage(Core.getLanguage().getString("Messages.No-Permission", true));
                return;
            }
            Core.get().getGadgetPageMaker().getRaw().clear();
            for (Gadget gadget : Gadget.Variables.getGadgetList()) {
                if (gadget.isEnabled()) {
                    Core.get().getGadgetPageMaker().getRaw().add(gadget.getIdName());
                }
            }
            player.sendMessage("§eSimpleGadgets §6>> §7Gadgets have been reloaded.");
        } else if (args[0].equalsIgnoreCase("version")) {
            if (!player.hasPermission("SimpleGadgets.command.version")) {
                player.sendMessage(Core.getLanguage().getString("Messages.No-Permission", true));
                return;
            }
            for (String messages : SpigotPluginHandler.versionStats(Core.get())) {
                player.sendMessage(messages);
            }
        } else if (args[0].equalsIgnoreCase("gadgetslist")) {
            if (!player.hasPermission("SimpleGadgets.command.gadgetsList")) {
                player.sendMessage(Core.getLanguage().getString("Messages.No-Permission", true));
                return;
            }
            for (String s : Core.get().getGadgetPageMaker().getRaw()) {
                Gadget gadget = Gadget.getByIdName(s);
                if (gadget != null)
                player.sendMessage("§6• §eId:§7" + gadget.getId() + "    §eCooldown:§7" + gadget.getCooldown() + "    §eIdName:§7" + gadget.getIdName());
            }
        } else if (args[0].equalsIgnoreCase("permissions")) {
            if (!player.hasPermission("SimpleGadgets.command.permissions")) {
                player.sendMessage(Core.getLanguage().getString("Messages.No-Permission", true));
                return;
            }
            player.sendMessage("§6Gadget Permissions have been printed in the Console/Logs");
            System.out.println("Gadget Permissions: ");
            for (Gadget gadget : Gadget.values()) {
                System.out.println("- Permission for the " + gadget.getActualName() + " gadget is " + gadget.getPermission());
            }
        } else if (args[0].equalsIgnoreCase("author")) {
            player.sendMessage("§6Name:§e brainsynder");
            player.sendMessage("§6Website:§e §nhttp://brainsynder.us/");
            player.sendMessage("§6Spigot:§e §nhttp://spigotmc.org/members/35575");
        } else {
            help(player);
        }
    }
}
