package old.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.gadget.Gadget;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;

public class Gadget_WaterBomb extends Gadget {
    public Gadget_WaterBomb() {
        super(6, "water_bomb", Material.WATER_BUCKET);
    }

    @Override
    public void run(final Player player) {
        final Item bukkit = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.WATER_BUCKET));
        bukkit.setMetadata("takeable", new FixedMetadataValue(Core.get(), "takeable"));
        bukkit.setVelocity(player.getEyeLocation().getDirection());
        bukkit.setPickupDelay(Integer.MAX_VALUE);
        BukkitRunnable waterBomb = new BukkitRunnable() {
            @Override
            public void run() {
                SoundPlayer.playSound(SoundMaker.ENTITY_GENERIC_EXPLODE, bukkit.getLocation());
                for(int x1 = 0; x1 <= 16; x1++) {
                    double x = (double)(-0.5F + (float)(Math.random() * 2.0D));
                    double y = 1.0D;
                    double z = (double)(-0.5F + (float)(Math.random() * 2.0D));
                    Location eLoc = bukkit.getLocation();
                    World w = eLoc.getWorld();
                    Location bLoc = bukkit.getLocation();
                    FallingBlock fallingBlock = w.spawnFallingBlock(bLoc, Material.WATER, (byte)0);
                    fallingBlock.setMetadata("GadgetFB", new FixedMetadataValue(Core.get(), "GadgetFB"));
                    fallingBlock.setDropItem(false);
                    fallingBlock.setVelocity(new Vector(x, y, z));
                    ParticleRef.sendParticle(ParticleMaker.Particle.WATER_SPLASH, fallingBlock.getLocation(), 1.5F, 1.5F, 1.5F, 20);
                    ParticleRef.sendParticle(ParticleMaker.Particle.WATER_DROP, fallingBlock.getLocation (), 1.5F, 1.5F, 1.5F, 20);
                }

                bukkit.remove();
            }
        };
        waterBomb.runTaskLater (Core.get (), 40);
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
