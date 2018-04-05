package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.math.MathUtils;
import simple.brainsynder.sound.SoundMaker;

import java.util.List;

public class Laser extends Gadget {
    public Laser(GadgetPlugin plugin) {
        super(plugin, "particle_laser");
    }

    @Override
    public void loadExtraTags() {
        super.loadExtraTags();
        setDefault("LaunchBlocks", "true");
        setDefault("length", 100);
        setDefault("radius", 3);
    }

    @Override
    public void run(User user) {
        laserbeam(user.getPlayer(), Color.RED, getInteger("length"), Boolean.valueOf(getString("LaunchBlocks", false)));
    }

    private void laserbeam(LivingEntity user, Color color, int length, boolean launchBlocks) {
        Location start = getPlugin().getUtilities().getEyeLocation(user);
        SoundMaker.ENTITY_GENERIC_EXPLODE.playSound(start, 0.5F, 0.5F);
        Location end = getPlugin().getUtilities().getTargetLocation(user);
        double dist = Math.abs(end.distance(start));
        for (int i = -1; i < length; ++i) {
            double delta = (double) i / dist;
            double x = (1.0D - delta) * start.getX() + delta * (end.getX() + 0.5D);
            double y = (1.0D - delta) * start.getY() + delta * (end.getY() + 0.5D);
            double z = (1.0D - delta) * start.getZ() + delta * (end.getZ() + 0.5D);
            Location l = new Location(start.getWorld(), x, y, z);
            if (l.getBlock().getType().isSolid()) {
                ParticleMaker maker = new ParticleMaker(ParticleMaker.Particle.EXPLOSION_HUGE, 1, 1.0);
                maker.sendToLocation(l);
                SoundMaker.ENTITY_GENERIC_EXPLODE.playSound(l);

                if (launchBlocks) {
                    List<Block> blocks = getPlugin().getBlockUtils().getBlocksInRadius(l, getInteger("radius"), false);
                    for (Block block : blocks) {
                        if ((block.getRelative(BlockFace.UP) == null) || (block.getRelative(BlockFace.UP).getType() == Material.AIR)) {
                            if (block.getLocation().getBlockY() == block.getLocation().getBlockY()) {
                                FallingBlock falling = user.getWorld().spawnFallingBlock(block.getLocation().add(0, 2, 0), block.getState().getData());
                                falling.setDropItem(false);
                                falling.setMetadata("GadgetFB", new FixedMetadataValue(getPlugin().getPlugin(), "GadgetFB"));
                                removableEntities.add(falling);
                                if (!getPlugin().getEntityUtils().isValid(falling)) return;

                                double velX = MathUtils.random(0.8f, -0.8f);
                                double velY = MathUtils.random(1f, 0.5f);
                                double velZ = MathUtils.random(0.8f, -0.8f);
                                MathUtils.applyVelocity(falling, new Vector(velX, velY, velZ));
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (getPlugin().getEntityUtils().isValid(falling))
                                            falling.remove();
                                    }
                                }.runTaskLater(getPlugin().getPlugin(), 20);
                            }
                        }
                    }
                }
                break;
            }
            ParticleMaker maker = new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, color);
            maker.sendToLocation(l);
        }
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.CLAY_BRICK).withName("&eParticle Laser");
    }
}
