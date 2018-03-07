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
import gadgets.brainsynder.items.ItemManager;
import gadgets.brainsynder.items.list.NextPage;
import gadgets.brainsynder.items.list.PreviousPage;
import gadgets.brainsynder.items.list.RemoveGadget;
import gadgets.brainsynder.listeners.GadgetListeners;
import gadgets.brainsynder.listeners.MenuListener;
import gadgets.brainsynder.menus.GadgetSelector;
import gadgets.brainsynder.utilities.BlockUtils;
import gadgets.brainsynder.utilities.EntityUtils;
import gadgets.brainsynder.utilities.Utils;
import gadgets.brainsynder.utilities.VelocityUtils;
import gadgets.brainsynder.utilities.errors.GadgetRegisterException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.utils.ObjectPager;
import simple.brainsynder.utils.SpigotPluginHandler;

public class Core extends JavaPlugin implements GadgetPlugin {
    public ObjectPager<Gadget> pages;
    private static Core instance;
    private static Language language;
    public IStorage<String> slots;

    private GadgetManager manager;
    private Utils utilities;
    private EntityUtils entityUtils;
    private BlockUtils blockUtils;
    private VelocityUtils velocityUtils;
    private ItemManager itemManager;
    private GadgetSelector gadgetSelector;

    public static Language getLanguage() {
        return Core.language;
    }

    @Override
    public IStorage<String> getSlots() {
        return slots;
    }

    @Override
    public ItemManager getItemManager() {
        return itemManager;
    }

    @Override
    public GadgetSelector getSelectionMenu() {
        return gadgetSelector;
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
        if (!spigotPluginHandler.runTamperCheck("brainsynder", "SimpleGadgets", "1.5-SNAPSHOT")) {
            setEnabled(false);
            return;
        }


        instance = this;
        language = new Language(this);
        language.loadDefaults();
        utilities = new Utils(this);
        velocityUtils = new VelocityUtils(this);
        entityUtils = new EntityUtils(this);
        blockUtils = new BlockUtils();
        manager = new GadgetManager(this);
        slots = new StorageList<>(getLanguage().getStringList("Slots-For-Gadgets"));
        if (pages == null) {
            pages = new ObjectPager<>(slots.getSize());
        }

        itemManager = new ItemManager();
        itemManager.register(new NextPage(this));
        itemManager.register(new PreviousPage(this));
        itemManager.register(new RemoveGadget(this));

        gadgetSelector = new GadgetSelector(this);

        try {
            registerGadgets();
        } catch (GadgetRegisterException e) {
            e.printStackTrace();
        }
        CommandManager.registerCommands(this);
        getServer().getPluginManager().registerEvents(new GadgetListeners(this), this);
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);
    }

    public void onDisable() {
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
        manager.registerGadget(new FallScare(this));
        manager.registerGadget(new FireBender(this));
        manager.registerGadget(new Firework(this));
        manager.registerGadget(new FireworkCannon(this));
        manager.registerGadget(new FreezeBomb(this));
        manager.registerGadget(new FunCannon(this));
        manager.registerGadget(new GravitySurge(this));
        manager.registerGadget(new Laser(this));
        manager.registerGadget(new MelonBlaster(this));
        manager.registerGadget(new NatureWind(this));
        manager.registerGadget(new NetherBlaze(this));
        manager.registerGadget(new NinjaVanish(this));
        manager.registerGadget(new PaintSprayer(this));
        manager.registerGadget(new PaintTrail(this));
        manager.registerGadget(new PancakeCannon(this));
        manager.registerGadget(new PoopBomb(this));
        manager.registerGadget(new QuakeGun(this));
        manager.registerGadget(new Rocket(this));
        manager.registerGadget(new SheepBomb(this));
        manager.registerGadget(new TrailBlazer(this));
        manager.registerGadget(new WaterBomb(this));
        manager.registerGadget(new Pee(this));
        manager.registerGadget(new ChickenBlaster(this));
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

    @Override
    public VelocityUtils getVelocityUtils() {
        return velocityUtils;
    }
}
