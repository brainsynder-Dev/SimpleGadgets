package old.brainsynder.Gadgets.List;

import gadgets.brainsynder.api.gadget.Gadget;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;

public class Gadget_QuakeGun extends Gadget {
    public Gadget_QuakeGun() {
        super(9, "quake_gun", Material.DIAMOND_HOE, (byte)0);
    }

    @Override
    public void run(final Player p) {
        for (Location location : RandomRef.getStraightLine(p, 10)) {
            SoundPlayer.playSound(SoundMaker.ENTITY_FIREWORK_LAUNCH, location);
            ParticleRef.sendParticle(ParticleMaker.Particle.SPELL_INSTANT, location, 0.1F, 0.1F, 0.1F, 5);
        }
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
