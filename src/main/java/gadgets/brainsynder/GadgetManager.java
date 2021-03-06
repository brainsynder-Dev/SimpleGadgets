package gadgets.brainsynder;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.event.GadgetListener;
import gadgets.brainsynder.api.event.gadget.GadgetRegisterEvent;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.errors.GadgetRegisterException;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GadgetManager {
    private Core core;
    private Map<String, Gadget> byName = new HashMap<>();
    private static Map<String, User> userMap = new HashMap<>();

    GadgetManager (Core core) {
        this.core = core;
    }

    public void registerGadget(Gadget gadget) throws GadgetRegisterException {
        if (byName.containsKey(gadget.getIdName())) {
            throw new GadgetRegisterException("The Gadget (" + gadget.getIdName() + ") is already registered.");
        }
        GadgetRegisterEvent event = new GadgetRegisterEvent(gadget);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        String gadgetName = WordUtils.capitalizeFully(gadget.getIdName().replace("_", " "));
        gadget.loadExtraTags();
        gadget.setDefault ("enabled", "true");
        gadget.setDefault ("name", gadgetName);
        gadget.setDefault ("cooldown", 10);
        gadget.setDefault ("item", gadget.getDefaultItem().toJSON());
        gadget.setDefault ("permission", "SimpleGadgets.gadget." + gadgetName.replace(" ", ""));
        gadget.save();
        gadget.load();
        if (gadget.isEnabled()) {
            core.getPages().add(gadget);
        }
        byName.put(gadget.getIdName(), gadget);
        if (gadget instanceof GadgetListener) {
            GadgetListener listener = (GadgetListener) gadget;
            Bukkit.getServer().getPluginManager().registerEvents(listener, core);
        }
        //System.out.println("SimpleGadgets >> The gadget " + WordUtils.capitalizeFully(gadget.getIdName().replace("_", " ")) + " was successfully registered with the id " + gadget.getId());
    }

    public Gadget getByIdName(String idName, boolean newInstance) {
        for (Gadget gadget : byName.values()) {
            if (gadget.getIdName().equals(idName)) {
                if (newInstance) {
                    try {
                        return gadget.getClass().getConstructor(GadgetPlugin.class).newInstance(core);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
                return gadget;
            }
        }
        return null;
    }

    public Gadget getByItem(ItemStack item, boolean newInstance) {
        for (Gadget gadget : byName.values()) {
            if (gadget.getItem().isSimilar(item)) {
                if (newInstance) {
                    try {
                        return gadget.getClass().getConstructor(GadgetPlugin.class).newInstance(core);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
                return gadget;
            }
        }
        return null;
    }

    public User getUser (Player player) {
        if (userMap.containsKey(player.getUniqueId().toString()))
            return userMap.get(player.getUniqueId().toString());
        User user = new User(player);
        userMap.put(player.getUniqueId().toString(), user);
        return user;
    }

    public Collection<Gadget> getGadgetList() {
        return byName.values();
    }

}
