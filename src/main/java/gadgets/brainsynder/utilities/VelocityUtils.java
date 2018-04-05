package gadgets.brainsynder.utilities;

import gadgets.brainsynder.api.GadgetPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

@SuppressWarnings("unused")
public class VelocityUtils {
    private GadgetPlugin plugin;

    public VelocityUtils (GadgetPlugin plugin) {
        this.plugin = plugin;
    }

    private Vector getBumpVector(Entity entity, Location from, double power) {
        Vector bump = entity.getLocation().toVector().subtract(from.toVector()).normalize();
        bump.multiply(power);
        return bump;
    }

    private Vector getPullVector(Entity entity, Location to, double power) {
        Vector pull = to.toVector().subtract(entity.getLocation().toVector()).normalize();
        pull.multiply(power);
        return pull;
    }

    public void bumpEntity(Entity entity, Location from, double power) {
        entity.setVelocity(getBumpVector(entity, from, power));
    }

    public void bumpEntity(Entity entity, Location from, double power, double fixedY) {
        Vector vector = getBumpVector(entity, from, power);
        vector.setY(fixedY);
        entity.setVelocity(vector);
    }

    public void pullEntity(Entity entity, Location to, double power) {
        entity.setVelocity(getPullVector(entity, to, power));
    }

    public void pullEntity(Entity entity, Location from, double power, double fixedY) {
        Vector vector = getPullVector(entity, from, power);
        vector.setY(fixedY);
        entity.setVelocity(vector);
    }

    public void velocity(Entity ent, double str, double yAdd, double yMax) {
        velocity(ent, ent.getLocation().getDirection(), str, false, 0.0D, yAdd, yMax);
    }

    private void velocity(Entity ent, Vector vec, double str, boolean ySet, double yBase, double yAdd, double yMax) {
        if ((Double.isNaN(vec.getX()))
                || (Double.isNaN(vec.getY()))
                || (Double.isNaN(vec.getZ()))
                || (vec.length() == 0.0D)) return;

        if (ySet) vec.setY(yBase);

        vec.normalize();
        vec.multiply(str);

        vec.setY(vec.getY() + yAdd);
        if (vec.getY() > yMax) vec.setY(yMax);

        ent.setFallDistance(0.0F);
        ent.setVelocity(vec);
    }

    public Vector rotateAroundAxisX(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    public Vector rotateAroundAxisY(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public Vector rotateAroundAxisZ(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos - v.getY() * sin;
        double y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }

    public Vector rotateVector(Vector v, double angleX, double angleY, double angleZ) {
        rotateAroundAxisX(v, angleX);
        rotateAroundAxisY(v, angleY);
        rotateAroundAxisZ(v, angleZ);
        return v;
    }

    public double angleToXAxis(Vector vector) {
        return Math.atan2(vector.getX(), vector.getY());
    }

    public void velocity(Entity ent, double str, double yAdd, double yMax, boolean groundBoost) {
        velocity(ent, ent.getLocation().getDirection(), str, false, 0.0D, yAdd, yMax, groundBoost);
    }

    private void velocity(Entity ent, Vector vec, double str, boolean ySet, double yBase, double yAdd, double yMax, boolean groundBoost) {
        if ((Double.isNaN(vec.getX()))
                || (Double.isNaN(vec.getY()))
                || (Double.isNaN(vec.getZ()))
                || (vec.length() == 0.0D)) return;

        if (ySet) vec.setY(yBase);

        vec.normalize();
        vec.multiply(str);

        vec.setY(vec.getY() + yAdd);

        if (vec.getY() > yMax) vec.setY(yMax);
        if ((groundBoost) && (plugin.getBlockUtils().isOnGround(ent))) vec.setY(vec.getY() + 0.2D);

        ent.setFallDistance(0.0F);
        ent.setVelocity(vec);
    }
}
