package gadgets.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;

public class Gadget_BBQ extends Gadget {
    public Gadget_BBQ() {
        super(23, "bbq_cannon", Material.COOKED_BEEF);
    }

    @Override
    public void run(final Player p) {
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i >= 8) {
                    cancel();
                    return;
                }
                if (!p.isValid()) {
                    cancel();
                    return;
                }
                if (!p.isOnline()) {
                    cancel();
                    return;
                }

                final Item item = p.getWorld().dropItem(p.getEyeLocation(), new ItemMaker(Material.COOKED_BEEF).setName("" + Math.random()).create());
                item.setVelocity(p.getEyeLocation().getDirection());
                item.setFireTicks(156151);
                item.setMetadata("takeable", new FixedMetadataValue(Core.get(), "takeable"));
                SoundPlayer.playSound(SoundMaker.ENTITY_CHICKEN_EGG, p.getLocation());
                final BukkitRunnable flameParticles = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!item.isValid()) {
                            cancel();
                            return;
                        }
                        ParticleMaker maker = new ParticleMaker(ParticleMaker.Particle.ITEM_CRACK, 2, 0.2);
                        maker.setData(Material.COOKED_BEEF);
                        maker.sendToLocation(item.getLocation());
                        ParticleRef.sendParticle(ParticleMaker.Particle.FLAME, item.getLocation(), 0.0F, 0.2F, 0.0F, 2);
                    }
                };
                flameParticles.runTaskTimer(getPlugin().getGadgetPlugin(), 0, 2);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        flameParticles.cancel();
                        ParticleMaker maker = new ParticleMaker(ParticleMaker.Particle.SPELL_INSTANT, 10, 1, 1, 1);
                        maker.sendToLocation(item.getLocation());
                        SoundPlayer.playSound(SoundMaker.ENTITY_FIREWORK_SHOOT, p.getLocation());
                        item.remove();
                    }
                }.runTaskLaterAsynchronously(Core.get(), 15);
                i++;
            }
        }.runTaskTimer(Core.get(), 0, 10);
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
