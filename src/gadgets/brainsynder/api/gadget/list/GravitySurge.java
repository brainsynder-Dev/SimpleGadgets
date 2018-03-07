package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.Cooldown;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import simple.brainsynder.math.MathUtils;

import java.util.Arrays;
import java.util.List;

public class GravitySurge extends Gadget {
    public GravitySurge(GadgetPlugin plugin) {
        super(plugin, "gravity_surge");
    }

    @Override
    public void loadExtraTags() {
        super.loadExtraTags();
        setDefault("cooldown", 15);
        setDefault("radius", 5);
        setDefault("error", "&cRight Click a Block");
    }

    @Override
    public void run(User user) {
        user.getPlayer().sendMessage(getString("error", true));
        if (Cooldown.hasCooldown(user.getPlayer(), this, false)) {
            Cooldown.removeCooldown(user.getPlayer());
        }
    }

    @Override
    public void onBlockClick(User user, Block b) {
        if (b == null) return;
        if (b.getType() == Material.AIR) return;
        Player p = user.getPlayer();

        List<Block> blocks = getPlugin().getBlockUtils().getBlocksInRadius(b.getLocation(), getInteger("radius"), false);
        for (Block block : blocks) {
            if ((block.getRelative(BlockFace.UP) == null) || (block.getRelative(BlockFace.UP).getType() == Material.AIR)) {
                if (block.getLocation().getBlockY() == block.getLocation().getBlockY()) {
                    FallingBlock falling = p.getWorld().spawnFallingBlock(block.getLocation().add(0, 2, 0), block.getState().getData());
                    falling.setDropItem(false);
                    falling.setMetadata("GadgetFB", new FixedMetadataValue(getPlugin().getPlugin(), "GadgetFB"));
                    removableEntities.add(falling);
                    BukkitTask task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (isRemoved()) {
                                cancel();
                                falling.remove();
                                return;
                            }

                            if (!getPlugin().getEntityUtils().isValid(falling)) {
                                cancel();
                                return;
                            }
                            MathUtils.applyVelocity(falling, new Vector(0, MathUtils.random(0.3f, -0.15f), 0));
                        }
                    }.runTaskTimerAsynchronously(getPlugin().getPlugin(), 0, 1);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            task.cancel();
                        }
                    }.runTaskLaterAsynchronously(getPlugin().getPlugin(), 50);
                }
            }
        }
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.CLAY_BALL).withName("&eGravity Surge").withLore(Arrays.asList("&7I pick things up and put them down"));
    }
}
