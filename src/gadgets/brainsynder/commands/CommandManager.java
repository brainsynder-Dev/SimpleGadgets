package gadgets.brainsynder.commands;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.commands.list.Command_Gadgets;
import gadgets.brainsynder.commands.list.Command_SimpleGadgets;
import simple.brainsynder.commands.BaseCommand;

public class CommandManager {
    private static final BaseCommand GADGETS = new Command_Gadgets();
    private static final BaseCommand SIMPLEGADGETS = new Command_SimpleGadgets();

    public static void registerCommands () {
        GADGETS.registerCommand(Core.get());
        SIMPLEGADGETS.registerCommand(Core.get());
    }
}
