package gadgets.brainsynder.utilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressWarnings("ALL")
public class ItemBuilder {
    private JSONObject JSON;
    private ItemStack is;
    private ItemMeta im;

    public ItemBuilder(Material material) {
        this (material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        JSON = new JSONObject(new LinkedHashMap());
        JSON.put("material", material.name());
        JSON.put("amount", amount);
        this.is = new ItemStack(material, amount);
        this.im = is.getItemMeta();
    }

    public static ItemBuilder fromJSON (JSONObject json) {
        if (!json.containsKey("material")) throw new NullPointerException("JSONObject seems to be missing a material");

        int amount = 1;
        if (json.containsKey("amount")) amount = Integer.parseInt(String.valueOf(json.get("amount")));

        ItemBuilder builder = new ItemBuilder(Material.valueOf(String.valueOf(json.get("material"))), amount);

        if (json.containsKey("name")) builder.withName(String.valueOf(json.get("name")));
        if (json.containsKey("lore")) {
            List<String> lore = new ArrayList<>();
            lore.addAll(((JSONArray)json.get("lore")));
            builder.withLore(lore);
        }
        if (json.containsKey("data")) builder.withData(Integer.parseInt(String.valueOf(json.get("data"))));

        if (json.containsKey("enchants")) {
            JSONArray array = (JSONArray) json.get("enchants");
            for (Object o : array) {
                try {
                    String[] args = String.valueOf(o).split(" ~~ ");
                    Enchantment enchant = Enchantment.getByName(args[0]);
                    int level = Integer.parseInt(args[1]);
                    builder.withEnchant(enchant, level);
                }catch (Exception ignored) {}
            }
        }
        if (json.containsKey("flags")) {
            JSONArray array = (JSONArray) json.get("flags");
            for (Object o : array) {
                ItemFlag flag = ItemFlag.valueOf(String.valueOf(o));
                builder.withFlag(flag);
            }
        }

        return builder;
    }

    public ItemBuilder withName(String name) {
        JSON.put("name", name);
        im.setDisplayName(translate(name));
        return this;
    }

    public ItemBuilder withLore(List<String> lore) {
        JSONArray LORE = new JSONArray();
        LORE.addAll(lore);
        JSON.put("lore", LORE);

        im.setLore(translate(lore));
        return this;
    }
    public ItemBuilder addLore(String... lore) {
        JSONArray LORE = new JSONArray();
        if (JSON.containsKey("lore")) LORE = (JSONArray) JSON.get("lore");
        List<String> itemLore = new ArrayList<>();
        if (im.hasLore()) itemLore = im.getLore();

        LORE.addAll(Arrays.asList(lore));
        JSON.put("lore", LORE);
        List<String> finalItemLore = itemLore;
        Arrays.asList(lore).forEach(s -> finalItemLore.add(translate(s)));
        im.setLore(finalItemLore);
        return this;
    }
    public ItemBuilder clearLore() {
        if (JSON.containsKey("lore")) JSON.remove("lore");
        im.getLore().clear();
        return this;
    }
    public ItemBuilder removeLore(String lore) {
        List<String> itemLore = new ArrayList<>();
        if (im.hasLore()) itemLore = im.getLore();
        if (JSON.containsKey("lore")) {
            JSONArray LORE = (JSONArray) JSON.get("lore");
            LORE.stream().filter(o -> String.valueOf(o).startsWith(lore)).forEach(o -> LORE.remove(o));
            if (LORE.isEmpty()) {
                JSON.remove("lore");
            }else{
                JSON.put("lore", LORE);
            }
        }
        itemLore.remove(translate(lore));
        im.setLore(itemLore);
        return this;
    }

    @Deprecated
    public ItemBuilder withData(int data) {
        JSON.put("data", data);
        is.setDurability((short) data);
        return this;
    }

    public ItemBuilder withEnchant(Enchantment enchant, int level) {
        JSONArray ENCHANTS = new JSONArray();
        if (JSON.containsKey("enchants")) ENCHANTS = (JSONArray) JSON.get("enchants");
        ENCHANTS.add(enchant.getName()+" ~~ "+level);
        JSON.put("enchants", ENCHANTS);
        is.addEnchantment(enchant, level);
        return this;
    }
    public ItemBuilder removeEnchant(Enchantment enchant) {
        if (JSON.containsKey("enchants")) {
            JSONArray ENCHANTS = (JSONArray) JSON.get("enchants");
            ENCHANTS.stream().filter(o -> String.valueOf(o).startsWith(enchant.getName())).forEach(o -> ENCHANTS.remove(o));
            if (ENCHANTS.isEmpty()) {
                JSON.remove("enchants");
            }else{
                JSON.put("enchants", ENCHANTS);
            }
        }

        is.removeEnchantment(enchant);
        return this;
    }

    public ItemBuilder withFlag(ItemFlag flag) {
        JSONArray FLAGS = new JSONArray();
        if (JSON.containsKey("flags")) FLAGS = (JSONArray) JSON.get("flags");
        FLAGS.add(flag.name());
        JSON.put("flags", FLAGS);
        im.addItemFlags(flag);
        return this;
    }
    public ItemBuilder removeFlag(ItemFlag flag) {
        if (JSON.containsKey("flags")) {
            JSONArray FLAGS = (JSONArray) JSON.get("flags");
            FLAGS.stream().filter(o -> String.valueOf(o).equals(flag.name())).forEach(o -> FLAGS.remove(o));

            if (FLAGS.isEmpty()) {
                JSON.remove("flags");
            }else{
                JSON.put("flags", FLAGS);
            }
        }

        im.removeItemFlags(flag);
        return this;
    }

    public JSONObject toJSON () {
        return JSON;
    }

    public ItemStack build() {
        is.setItemMeta(im);
        return is;
    }

    private String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    private List<String> translate(List<String> message) {
        ArrayList<String> newLore = new ArrayList<>();
        message.forEach(msg -> newLore.add(translate(msg)));
        return newLore;
    }
}
