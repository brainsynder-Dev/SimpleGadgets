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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.plugin.java.JavaPlugin;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.trailer.AsyncTrailer;
import simple.brainsynder.trailer.IAsyncTrailer;

public class Gadget_TrailBlazer extends Gadget {
    public Gadget_TrailBlazer() {
        super(3, "trail_blazer", Material.SNOW_BALL);
    }

    @Override
    public void run(Player p) {
        Projectile proj = ProjectileRef.launchProjectile(p, Snowball.class);
        final IAsyncTrailer<Entity> trailer = new AsyncTrailer<>();
        trailer.setTarget(proj);
        trailer.setPlayer(p);
        IStorage<ParticleMaker> iStorage = new StorageList<>();
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.LAVA, 10, 0.5, 0.5, 0.5));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.HEART, 10, 0.5, 0.5, 0.5));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.SPELL_WITCH, 10, 0.5, 0.5, 0.5));
        trailer.setParticles(iStorage);
        trailer.start((JavaPlugin) getPlugin().getPlugin());
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
