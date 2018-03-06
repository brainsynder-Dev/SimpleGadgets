package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;

public class WaterBomb extends Gadget {
    public WaterBomb(GadgetPlugin plugin) {
        super(plugin, "water_bomb");
    }

    @Override
    public void run(User user) {
        Player player = user.getPlayer();

        Item bukkit = getPlugin().getEntityUtils().launchItem(player, new ItemStack(Material.WATER_BUCKET));
        bukkit.setPickupDelay(Integer.MAX_VALUE);
        new BukkitRunnable() {
            @Override
            public void run() {
                SoundMaker.ENTITY_GENERIC_EXPLODE.playSound(bukkit.getLocation());
                new ParticleMaker(ParticleMaker.Particle.WATER_SPLASH, 10, 1)
                        .sendToLocation(bukkit.getLocation());
                new ParticleMaker(ParticleMaker.Particle.WATER_DROP, 10, 1)
                        .sendToLocation(bukkit.getLocation());

                for(int x1 = 0; x1 <= 16; x1++) {
                    double x = (double)(-0.5F + (float)(Math.random() * 1.0D));
                    double y = 1.0D;
                    double z = (double)(-0.5F + (float)(Math.random() * 1.0D));
                    Location eLoc = bukkit.getLocation();
                    World w = eLoc.getWorld();
                    Location bLoc = bukkit.getLocation();
                    FallingBlock fallingBlock = w.spawnFallingBlock(bLoc, new MaterialData(Material.WATER));
                    fallingBlock.setMetadata("GadgetFB", new FixedMetadataValue(Core.get(), "GadgetFB"));
                    fallingBlock.setDropItem(false);
                    fallingBlock.setVelocity(new Vector(x, y, z));
                    removableEntities.add(fallingBlock);
                }

                bukkit.remove();
            }
        }.runTaskLater (Core.get (), 40);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.WATER_BUCKET).withName("&eWater Bomb");
    }
}
