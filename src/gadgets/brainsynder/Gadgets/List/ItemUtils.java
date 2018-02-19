package gadgets.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    public static void spawnRemoveableItem (Location location, ItemStack itemstack, int time){
        final Item item = location.getWorld().dropItem(location, itemstack);
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setMetadata("takeable", new FixedMetadataValue(Core.get(), "takeable"));
        new BukkitRunnable() {
            @Override
            public void run () {
                item.remove();
            }
        }.runTaskLater(Core.get(), time);
    }

    public static void spawnRemoveableItem (Location location, ItemStack itemstack, Vector vector, int time){
        final Item item = location.getWorld().dropItem(location, itemstack);
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setVelocity(vector);
        item.setMetadata("takeable", new FixedMetadataValue(Core.get(), "takeable"));
        new BukkitRunnable() {
            @Override
            public void run () {
                item.remove();
            }
        }.runTaskLater(Core.get(), time);
    }

    public static List<Item> spawnItems (Location location, ItemStack itemstack, Vector vector, int amount){
        List<Item>items = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            final Item item = location.getWorld().dropItem(location, itemstack);
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setVelocity(vector);
            item.setMetadata("takeable", new FixedMetadataValue(Core.get(), "takeable"));
            items.add(item);
        }
        return items;
    }

    public static List<Item> spawnItems (Location location, ItemStack itemstack, int amount){
        List<Item>items = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            double x = -0.5F + (float)(Math.random() * 0.9D);
            double y = 0.5D;
            double z = -0.5F + (float)(Math.random() * 0.9D);
            final Item item = location.getWorld().dropItem(location, itemstack);
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setVelocity(new Vector (x, y, z));
            item.setMetadata("takeable", new FixedMetadataValue(Core.get(), "takeable"));
            items.add(item);
        }
        return items;
    }

    public static Item launchItem (LivingEntity entity, ItemStack itemStack) {
        Item it = entity.getWorld().dropItem(entity.getEyeLocation(), itemStack);
        it.setVelocity(entity.getEyeLocation().getDirection());
        it.setMetadata("takeable", new FixedMetadataValue(Core.get(), "takeable"));
        return it;
    }
}
