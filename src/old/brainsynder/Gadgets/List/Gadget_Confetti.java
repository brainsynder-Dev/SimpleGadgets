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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import simple.brainsynder.sound.SoundMaker;

public class Gadget_Confetti extends Gadget {
    public Gadget_Confetti() {
        super(4, "confetti", Material.DIAMOND_BARDING);
    }

    @Override
    public void run(Player player) {
        Location localLocation1 = player.getEyeLocation();
        double d1 = localLocation1.getX();
        double d2 = localLocation1.getY();
        double d3 = localLocation1.getZ();
        double d4 = Math.toRadians(localLocation1.getYaw() + 90.0F);
        double d5 = Math.toRadians(localLocation1.getPitch() + 90.0F);
        double d6 = Math.sin(d5) * Math.cos(d4);
        double d7 = Math.sin(d5) * Math.sin(d4);
        double d8 = Math.cos(d5);
        for (double i = 0.9; i < 2.5; i += 0.1) {
            Location localLocation2 = new Location(player.getWorld(), d1 + i * d6, (d2 + i * d8), d3 + i * d7);
            if (localLocation2.getBlock().getType().isSolid()) break;
            ParticleRef.sendParticle(localLocation2, Material.INK_SACK, (short) 1, 0.3F, 0.3F, 0.3F, 10);
            ParticleRef.sendParticle(localLocation2, Material.INK_SACK, (short) 15, 0.3F, 0.3F, 0.3F, 10);
            ParticleRef.sendParticle(localLocation2, Material.INK_SACK, (short) 10, 0.3F, 0.3F, 0.3F, 10);
            ParticleRef.sendParticle(localLocation2, Material.INK_SACK, (short) 4, 0.3F, 0.3F, 0.3F, 10);
            ParticleRef.sendParticle(localLocation2, Material.INK_SACK, (short) 11, 0.3F, 0.3F, 0.3F, 10);
            ParticleRef.sendParticle(localLocation2, Material.INK_SACK, (short) 14, 0.3F, 0.3F, 0.3F, 10);
            ParticleRef.sendParticle(localLocation2, Material.INK_SACK, (short) 12, 0.3F, 0.3F, 0.3F, 10);
        }
        SoundPlayer.playSound(SoundMaker.ENTITY_EXPERIENCE_ORB_PICKUP, player.getLocation());
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
