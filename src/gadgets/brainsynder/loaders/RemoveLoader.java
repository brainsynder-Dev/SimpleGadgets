package gadgets.brainsynder.loaders;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.api.SkullMaker;
import simple.brainsynder.files.JSONFile;

import java.io.File;

public class RemoveLoader extends JSONFile {
    public RemoveLoader(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put("Slot-Number", "50");
        defaults.put("MaterialName", "BARRIER");
        defaults.put("MaterialData", "0");
        defaults.put("MaterialAmount", "1");
        defaults.put("DisplayName", "&cRemove Gadget");
        JSONArray lore = new JSONArray();
        lore.add("&7Click Here to");
        lore.add("&7Remove your Gadget");
        defaults.put("DisplayLoreEnabled", "true");
        defaults.put("DisplayLore", lore);
        defaults.put("FakeEnchanted", "false");
        JSONObject custom = new JSONObject();
        custom.put("Enabled", "false");
        custom.put("SkullOwner", "SimpleAPI");
        custom.put("TextureURL", "Insert Base64URL Here");
        defaults.put("CustomSkull", custom);
    }

    public int getSlot() {
        int i = getInteger("Slot-Number");
        return (i - 1);
    }

    public ItemStack getItem() {
        if (getBoolean(getObject("CustomSkull"), "Enabled")) {
            SkullMaker maker = new SkullMaker();
            maker.setAmount(getInteger("MaterialAmount"));
            maker.setName(getString("DisplayName", true));
            maker.setSkullOwner(getString(getObject("CustomSkull"), "SkullOwner"));
            maker.setOwner(getString(getObject("CustomSkull"), "TextureURL"));
            if (getBoolean("DisplayLoreEnabled"))
                for (Object s : getArray("DisplayLore")) {
                    maker.addLoreLine(String.valueOf(s));
                }
            return maker.create();
        } else {
            ItemMaker maker = new ItemMaker(Material.valueOf(getString("MaterialName", true)), (byte) getInteger("MaterialData"));
            maker.setAmount(getInteger("MaterialAmount"));
            maker.setName(getString("DisplayName", true));
            if (getBoolean("DisplayLoreEnabled"))
                for (Object s : getArray("DisplayLore")) {
                    maker.addLoreLine(String.valueOf(s));
                }
            if (getBoolean("FakeEnchanted"))
                maker.enchant();
            return maker.create();
        }
    }
}
