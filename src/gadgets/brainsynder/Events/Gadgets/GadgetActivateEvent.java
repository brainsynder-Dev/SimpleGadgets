package gadgets.brainsynder.Events.Gadgets;

import gadgets.brainsynder.Events.GadgetEvent;
import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class GadgetActivateEvent extends GadgetEvent implements Cancellable {
    private Player player;
    private boolean canceled = false;

    public GadgetActivateEvent(Gadget gadget, Player player) {
        super(gadget);
        this.player = player;
    }

    public Player getPlayer () {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.canceled = b;
    }
}
