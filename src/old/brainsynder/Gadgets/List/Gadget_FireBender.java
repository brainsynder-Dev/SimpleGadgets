package old.brainsynder.Gadgets.List;

import gadgets.brainsynder.api.event.GadgetListener;
import gadgets.brainsynder.api.gadget.Gadget;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ParticleMaker;

import java.util.ArrayList;
import java.util.List;

public class Gadget_FireBender extends Gadget implements GadgetListener {
    public Gadget_FireBender() {
        super(14, "fire_bender", Material.FLINT, (byte) 0);
    }

    private List<Player> isUsing = new ArrayList<>();
    @Override
    public void run(final Player p) {
        isUsing.add(p);
        p.setFireTicks(20 * 3);
        final BukkitRunnable flameParticles = new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.isOnline()) {
                    cancel();
                    return;
                }
                Location location = p.getLocation().add(0.0D, 1.0D, 0.0D);
                ParticleRef.sendParticle(ParticleMaker.Particle.LAVA, location, 0.0F, 0.2F, 0.0F, 2);
                ParticleRef.sendParticle(ParticleMaker.Particle.SMOKE_NORMAL, location, 0.0F, 0.2F, 0.0F, 2);
            }
        };
        flameParticles.runTaskTimer(getPlugin().getPlugin(), 0, 2);
        new BukkitRunnable() {
            @Override
            public void run() {
                isUsing.remove(p);
                flameParticles.cancel();
            }
        }.runTaskLater(getPlugin().getPlugin(), 20 * 5);
    }

    @EventHandler
    private void onFire (EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if ((e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)
                    || (e.getCause() == EntityDamageEvent.DamageCause.FIRE)) {
                Player player = (Player)e.getEntity();
                if (Gadget.Variables.hasGadget(player)) {
                    Gadget gadget = Gadget.Variables.getGadget(player);
                    if (gadget.getIdName().equals("fire_bender")) {
                        if (isUsing.contains(player)) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
