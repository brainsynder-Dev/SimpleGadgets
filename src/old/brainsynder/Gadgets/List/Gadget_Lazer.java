package old.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.utilities.VelocityUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;

public class Gadget_Lazer extends Gadget {
    public Gadget_Lazer() {
        super(19, "particle_lazer", Material.CLAY_BRICK);
    }

    @Override
    public void run(Player player) {
        int length = Core.getLanguage().getInt("Gadgets." + getIdName() + ".MaxBeamLength");
        boolean push = Core.getLanguage().getBoolean("Gadgets." + getIdName() + ".LaunchMobs");
        laserbeam(player, Color.RED, length, push);
    }

    @Override
    public void loadExtraTags() {
        Core.getLanguage().set(true, "Gadgets." + getIdName() + ".LaunchMobs", true);
        Core.getLanguage().set(true, "Gadgets." + getIdName() + ".MaxBeamLength", 100);
    }

    protected void laserbeam(LivingEntity user, Color color, int length, boolean pushMobs) {
        Location start = RandomRef.getEyeLocation(user);
        SoundPlayer.playSound(SoundMaker.ENTITY_ARROW_HIT, start, 0.5F, 0.5F);
        Location end = RandomRef.getTargetLocation(user);
        double dist = Math.abs(end.distance(start));
        for (int i = -1; i < length; ++i) {
            double delta = (double) i / dist;
            double x = (1.0D - delta) * start.getX() + delta * (end.getX() + 0.5D);
            double y = (1.0D - delta) * start.getY() + delta * (end.getY() + 0.5D);
            double z = (1.0D - delta) * start.getZ() + delta * (end.getZ() + 0.5D);
            Location l = new Location(start.getWorld(), x, y, z);
            if (l.getBlock().getType().isSolid()) {
                ParticleRef.sendParticle (ParticleMaker.Particle.EXPLOSION_HUGE, l, 1.0, 1.0, 1.0, 0.0, 1);
                SoundPlayer.playSound(SoundMaker.ENTITY_GENERIC_EXPLODE, l);
                break;
            }
            ParticleMaker maker = new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, color);
            maker.sendToLocation(l);
            if (pushMobs) {
                for (LivingEntity entity : start.getWorld().getLivingEntities()) {
                    if ((entity != user) && (!entity.hasMetadata("NPC"))) {
                        if (l.distanceSquared(entity.getEyeLocation()) < 4.0D) {
                            VelocityUtils.pushEntity(entity, l);
                        }
                    }
                }
            }

        }

    }

    protected void laserbeam(LivingEntity user, ParticleMaker.Particle particle, float[] offsets, int length, boolean pushMobs) {
        Location start = RandomRef.getEyeLocation(user);
        Location end = RandomRef.getTargetLocation(user);
        double dist = Math.abs(end.distance(start));
        for (int i = -1; i < length; ++i) {
            double delta = (double) i / dist;
            double x = (1.0D - delta) * start.getX() + delta * (end.getX() + 0.5D);
            double y = (1.0D - delta) * start.getY() + delta * (end.getY() + 0.5D);
            double z = (1.0D - delta) * start.getZ() + delta * (end.getZ() + 0.5D);
            Location l = new Location(start.getWorld(), x, y, z);
            if (l.getBlock().getType().isSolid()) {
                ParticleRef.sendParticle(ParticleMaker.Particle.EXPLOSION_HUGE, l, 1.0, 1.0, 1.0, 0.0, 1);
                SoundPlayer.playSound(SoundMaker.ENTITY_GENERIC_EXPLODE, l);
                break;
            }
            ParticleRef.sendParticle(particle, l, offsets[0], offsets[1], offsets[2], 1);
            if (pushMobs) {
                for (LivingEntity entity : start.getWorld().getLivingEntities()) {
                    if (entity != user) {
                        if (l.distanceSquared(entity.getEyeLocation()) < 4.0D) {
                            VelocityUtils.pushEntity(entity, l);
                        }
                    }
                }
            }

        }

    }

    @Override
    public void onUserMove(Player player) {

    }
}
