package gadgets.brainsynder.items;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.files.JSONFile;
import gadgets.brainsynder.utilities.ItemBuilder;

import java.io.File;

public abstract class CustomItem extends JSONFile {
    private ItemBuilder _BUILDER_;
    private int _SLOT_;
    private String name;
    private GadgetPlugin plugin;

    public CustomItem(GadgetPlugin plugin, String name) {
        super(new File(new File(plugin.getPlugin().getDataFolder().toString()+File.separator+"Items"), name+".json"));
        this.name = name;
        this.plugin = plugin;
    }

    @Deprecated
    public CustomItem(File file) {
        super(file);
        name = file.getName().replace(".json", "");
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
        return _SLOT_-1;
    }

    public GadgetPlugin getPlugin() {
        return plugin;
    }

    public ItemBuilder getItemBuilder() {
        return _BUILDER_;
    }

    public String getName() {
        return name;
    }

    public void onClick (User user){
        onClick(user, 1);
    }
    public void onClick (User user, int page){
        onClick(user);
    }

    public boolean addToInventory (int page) {
        return true;
    }

    @Deprecated
    public abstract int getDefaultSlot();

    @Deprecated
    public abstract ItemBuilder getDefaultItem ();
}
