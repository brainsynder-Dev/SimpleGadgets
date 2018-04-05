package gadgets.brainsynder.commands;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.commands.list.Command_Gadgets;
import gadgets.brainsynder.commands.list.Command_SimpleGadgets;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager {
    public static void registerCommands (GadgetPlugin plugin) {
        new Command_Gadgets(plugin).registerCommand((JavaPlugin) plugin.getPlugin());
        new Command_SimpleGadgets(plugin).registerCommand((JavaPlugin) plugin.getPlugin());
    }
}
