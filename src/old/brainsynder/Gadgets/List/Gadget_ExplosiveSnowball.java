/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package old.brainsynder.Gadgets.List;

import gadgets.brainsynder.api.gadget.Gadget;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;

public class Gadget_ExplosiveSnowball extends Gadget {
    public Gadget_ExplosiveSnowball() {
        super(5, "explosive_snowball", Material.SNOW_BALL);
    }

    @Override
    public void run(Player p) {
        ProjectileRef.launchProjectile(p, Snowball.class, 2.5);
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {
        SoundPlayer.playSound(SoundMaker.ENTITY_GENERIC_EXPLODE, projectile.getLocation());
        ParticleRef.sendParticle(ParticleMaker.Particle.EXPLOSION_HUGE, projectile.getLocation(), 2.0F, 2.0F, 2.0F, 40);
        ParticleRef.sendParticle(ParticleMaker.Particle.FLAME, projectile.getLocation(), 2.0F, 2.0F, 2.0F, 10);
    }
}
