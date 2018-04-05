package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;

public class Laser extends Gadget {
    public Laser(GadgetPlugin plugin) {
        super(plugin, "particle_laser");
    }

    @Override
    public void loadExtraTags() {
        super.loadExtraTags();
        setDefault("LaunchMobs", true);
        setDefault("length", 100);
    }

    @Override
    public void run(User user) {
        laserbeam(user.getPlayer(), Color.RED, getInteger("length"), getBoolean("LaunchMobs"));
    }

    private void laserbeam(LivingEntity user, Color color, int length, boolean pushMobs) {
        Location start = getPlugin().getUtilities().getEyeLocation(user);
        SoundMaker.ENTITY_GENERIC_EXPLODE.playSound(start, 0.5F, 0.5F);
        Location end = getPlugin().getUtilities().getTargetLocation(user);
        double dist = Math.abs(end.distance(start));
        for (int i = -1; i < length; ++i) {
            double delta = (double) i / dist;
            double x = (1.0D - delta) * start.getX() + delta * (end.getX() + 0.5D);
            double y = (1.0D - delta) * start.getY() + delta * (end.getY() + 0.5D);
            double z = (1.0D - delta) * start.getZ() + delta * (end.getZ() + 0.5D);
            Location l = new Location(start.getWorld(), x, y, z);
            if (l.getBlock().getType().isSolid()) {
                ParticleMaker maker = new ParticleMaker(ParticleMaker.Particle.EXPLOSION_HUGE, 1, 1.0);
                maker.sendToLocation(l);
                SoundMaker.ENTITY_GENERIC_EXPLODE.playSound(l);

                if (pushMobs) {
                    for (LivingEntity entity : start.getWorld().getLivingEntities()) {
                        //if ((entity != user) && (!entity.hasMetadata("NPC"))) {
                            if (l.distanceSquared(entity.getLocation()) <= 5) {
                                getPlugin().getUtilities().pushEntity(entity, l);
                            }
                        //}
                    }
                }
                break;
            }
            ParticleMaker maker = new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, color);
            maker.sendToLocation(l);
        }
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.CLAY_BRICK).withName("&eParticle Laser");
    }
}
