package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ParticleMaker;

public class NinjaVanish extends Gadget {
    private Player p = null;
    public NinjaVanish(GadgetPlugin plugin) {
        super(plugin, "ninja_vanish");
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (p == null) return;
        if (!getPlugin().getEntityUtils().isValid(p)) return;
        if (!p.hasPotionEffect(PotionEffectType.INVISIBILITY)) return;
        p.removePotionEffect(PotionEffectType.INVISIBILITY);
        p = null;
    }

    @Override
    public void run(User user) {
        p = user.getPlayer();
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2, true, false));
        Location location = p.getLocation().add(0.0D, 1.0D, 0.0D);
        new ParticleMaker (ParticleMaker.Particle.EXPLOSION_LARGE, 4, 1,1.2,10)
                .sendToLocation(location);
        new ParticleMaker (ParticleMaker.Particle.SMOKE_NORMAL, 6, 1,1.2,10)
                .sendToLocation(location);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isRemoved()) return;
                if (!getPlugin().getEntityUtils().isValid(p)) return;
                new ParticleMaker (ParticleMaker.Particle.SMOKE_NORMAL, 6, 1,1.2,10)
                        .sendToLocation(p.getLocation().add(0.0D, 1.0D, 0.0D));
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }.runTaskLater(getPlugin().getPlugin(), 20 * 5);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.FIREWORK_CHARGE).withName("&eNinja Vanish");
    }
}
