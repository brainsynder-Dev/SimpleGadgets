package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.gadget.GadgetInfo;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;

@GadgetInfo(projectileHit = true)
public class ExplosiveSnowball extends Gadget {
    public ExplosiveSnowball(GadgetPlugin plugin) {
        super(plugin, "explosive_snowball");
    }

    @Override
    public void run(User user) {
        getPlugin().getEntityUtils().launchProjectile(user.getPlayer(), Snowball.class, 2.5);
    }

    @Override
    public void onProjectileHit(User user, Projectile projectile) {
        SoundMaker.ENTITY_GENERIC_EXPLODE.playSound(projectile.getLocation());
        new ParticleMaker(ParticleMaker.Particle.EXPLOSION_HUGE, 40,
                2.0F, 2.0F, 2.0F).sendToLocation(projectile.getLocation());
        new ParticleMaker(ParticleMaker.Particle.FLAME, 10,
                2.0F, 2.0F, 2.0F).sendToLocation(projectile.getLocation());
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SNOW_BALL, 1).withName("&eExplosive Snowball");
    }
}
