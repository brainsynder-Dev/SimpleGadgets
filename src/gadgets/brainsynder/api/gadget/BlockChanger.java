package gadgets.brainsynder.api.gadget;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.utilities.BlockStorage;

/**
 * This class is mostly for the Gadgets that change blocks
 */
public abstract class BlockChanger extends Gadget {
    protected BlockStorage storage = new BlockStorage ();
    public BlockChanger(GadgetPlugin plugin, String idName) {
        super(plugin, idName);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (!storage.isEmpty()) storage.reset();
    }
}
