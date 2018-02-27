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
import java.util.List;

@SuppressWarnings("ALL")
public class ItemBuilder {
    private JSONObject JSON;
    private ItemStack is;
    private ItemMeta im;

    public ItemBuilder(Material material, int amount) {
        JSON = new JSONObject();
        JSON.put("material", material.name());
        JSON.put("amount", amount);
        this.is = new ItemStack(material, amount);
        this.im = is.getItemMeta();
    }

    public static ItemBuilder fromJSON (JSONObject json) {
        if (!json.containsKey("material")) throw new NullPointerException("JSONObject seems to be missing a material");
        if (!json.containsKey("amount")) throw new NullPointerException("JSONObject seems to be missing an amount");

        ItemBuilder builder = new ItemBuilder(Material.valueOf(String.valueOf(json.get("material"))), (Integer) json.get("amount"));

        if (json.containsKey("name")) builder.withName(String.valueOf(json.get("name")));
        if (json.containsKey("lore")) {
            List<String> lore = new ArrayList<>();
            lore.addAll(((JSONArray)json.get("lore")));
            builder.withLore(lore);
        }
        if (json.containsKey("data")) builder.withData((Integer) json.get("data"));

        if (json.containsKey("enchants")) {
            JSONArray array = (JSONArray) json.get("enchants");
            for (Object o : array) {
                String[] args = String.valueOf(o).split(" ~~ ");
                Enchantment enchant = Enchantment.getByName(args[0]);
                int level = Integer.parseInt(args[1]);
                builder.withEnchant(enchant, level);
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
        if (JSON.containsKey("lore")) {
            LORE = (JSONArray) JSON.get("lore");
        }
        LORE.addAll(Arrays.asList(lore));
        JSON.put("lore", LORE);
        Arrays.asList(lore).forEach(s -> im.getLore().add(translate(s)));
        return this;
    }
    public ItemBuilder clearLore() {
        if (JSON.containsKey("lore")) JSON.remove("lore");
        im.getLore().clear();
        return this;
    }
    public ItemBuilder removeLore(String lore) {
        if (JSON.containsKey("lore")) {
            JSONArray LORE = (JSONArray) JSON.get("lore");
            for (Object o : LORE) {
                if (String.valueOf(o).startsWith(lore)) {
                    LORE.remove(o);
                    break;
                }
            }

            if (LORE.isEmpty()) {
                JSON.remove("lore");
            }else{
                JSON.put("lore", LORE);
            }
        }
        im.getLore().remove(translate(lore));
        return this;
    }

    public ItemBuilder withData(int data) {
        JSON.put("data", data);
        is.setDurability((short) data);
        return this;
    }

    public ItemBuilder withEnchant(Enchantment enchant, int level) {
        JSONArray ENCHANTS = new JSONArray();
        if (JSON.containsKey("enchants")) {
            ENCHANTS = (JSONArray) JSON.get("enchants");
        }
        ENCHANTS.add(enchant.getName()+" ~~ "+level);
        JSON.put("enchants", ENCHANTS);

        is.addEnchantment(enchant, level);
        return this;
    }
    public ItemBuilder removeEnchant(Enchantment enchant) {
        if (JSON.containsKey("enchants")) {
            JSONArray ENCHANTS = (JSONArray) JSON.get("enchants");
            for (Object o : ENCHANTS) {
                if (String.valueOf(o).startsWith(enchant.getName())) {
                    ENCHANTS.remove(o);
                    break;
                }
            }
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
        if (JSON.containsKey("flags")) {
            FLAGS = (JSONArray) JSON.get("flags");
        }
        FLAGS.add(flag.name());
        JSON.put("flags", FLAGS);
        im.addItemFlags(flag);
        return this;
    }
    public ItemBuilder removeFlag(ItemFlag flag) {
        if (JSON.containsKey("flags")) {
            JSONArray FLAGS = (JSONArray) JSON.get("flags");
            for (Object o : FLAGS) {
                if (String.valueOf(o).equals(flag.name())) {
                    FLAGS.remove(o);
                    break;
                }
            }
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

        for (String msg : message)
            newLore.add(translate(msg));

        return newLore;
    }
}
