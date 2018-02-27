package gadgets.brainsynder.utilities;

import gadgets.brainsynder.api.GadgetPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import simple.brainsynder.math.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Utils {
    private GadgetPlugin plugin;

    public Utils (GadgetPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isSimilar(ItemStack main, ItemStack check) {
        List<Boolean> values = new ArrayList<>();
        if ((main == null) || (check == null)) return false;

        if (main.getType() == check.getType()) {
            if (main.hasItemMeta() && check.hasItemMeta()) {
                ItemMeta mainMeta = main.getItemMeta();
                ItemMeta checkMeta = check.getItemMeta();
                if (mainMeta.hasDisplayName() && checkMeta.hasDisplayName()) {
                    values.add(mainMeta.getDisplayName().equals(checkMeta.getDisplayName()));
                }

                if (mainMeta.hasLore() && checkMeta.hasLore()) {
                    values.add(mainMeta.getLore().equals(checkMeta.getLore()));
                }

                if (mainMeta.hasEnchants() && checkMeta.hasEnchants()) {
                    values.add(mainMeta.getEnchants().equals(checkMeta.getEnchants()));
                }

                //values.add(mainMeta.equals(checkMeta));


                if (!values.isEmpty()) return !values.contains(false);
            }
        }

        return main.isSimilar(check);
    }

    public String itemToString(ItemStack itemStack) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("i", itemStack);
        return config.saveToString();
    }

    public ItemStack stringToItem(String stringBlob) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(stringBlob);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return config.getItemStack("i", null);
    }

    public List<Location> getBlockLine(LivingEntity player, int length) {
        List<Location> list = new ArrayList<>();
        for (int amount = length; amount > 0; amount--)
            list.add(player.getTargetBlock(null, amount).getLocation());
        return list;
    }

    public Location getEyeLocation(Entity player) {
        return player.getLocation().add(0.0D, 1.1D, 0.0D);
    }

    public Location getTargetLocation(LivingEntity player) {
        return player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().multiply(100.0));
    }

    public Location getTargetLocation(LivingEntity player, double length) {
        return player.getLocation().clone().add(player.getEyeLocation().getDirection().multiply(length));
    }

    public List<Location> getStraightLine(LivingEntity user, int length) {
        List<Location> locations = new ArrayList<>();
        Location start = user.getEyeLocation();
        Location end = getTargetLocation(user, length);
        double dist = Math.abs(end.distance(start));
        for (int i = -1; i < length; ++i) {
            double delta = (double) i / dist;
            double x = (1.0D - delta) * start.getX() + delta * (end.getX() + 0.5D);
            double y = (1.0D - delta) * start.getY() + delta * (end.getY() + 0.5D);
            double z = (1.0D - delta) * start.getZ() + delta * (end.getZ() + 0.5D);
            Location l = new Location(start.getWorld(), x, y, z);
            locations.add(l);
        }
        return locations;
    }

    public Vector calculatePath(Player player) {
        Random r = new Random();
        double yaw = Math.toRadians((double) (-player.getLocation().getYaw() - 90.0F));
        double pitch = Math.toRadians((double) (-player.getLocation().getPitch()));
        double x = Math.cos(pitch) * Math.cos(yaw);
        double y = Math.sin(pitch);
        double z = -Math.sin(yaw) * Math.cos(pitch);
        return new Vector(x, y, z);
    }

    public Location getRandomLoc(Location location) {
        Location loc = location.clone();
        loc.add(MathUtils.random(0.1F, 0.8F), 0, MathUtils.random(0.1F, 0.8F));
        return loc;
    }

    public Location getRandomLoc(float min, float max, Location location) {
        Location loc = location.clone();
        loc.add(MathUtils.random(min, max), 0, MathUtils.random(min, max));
        return loc;
    }

    public void noArc(final Entity proj, final org.bukkit.util.Vector direction) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin.getPlugin(), () -> {
            if (proj == null) {
                return;
            }

            if (!proj.isValid()) {
                proj.setVelocity(direction);
                Utils.this.noArc(proj, direction);
            }
        }, 1L);
    }

    public Entity[] getNearbyEntities(Location l, int radius) {
        int chunk = radius < 16 ? 1 : (radius - radius % 16) / 16;
        ArrayList<Entity> radiusEntities = new ArrayList<>();

        for (int ent = 0 - chunk; ent <= chunk; ++ent) {
            for (int chZ = 0 - chunk; chZ <= chunk; ++chZ) {
                int x = (int) l.getX();
                int y = (int) l.getY();
                int z = (int) l.getZ();
                Location loc = new Location(l.getWorld(), (double) (x + ent * 16), (double) y, (double) (z + chZ * 16));

                for (Entity e : loc.getChunk().getEntities()) {
                    if (Objects.equals(e.getLocation().getWorld().getName(), l.getWorld().getName()) && e.getLocation().distance(l) <= (double) radius && e.getLocation().getBlock() != l.getBlock()) {
                        radiusEntities.add(e);
                    }
                }
            }
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

    public Vector getVector(Entity entity, Location from) {
        Vector s = entity.getLocation().toVector().subtract(from.toVector()).normalize();
        s.multiply(2);
        return s;
    }

    public void pushEntity(Entity entity, Location place) {
        Vector v = entity.getLocation().toVector().subtract(place.toVector()).multiply(0.5D).add(new Vector(0.0D, 0.5D, 0.0D));
        v.setY(0);
        v.add(new Vector(0, 1, 0));
        MathUtils.applyVelocity(entity, v.add(MathUtils.getRandomCircleVector().multiply(0.2D)));
    }
}
