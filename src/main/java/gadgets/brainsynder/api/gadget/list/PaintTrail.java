package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.BlockChanger;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.math.MathUtils;

public class PaintTrail extends BlockChanger {
    public PaintTrail(GadgetPlugin plugin) {
        super(plugin, "paint_trail");
    }

    @Override
    public void run(User user) {
        Player p = user.getPlayer();

        BukkitRunnable paintMaker = new BukkitRunnable() {
            @Override
            public void run() {
                if (isRemoved()) {
                    cancel();
                    return;
                }

                if (!getPlugin().getEntityUtils().isValid(p)) {
                    cancel();
                    return;
                }
                if (!p.isOnGround()) return;

                Block block = p.getLocation().subtract(0.0, 1.0, 0.0).getBlock();
                if (!getPlugin().getBlockUtils().canChange(block)) return;
                if (storage.contains(block)) return;
                storage.addBlock(block);
                Material material= (MathUtils.random(1,2) == 1) ? Material.WOOL : Material.STAINED_CLAY;
                block.setType(material);
                block.setData((byte) MathUtils.random(0,15));
                getPlugin().getUtilities().blockParticles(block);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        storage.reset(block);
                    }
                }.runTaskLater(getPlugin().getPlugin(), 40);
            }
        };
        paintMaker.runTaskTimer(getPlugin().getPlugin(), 0, 1);
        new BukkitRunnable() {
            @Override
            public void run() {
                paintMaker.cancel();
                storage.reset();
            }
        }.runTaskLater(getPlugin().getPlugin(), 20 * 5);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SKULL_ITEM).withData(3).setTexture("http://textures.minecraft.net/texture/f72b5cf4fb2e3d9158a4f5ece714970bcc1371ca029e645555927fd156e184").withName("&ePaint Trail");
    }
}
