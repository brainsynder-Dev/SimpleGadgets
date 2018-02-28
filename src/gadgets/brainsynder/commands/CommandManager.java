package gadgets.brainsynder.commands;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.commands.list.Command_Gadgets;
import gadgets.brainsynder.commands.list.Command_SimpleGadgets;

public class CommandManager {
    public static void registerCommands (GadgetPlugin plugin) {
        new Command_Gadgets().registerCommand(Core.get());
        new Command_SimpleGadgets(plugin).registerCommand(Core.get());
    }
}
