package gadgets.brainsynder.utilities;

import gadgets.brainsynder.api.GadgetPlugin;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class EntityUtils {
    public static boolean spawnMe = false;
    private GadgetPlugin plugin;

    public EntityUtils(GadgetPlugin plugin) {
        this.plugin = plugin;
    }

    public <T extends Entity> T spawnMob(Location location, Class<? extends Entity> entityClass) {
        spawnMe = true;
        Entity ent = location.getWorld().spawn(location, entityClass);
        spawnMe = false;
        ent.setMetadata("Spawnable", new FixedMetadataValue(plugin.getPlugin(), "Spawnable"));
        return (T) ent;
    }

    public <T extends Entity> T spawnMob(Location location, EntityType type) {
        spawnMe = true;
        Entity ent = location.getWorld().spawnEntity(location, type);
        spawnMe = false;
        ent.setMetadata("Spawnable", new FixedMetadataValue(plugin.getPlugin(), "Spawnable"));
        return (T) ent;
    }


    // This section deals with the Projectile utilities
    public <T extends Projectile>T launchProjectile(LivingEntity entity, Class<? extends T> proj) {
        return launchProjectile(entity, proj, 2);
    }

    public <T extends Projectile>T launchProjectile(LivingEntity entity, Class<? extends T> proj, double mod) {
        return launchProjectile(entity, proj, (float)mod);
    }

    public <T extends Projectile>T launchProjectile(LivingEntity entity, Class<? extends T> proj, int mod) {
        return launchProjectile(entity, proj, (float)mod);
    }

    public <T extends Projectile>T launchProjectile(LivingEntity entity, Class<? extends T> proj, float mod) {
        Projectile p = entity.launchProjectile(proj);
        p.setShooter(entity);
        p.setMetadata("GadgetProj", new FixedMetadataValue(plugin.getPlugin(), "GadgetProj"));
        p.setVelocity(p.getVelocity().multiply(mod));
        return (T)p;
    }


    // This section deals with the Item utilities
    public void spawnRemovableItem(Location location, ItemStack itemstack, int time){
        final Item item = location.getWorld().dropItem(location, itemstack);
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setMetadata("takeable", new FixedMetadataValue(plugin.getPlugin(), "takeable"));
        new BukkitRunnable() {
            @Override
            public void run () {
                item.remove();
            }
        }.runTaskLater(plugin.getPlugin(), time);
    }

    public void spawnRemovableItem(Location location, ItemStack itemstack, Vector vector, int time){
        final Item item = location.getWorld().dropItem(location, itemstack);
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setVelocity(vector);
        item.setMetadata("takeable", new FixedMetadataValue(plugin.getPlugin(), "takeable"));
        new BukkitRunnable() {
            @Override
            public void run () {
                item.remove();
            }
        }.runTaskLater(plugin.getPlugin(), time);
    }

    public List<Item> spawnItems (Location location, ItemStack itemstack, Vector vector, int amount){
        List<Item>items = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            final Item item = location.getWorld().dropItem(location, itemstack);
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setVelocity(vector);
            item.setMetadata("takeable", new FixedMetadataValue(plugin.getPlugin(), "takeable"));
            items.add(item);
        }
        return items;
    }

    public List<Item> spawnItems (Location location, ItemStack itemstack, int amount){
        List<Item>items = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            double x = -0.5F + (float)(Math.random() * 0.9D);
            double y = 0.5D;
            double z = -0.5F + (float)(Math.random() * 0.9D);
            final Item item = location.getWorld().dropItem(location, itemstack);
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setVelocity(new Vector (x, y, z));
            item.setMetadata("takeable", new FixedMetadataValue(plugin.getPlugin(), "takeable"));
            items.add(item);
        }
        return items;
    }

    public Item launchItem (LivingEntity entity, ItemStack itemStack) {
        Item it = entity.getWorld().dropItem(entity.getEyeLocation(), itemStack);
        it.setVelocity(entity.getEyeLocation().getDirection());
        it.setMetadata("takeable", new FixedMetadataValue(plugin.getPlugin(), "takeable"));
        return it;
    }


    /**
     * Handles all the checks to see if an Entity is valid...
     *
     * @return
     *      false | Entity is not valid
     *      true  | Entity is valid
     */
    public boolean isValid (Entity entity) {
        List<Boolean> values = new ArrayList<>();
        values.add((entity != null));

        if (!values.contains(false)) {
            values.add(entity.isValid());
            values.add(!entity.isDead());
            if (entity instanceof Player) {
                Player player = (Player) entity;
                values.add(player.isOnline());
            }
        }

        return !values.contains(false);
    }
}