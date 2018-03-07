package gadgets.brainsynder.items;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.files.JSONFile;
import gadgets.brainsynder.utilities.ItemBuilder;

import java.io.File;

public abstract class CustomItem extends JSONFile {
    private ItemBuilder _BUILDER_;
    private int _SLOT_;

    public CustomItem(GadgetPlugin plugin, String name) {
        super(new File(new File(plugin.getPlugin().getDataFolder().toString()+File.separator+"Items"), name+".json"));
    }

    @Deprecated
    public CustomItem(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        super.loadDefaults();
        setDefault("slot", getDefaultSlot());
        setDefault("item", getDefaultItem().toJSON());
    }

    public void load () {
        _BUILDER_ = ItemBuilder.fromJSON(getObject("item"));
        _SLOT_ = getInteger("slot");
    }

    public int getSlot() {
        return _SLOT_;
    }

    public ItemBuilder getItemBuilder() {
        return _BUILDER_;
    }

    public void onClick (User user){
        onClick(user, 1);
    }
    public void onClick (User user, int page){
        onClick(user);
    }

    @Deprecated
    abstract int getDefaultSlot ();

    @Deprecated
    abstract ItemBuilder getDefaultItem ();
}
