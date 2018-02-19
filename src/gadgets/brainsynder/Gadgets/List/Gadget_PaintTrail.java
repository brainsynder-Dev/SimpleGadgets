package gadgets.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Gadget_PaintTrail extends Gadget {
    public Gadget_PaintTrail() {
        super(13, "paint_trail", Material.INK_SACK, (byte) 14);
    }

    @Override
    public void run(final Player p) {
        final BukkitRunnable paintMaker = new BukkitRunnable() {
            Random rand = new Random();
            int i = rand.nextInt(2) + 1;
            int data = rand.nextInt(15) + 1;
            @Override
            public void run() {
                if (!p.isOnline()) {
                    cancel();
                    return;
                }
                if (!p.isOnGround()) {
                    return;
                }
                Block block = p.getLocation().subtract(0.0, 1.0, 0.0).getBlock();
                BlockUtils.setToRestore(Core.get(), block, (i == 0) ? Material.WOOL : Material.STAINED_CLAY, (byte) data, 50);
                ParticleRef.sendParticle(block.getLocation(), (i == 1) ? Material.WOOL : Material.STAINED_CLAY, (byte) data, 1.0F, 1.0F, 1.0F, 10);
                i = rand.nextInt(2) + 1;
                data = rand.nextInt(15) + 1;
            }
        };
        paintMaker.runTaskTimer(getPlugin().getGadgetPlugin(), 0, 1);
        new BukkitRunnable() {
            @Override
            public void run() {
                paintMaker.cancel();
            }
        }.runTaskLater(getPlugin().getGadgetPlugin(), 20 * 5);

    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
