/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package gadgets.brainsynder.Gadgets.List;

import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;

public class Gadget_FunCannon extends Gadget {
	public Gadget_FunCannon () {
		super (1, "fun_cannon", Material.BLAZE_ROD);
	}

	@Override
	public void run (Player player) {
        ProjectileRef.launchProjectile(player, Snowball.class);
	}

	@Override
	public void onUserMove (Player player) {

	}

	@Override
	public void onProjectileHit (Projectile projectile) {
		ParticleRef.sendParticle (ParticleMaker.Particle.LAVA, projectile.getLocation (), 0.5F, 0.8F, 0.5F, 20);
		ParticleRef.sendParticle (ParticleMaker.Particle.HEART, projectile.getLocation (), 0.5F, 0.8F, 0.5F, 15);
		SoundPlayer.playSound(SoundMaker.ENTITY_CAT_PURREOW, projectile.getLocation());
	}
}
