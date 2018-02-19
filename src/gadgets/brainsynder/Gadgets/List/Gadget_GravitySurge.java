package gadgets.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.Gadgets.Gadget;
import gadgets.brainsynder.Utils.Cooldown;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import simple.brainsynder.math.MathUtils;

import java.util.List;

public class Gadget_GravitySurge extends Gadget {
    public Gadget_GravitySurge() {
        super(27, "gravity_surge", Material.CLAY_BALL, (byte) 0);
    }

    @Override
    public void loadExtraTags() {
        super.loadExtraTags();
        Core.getLanguage().set(true, "Gadgets." + getIdName() + ".CooldownTime", 15);
        Core.getLanguage().set(true, "Gadgets." + getIdName() + ".Radius", 5);
        Core.getLanguage().set(true, "Gadgets." + getIdName() + ".RightClickBlock", "&cRight Click a Block");
    }

    @Override public void run(Player player) {
        player.sendMessage(Core.getLanguage().getString("Gadgets." + getIdName() + ".RightClickBlock", true));
        if (Cooldown.hasCooldown(player, this, false)) {
            Cooldown.removeCooldown(player);
        }
    }

    @Override
    public void onBlockClick(final Player p, Block b) {
        if (b == null)
            return;
        if (b.getType() == Material.AIR)
            return;
        List<Block> blocks = BlockUtils.getBlocksInRadius(b.getLocation(), Core.getLanguage().getInt("Gadgets." + getIdName() + ".Radius"), false);
        for (Block block : blocks) {
            if ((block.getRelative(BlockFace.UP) == null) || (block.getRelative(BlockFace.UP).getType() == Material.AIR))
                if (block.getLocation().getBlockY() == b.getLocation().getBlockY()) {
                    final FallingBlock falling = p.getWorld().spawnFallingBlock(block.getLocation().add(0, 2, 0), block.getType(), block.getData());
                    falling.setDropItem(false);
                    falling.setMetadata("GadgetFB", new FixedMetadataValue(getPlugin().getGadgetPlugin(), "GadgetFB"));
                    final BukkitTask task = new BukkitRunnable() {
                        @Override public void run() {
                            if (falling.isValid())
                                MathUtils.applyVelocity(falling, new Vector(0, MathUtils.random(0.3f, -0.15f), 0));
                        }
                    }.runTaskTimerAsynchronously(getPlugin().getGadgetPlugin(), 0, 1);
                    new BukkitRunnable() {
                        @Override public void run() {
                            falling.remove();
                            task.cancel();
                        }
                    }.runTaskLaterAsynchronously(getPlugin().getGadgetPlugin(), 50);
                }
        }
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
