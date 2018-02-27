package gadgets.brainsynder.api.event;

import gadgets.brainsynder.api.gadget.Gadget;

public class GadgetEvent extends SimpleGadgetsEvent {
    private Gadget gadget;

    public GadgetEvent (Gadget gadget) {
        this.gadget = gadget;
    }

    public Gadget getGadget () {
        return gadget;
    }
}
