package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;

public class FunCannon extends Gadget{
    public FunCannon(GadgetPlugin plugin) {
        super(plugin, "fun_cannon");
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.BLAZE_ROD, 1).withName("&eFun Cannon");
    }

    @Override
    public void run(User player) {
        getPlugin().getEntityUtils().launchProjectile(player.getPlayer(), Snowball.class);
    }

    @Override
    public void onProjectileHit(User player, Projectile projectile) {
        new ParticleMaker (ParticleMaker.Particle.LAVA, 20, 0.5F, 0.8F, 0.5F).sendToLocation(projectile.getLocation ());
        new ParticleMaker (ParticleMaker.Particle.HEART, 15, 0.5F, 0.8F, 0.5F).sendToLocation(projectile.getLocation ());
        SoundMaker.ENTITY_CAT_PURREOW.playSound(projectile.getLocation());
    }
}
