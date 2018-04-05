/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package gadgets.brainsynder.api.gadget;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.files.JSONFile;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class Gadget extends JSONFile {
    protected List<Item> removableItems = new ArrayList<>();
    protected List<Entity> removableEntities = new ArrayList<>();
    private String idName;
    private GadgetPlugin plugin;
    private String _PERMISSION_, _NAME_;
    private boolean _ENABLED_, removed = false;
    private int _COOLDOWN_;
    private ItemBuilder _BUILDER_;

    public void load () {
        _COOLDOWN_ = getInteger("cooldown");
        _ENABLED_ = Boolean.valueOf(getString("enabled", false));
        _PERMISSION_ = getString("permission", false);
        _NAME_ = getString("name", false);
        _BUILDER_ = ItemBuilder.fromJSON(getObject("item"));
    }


    public Gadget(GadgetPlugin plugin, String idName) {
        super(new File(new File(plugin.getPlugin().getDataFolder().toString()+File.separator+"Gadgets"), idName+".json"));
        this.plugin = plugin;
        this.idName = idName.toLowerCase().replace(" ", "_");
    }

    public GadgetPlugin getPlugin () {
        return plugin;
    }

    public void loadExtraTags() {}

    /**
     * Does the player have permission to use this gadget?
     */
    public boolean hasPermission(Player player) {
        if (!Core.getLanguage().getBoolean("Needs-Permission"))
            return true;

        return (player.hasPermission(getPermission())) || (player.hasPermission("SimpleGadgets.gadget.*"));
    }
    public String getPermission() {
        return _PERMISSION_;
    }

    public String getName() {
        return _NAME_;
    }

    public boolean isEnabled() {
        return _ENABLED_;
    }

    public boolean isRemoved() {
        return removed;
    }

    public int getCooldown() {
        return _COOLDOWN_;
    }

    /**
     * This method is fired when the player removes their gadget
     *
     * Used to clean up... aka kill mobs/lists/ETC...
     */
    public void onRemove () {
        removed = true;
        clearEntities();
        clearItems();
    }

    /**
     * This method is run when the player RightClicks their gadget item
     *
     * @param user
     *      The players' User instance
     */
    public void run(User user) {}

    /**
     * This method is run when the player Right Clicks a block with their gadget item
     *
     * @param user
     *      The players' User instance
     * @param block
     *      The block the player clicked (Right Clicked)
     */
    public void onBlockClick(User user, Block block) {
        run(user);
    }

    /**
     * This method is run when the player moves when their gadget is activated
     *
     * @param user
     *      The players' User instance
     */
    public void onUserMove(User user) {}

    /**
     * This method is called when the projectile their gadget shot lands
     *
     * @param user
     *      The players' User instance
     * @param projectile
     *      The Projectile the player shot
     */
    public void onProjectileHit(User user, Projectile projectile) {}
    public void onProjectileHit(User user, Projectile projectile, Location location) {}

    /**
     * This is the ItemBuilder method that can be used to checking the items
     */
    public ItemBuilder getItem() {
        return _BUILDER_;
    }

    /**
     * This is for loading the default item into the Gadget File...
     *
     * DO NOT USE
     */
    @Deprecated
    public abstract ItemBuilder getDefaultItem();

    public boolean hasGadgetInfo () {
        return getClass().isAnnotationPresent(GadgetInfo.class);
    }

    public GadgetInfo getGadgetInfo () {
        if (hasGadgetInfo()) return getClass().getAnnotation(GadgetInfo.class);
        return null;
    }

    /**
     *
     * @return idName is the Identifier name for the gadget (All lowercase and no spaces)
     */
    public String getIdName() {
        return this.idName;
    }

    protected void clearItems () {
        if (removableItems.isEmpty()) return;
        removableItems.stream()
                .filter(item -> getPlugin().getEntityUtils().isValid(item))
                .forEach(Item::remove);
        removableItems.clear();
    }

    protected void clearEntities () {
        if (removableEntities.isEmpty()) return;
        removableEntities.stream()
                .filter(item -> getPlugin().getEntityUtils().isValid(item))
                .forEach(Entity::remove);
        removableEntities.clear();
    }
}
