package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.BlockChanger;
import gadgets.brainsynder.api.gadget.GadgetInfo;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.math.MathUtils;
import simple.brainsynder.sound.SoundMaker;

@GadgetInfo(projectileHit = true)
public class PaintSprayer extends BlockChanger {
    public PaintSprayer(GadgetPlugin plugin) {
        super(plugin, "paint_sprayer");
    }

    @Override
    public void run(User user) {
        Snowball projectile = getPlugin().getEntityUtils().launchProjectile(user.getPlayer(), Snowball.class);
        getPlugin().getUtilities().noArc(projectile, user.getPlayer().getLocation().getDirection().multiply(3));
    }

    @Override
    public void onProjectileHit(User user, Projectile projectile, Location location) {
        //Location location = projectile.getLocation();
        SoundMaker.ENTITY_CHICKEN_EGG.playSound(location, 0.5F, 0.3F);
        for (Block block2 : getPlugin().getBlockUtils().getBlocksInRadius(location, MathUtils.random(1,3), false)) {
            if (!getPlugin().getBlockUtils().canChange(block2)) continue;
            if (storage.contains(block2)) continue;
            storage.addBlock(block2);
            Material material = (MathUtils.random(1,2) == 1) ? Material.WOOL : Material.STAINED_CLAY;
            block2.setType(material);
            block2.setData((byte) MathUtils.random(0,16));
            getPlugin().getUtilities().blockParticles(block2);
            new BukkitRunnable() {
                @Override
                public void run() {
                    storage.reset(block2);
                }
            }.runTaskLater(getPlugin().getPlugin(), 60);
        }
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.GOLD_BARDING).withName("Paint Sprayer");
    }
}
