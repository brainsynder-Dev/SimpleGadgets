package gadgets.brainsynder.Utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Objects;

public class VelocityUtils {
    public VelocityUtils() {
    }

    public static Entity[] getNearbyEntities(Location l, int radius) {
        int chunk = radius < 16?1:(radius - radius % 16) / 16;
        ArrayList<Entity> radiusEntities = new ArrayList<>();

        for(int ent = 0 - chunk; ent <= chunk; ++ent) {
            for(int chZ = 0 - chunk; chZ <= chunk; ++chZ) {
                int x = (int)l.getX();
                int y = (int)l.getY();
                int z = (int)l.getZ();
                Location loc = new Location(l.getWorld(), (double)(x + ent * 16), (double)y, (double)(z + chZ * 16));

                for(Entity e : loc.getChunk().getEntities()) {
                    if(Objects.equals(e.getLocation().getWorld().getName(), l.getWorld().getName()) && e.getLocation().distance(l) <= (double)radius && e.getLocation().getBlock() != l.getBlock()) {
                        radiusEntities.add(e);
                    }
                }
            }
        }

        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

    public static Vector getVector(Entity entity, Location from) {
        Vector s = entity.getLocation().toVector().subtract(from.toVector()).normalize();
        s.multiply(2);
        return s;
    }

    public static void pushEntity(Entity entity, Location place) {
        Vector v = entity.getLocation().toVector().subtract(place.toVector()).multiply(0.5D).add(new Vector(0.0D, 0.5D, 0.0D));
        v.setY(0);
        v.add(new Vector(0, 1, 0));
        MathUtils.applyVelocity(entity, v.add(MathUtils.getRandomCircleVector().multiply(0.2D)));
    }
}
