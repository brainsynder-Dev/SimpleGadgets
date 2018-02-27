package gadgets.brainsynder.api.event.gadget;

import gadgets.brainsynder.api.event.GadgetEvent;
import gadgets.brainsynder.api.gadget.Gadget;
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
