package old.brainsynder.Gadgets.List;

import gadgets.brainsynder.api.event.GadgetListener;
import gadgets.brainsynder.api.gadget.Gadget;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ParticleMaker;

public class Gadget_NinjaVanish extends Gadget implements GadgetListener {
    public Gadget_NinjaVanish() {
        super(21, "ninja_vanish", Material.FIREWORK_CHARGE, (byte) 0);
    }

    @Override
    public void run(final Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2, true, false));
        Location location = p.getLocation().add(0.0D, 1.0D, 0.0D);
        ParticleRef.sendParticle(ParticleMaker.Particle.EXPLOSION_LARGE, location, 1.0F, 1.2F, 1.0F, 4);
        ParticleRef.sendParticle(ParticleMaker.Particle.SMOKE_NORMAL, location, 1.0F, 1.2F, 1.0F, 6);
        new BukkitRunnable() {
            @Override
            public void run() {
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }.runTaskLater(getPlugin().getPlugin(), 20 * 5);
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
