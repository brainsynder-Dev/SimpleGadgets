package gadgets.brainsynder.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.api.SkullMaker;
import simple.brainsynder.files.JSONFile;

import java.io.File;

public class NextLoader extends JSONFile {
    public NextLoader(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put("Slot-Number", "54");
        defaults.put("MaterialName", "ARROW");
        defaults.put("MaterialData", "0");
        defaults.put("MaterialAmount", "1");
        defaults.put("DisplayName", "&6&l&m----&6&l>");
        JSONArray lore = new JSONArray();
        lore.add("&7Click Here to go");
        lore.add("&7the next page");
        defaults.put("DisplayLoreEnabled", "true");
        defaults.put("DisplayLore", lore);
        defaults.put("FakeEnchanted", "false");
        JSONObject custom = new JSONObject();
        custom.put("Enabled", "true");
        custom.put("SkullOwner", "SimpleAPI");
        custom.put("TextureURL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJmOTEwYzQ3ZGEwNDJlNGFhMjhhZjZjYzgxY2Y0OGFjNmNhZjM3ZGFiMzVmODhkYjk5M2FjY2I5ZGZlNTE2In19fQ==");
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
