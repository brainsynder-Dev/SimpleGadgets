package gadgets.brainsynder.api.user;

import gadgets.brainsynder.api.event.gadget.GadgetSelectEvent;
import gadgets.brainsynder.api.gadget.Gadget;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class User {
    private Gadget gadget = null;
    private Player player;

    public User (Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Gadget getGadget() {
        return gadget;
    }

    public boolean hasGadget () {
        return (gadget != null);
    }

    public void setGadget(Gadget gadget) {
        GadgetSelectEvent event = new GadgetSelectEvent(gadget, player);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) return;

        if (hasGadget()) removeGadget();

        this.gadget = gadget;
        player.getInventory().addItem(gadget.getItemStack());
        player.updateInventory();
    }

    public void removeGadget () {
        if (!hasGadget()) return;
        if (player.getInventory().contains(gadget.getItemStack())) {
            player.getInventory().remove(gadget.getItemStack());
            player.updateInventory();
        }
        gadget = null;
    }
}
