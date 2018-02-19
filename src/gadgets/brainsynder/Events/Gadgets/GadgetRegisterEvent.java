package gadgets.brainsynder.Events.Gadgets;

import gadgets.brainsynder.Events.GadgetEvent;
import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.event.Cancellable;

public class GadgetRegisterEvent extends GadgetEvent implements Cancellable {
    private boolean canceled = false;

    public GadgetRegisterEvent(Gadget gadget) {
        super(gadget);
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
