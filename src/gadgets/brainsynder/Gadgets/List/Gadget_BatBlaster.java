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

import gadgets.brainsynder.Core;
import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.utils.AdvMap;

import java.util.ArrayList;

public class Gadget_BatBlaster extends Gadget {
    private static AdvMap<String, ArrayList<Bat>> bats = new AdvMap<>();

    public Gadget_BatBlaster() {
        super(2, "bat_blaster", Material.IRON_BARDING);
    }

    @Override
    public void run(final Player p) {
        bats.put(p.getName(), new ArrayList<Bat>());
        for (int i = 0; i < 10; i++) {
            Bat bat = EntityRef.spawnMob(p.getEyeLocation(), Bat.class);
            if (bat == null) {
                continue;
            }
            bats.getKey(p.getName()).add(bat);
        }
        runBatParticles(p);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (bats.getKey(p.getName()) == null) {
                    return;
                }
                for (Bat bat : bats.getKey(p.getName())) {
                    if (bat.isValid()) {
                        bat.remove();
                    }
                    bats.remove(p.getName());
                }
            }
        }.runTaskLater(Core.get(), 59L);
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }

    private void runBatParticles(final Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.isOnline()) {
                    if (!bats.containsKey(p.getName ())) {
                        cancel ();
                        return;
                    }
                    for (Bat bat : bats.getKey(p.getName())) {
                        if (bat.isValid()) {
                            bat.remove();
                        }
                        bats.remove(p.getName());
                    }
                    cancel();
                    return;
                }


                if (!bats.containsKey(p.getName ())) {
                    cancel ();
                    return;
                }
                Location fwe = p.getEyeLocation().clone();
                for (Bat bat : bats.getKey(p.getName())) {
                    if (!bat.isValid()) {
                        cancel();
                        return;
                    }
                    ParticleRef.sendParticle(ParticleMaker.Particle.SMOKE_LARGE, bat.getLocation(), 0.2F, 0.2F, 0.2F, 3);
                    bat.setVelocity(fwe.getDirection().clone().multiply(0.9D));
                }
            }
        }.runTaskTimer(Core.get(), 0, 1);
    }
}
