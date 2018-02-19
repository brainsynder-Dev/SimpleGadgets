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

import gadgets.brainsynder.Commands.CommandManager;
import gadgets.brainsynder.Files.Language;
import gadgets.brainsynder.Gadgets.Errors.GadgetRegisterException;
import gadgets.brainsynder.Gadgets.Gadget;
import gadgets.brainsynder.Listeners.GadgetsListener;
import gadgets.brainsynder.loaders.BackLoader;
import gadgets.brainsynder.loaders.NextLoader;
import gadgets.brainsynder.loaders.RemoveLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.utils.PageMaker;
import simple.brainsynder.utils.SpigotPluginHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Core extends JavaPlugin implements GadgetPlugin {
    public PageMaker pages;
    private static Core instance;
    private static Language language;
    public static IStorage<String> slots;
    private RemoveLoader removeGadget;
    private BackLoader back;
    private NextLoader next;
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
        slots = new StorageList<>(getLanguage().getStringList("Slots-For-Gadgets"));
        if (pages == null) {
            pages = new PageMaker(new ArrayList<String>(), slots.getSize());
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
        CommandManager.registerCommands();
        new Gadget.Listeners();
        getServer().getPluginManager().registerEvents(new GadgetsListener(), this);
    }

    public void onDisable() {
        removeGadget.save();
        next.save();
        back.save();
        if (Bukkit.getOnlinePlayers().size() != 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Gadget.Variables.removeGadget(player);
            }
        }
    }

    @Override
    public PageMaker getGadgetPageMaker() {
        return pages;
    }

    public void registerGadgets() throws GadgetRegisterException {
        Gadget.registerGadget(Gadget.FUN_CANNON);
        Gadget.registerGadget(Gadget.BAT_BLASTER);
        Gadget.registerGadget(Gadget.TRAIL_BLAZER);
        Gadget.registerGadget(Gadget.CONFETTI);
        Gadget.registerGadget(Gadget.EXPLOSIVE_SNOWBALL);
        Gadget.registerGadget(Gadget.WATER_BOMB);
        Gadget.registerGadget(Gadget.FIREWORKS);
        Gadget.registerGadget(Gadget.MELON_BLASTER);
        Gadget.registerGadget(Gadget.QUAKE_GUN);
        Gadget.registerGadget(Gadget.WINTER_BREEZE);
        Gadget.registerGadget(Gadget.NETHER_BLAZE);
        Gadget.registerGadget(Gadget.PAINT_SPRAYER);
        Gadget.registerGadget(Gadget.PAINT_TRAIL);
        Gadget.registerGadget(Gadget.FIRE_BENDER);
        Gadget.registerGadget(Gadget.NATURE_WIND);
        Gadget.registerGadget(Gadget.SHEEP_BOMB);
        Gadget.registerGadget(Gadget.FALL_SCARE);
        Gadget.registerGadget(Gadget.ROCKET);
        Gadget.registerGadget(Gadget.PARTICLE_LAZER);
        Gadget.registerGadget(Gadget.POOP_BOMB);
        Gadget.registerGadget(Gadget.NINJA_VANISH);
        Gadget.registerGadget(Gadget.BIRTHDAY_CANNON);
        Gadget.registerGadget(Gadget.BBQ_CANNON);
        Gadget.registerGadget(Gadget.FREEZE_BOMB);
        Gadget.registerGadget(Gadget.STAR_BLAZING);
        Gadget.registerGadget(Gadget.BANANA_CANNON);
        Gadget.registerGadget(Gadget.GRAVITY_SURGE);
        Gadget.registerGadget(Gadget.PANCAKE_CANNON);
        Gadget.registerGadget(Gadget.FIREWORK_CANNON);
    }

    public static Core get() {
        return instance;
    }

    @Override
    public Plugin getGadgetPlugin() {
        return this;
    }

    @Override
    public List<Gadget> getGadgets() {
        return Gadget.values();
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
