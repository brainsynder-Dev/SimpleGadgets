/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package gadgets.brainsynder;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.gadget.list.*;
import gadgets.brainsynder.commands.CommandManager;
import gadgets.brainsynder.files.Language;
import gadgets.brainsynder.items.BackLoader;
import gadgets.brainsynder.items.NextLoader;
import gadgets.brainsynder.items.RemoveLoader;
import gadgets.brainsynder.listeners.GadgetListeners;
import gadgets.brainsynder.listeners.MenuListener;
import gadgets.brainsynder.utilities.BlockUtils;
import gadgets.brainsynder.utilities.EntityUtils;
import gadgets.brainsynder.utilities.Utils;
import gadgets.brainsynder.utilities.errors.GadgetRegisterException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.utils.ObjectPager;
import simple.brainsynder.utils.SpigotPluginHandler;

import java.io.File;

public class Core extends JavaPlugin implements GadgetPlugin {
    public ObjectPager<Gadget> pages;
    private static Core instance;
    private static Language language;
    public static IStorage<String> slots;
    private RemoveLoader removeGadget;
    private BackLoader back;
    private NextLoader next;
    private GadgetManager manager;
    private Utils utilities;
    private EntityUtils entityUtils;
    private BlockUtils blockUtils;
    private File itemsFile = new File(getDataFolder().toString() + "/Items/");

    public static Language getLanguage() {
        return Core.language;
    }

    public static IStorage<String> getSlots() {
        return Core.slots;
    }

    public void onEnable() {
        saveResource("Permissions.yml", true);
        Plugin plugin = getServer().getPluginManager().getPlugin("SimpleAPI");
        if (plugin == null) {
            System.out.println("SimpleGadgets >> Missing dependency (SimpleAPI) Must have the plugin in order to work the plugin");
            new MissingAPI(this).runTaskTimer(this, 0, 20 * 60 * 2);
            return;
        }
        double ver = Double.parseDouble(plugin.getDescription().getVersion());
        if (ver < 3.1) {
            System.out.println("SimpleGadgets >> Notice: Your Version of SimpleAPI is not the required version, Please update SimpleAPI https://www.spigotmc.org/resources/24671/");
        }
        SpigotPluginHandler spigotPluginHandler = new SpigotPluginHandler(this, 22728, SpigotPluginHandler.MetricType.BSTATS);
        SpigotPluginHandler.registerPlugin(spigotPluginHandler);
        if (!spigotPluginHandler.runTamperCheck("brainsynder", "SimpleGadgets", "1.5")) {
            setEnabled(false);
            return;
        }


        instance = this;
        language = new Language(this);
        language.loadDefaults();
        utilities = new Utils(this);
        entityUtils = new EntityUtils(this);
        blockUtils = new BlockUtils();
        manager = new GadgetManager(this);
        slots = new StorageList<>(getLanguage().getStringList("Slots-For-Gadgets"));
        if (pages == null) {
            pages = new ObjectPager<>(slots.getSize());
        }
        removeGadget = new RemoveLoader(new File(itemsFile, "RemoveGadgetItem.json"));
        next = new NextLoader(new File(itemsFile, "NextPageItem.json"));
        back = new BackLoader(new File(itemsFile, "PreviousPageItem.json"));
        removeGadget.save();
        next.save();
        back.save();
        try {
            registerGadgets();
        } catch (GadgetRegisterException e) {
            e.printStackTrace();
        }
        CommandManager.registerCommands(this);
        new GadgetListeners(this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    public void onDisable() {
        removeGadget.save();
        next.save();
        back.save();
        if (Bukkit.getOnlinePlayers().size() != 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                manager.getUser(player).removeGadget();
            }
        }
    }

    @Override
    public ObjectPager<Gadget> getPages() {
        return pages;
    }

    public void registerGadgets() throws GadgetRegisterException {
        manager.registerGadget(new BananaCannon(this));
        manager.registerGadget(new BatBlaster(this));
        manager.registerGadget(new BBQCannon(this));
        manager.registerGadget(new BirthdayCannon(this));
        manager.registerGadget(new ConfettiCannon(this));
        manager.registerGadget(new ExplosiveSnowball(this));
    }

    public static Core get() {
        return instance;
    }

    @Override
    public GadgetManager getManager() {
        return manager;
    }

    @Override
    public Utils getUtilities() {
        return utilities;
    }

    @Override
    public Plugin getPlugin() {
        return this;
    }

    @Override
    public EntityUtils getEntityUtils() {
        return entityUtils;
    }

    @Override
    public BlockUtils getBlockUtils() {
        return blockUtils;
    }

    public RemoveLoader getRemoveGadget() {
        return this.removeGadget;
    }

    public BackLoader getBack() {
        return this.back;
    }

    public NextLoader getNext() {
        return this.next;
    }
}
