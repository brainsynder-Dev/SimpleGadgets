package gadgets.brainsynder.Events;

import gadgets.brainsynder.Gadgets.Gadget;

public class GadgetEvent extends SimpleGadgetsEvent {
    private Gadget gadget;

    public GadgetEvent (Gadget gadget) {
        this.gadget = gadget;
    }

    public Gadget getGadget () {
        return gadget;
    }
}
