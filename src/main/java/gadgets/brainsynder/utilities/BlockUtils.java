package gadgets.brainsynder.utilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class BlockUtils {
    public Map<Location, String> blocksToRestore = new HashMap<>();
    private List<Material> blockedblocks = new ArrayList<>();

    public List<Block> getBlocksInRadius(Location location, int radius, boolean hollow) {
        List<Block> blocks = new ArrayList<>();

        int bX = location.getBlockX();
        int bY = location.getBlockY();
        int bZ = location.getBlockZ();
        for (int x = bX - radius; x <= bX + radius; x++) {
            for (int y = bY - radius; y <= bY + radius; y++) {
                for (int z = bZ - radius; z <= bZ + radius; z++) {
                    double distance = (bX - x) * (bX - x) + (bY - y) * (bY - y) + (bZ - z) * (bZ - z);
                    if ((distance < radius * radius) && ((!hollow) || (distance >= (radius - 1) * (radius - 1)))) {
                        Location l = new Location(location.getWorld(), x, y, z);
                        if (l.getBlock().getType() != org.bukkit.Material.BARRIER) {
                            blocks.add(l.getBlock());
                        }
                    }
                }
            }
        }
        return blocks;
    }

    public ArrayList<Block> getSurrounding(Block block, boolean diagonals) {
        ArrayList<Block> blocks = new ArrayList<>();
        if (diagonals) {
            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y <= 1; ++y) {
                    for (int z = -1; z <= 1; ++z) {
                        if (x != 0 || y != 0 || z != 0) {
                            blocks.add(block.getRelative(x, y, z));
                        }
                    }
                }
            }
        } else {
            blocks.add(block.getRelative(BlockFace.UP));
            blocks.add(block.getRelative(BlockFace.DOWN));
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }

        return blocks;
    }

    public List<Location> circle(Location loc, Double r, Double h, Boolean hollow, Boolean sphere, int plus_y) {
        ArrayList circleblocks = new ArrayList();
        double cx = loc.getX();
        double cy = loc.getY();
        double cz = loc.getZ();

        for (double x = cx - r.doubleValue(); x <= cx + r.doubleValue(); ++x) {
            for (double z = cz - r.doubleValue(); z <= cz + r.doubleValue(); ++z) {
                for (double y = sphere.booleanValue() ? cy - r.doubleValue() : cy; y < (sphere.booleanValue() ? cy + r.doubleValue() : cy + h.doubleValue()); ++y) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere.booleanValue() ? (cy - y) * (cy - y) : 0.0D);
                    if (dist < r.doubleValue() * r.doubleValue() && (!hollow.booleanValue() || dist >= (r.doubleValue() - 1.0D) * (r.doubleValue() - 1.0D))) {
                        Location l = new Location(loc.getWorld(), x, y + (double) plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }

        return circleblocks;
    }

    public boolean isOnGround(Entity entity) {
        Block block = entity.getLocation().getBlock().getRelative(BlockFace.DOWN);
        return block.getType().isSolid() || block.isLiquid();
    }

    public double getDistance(int x1, int z1, int x2, int z2) {
        int dx = x1 - x2;
        int dz = z1 - z2;
        return Math.sqrt((double) (dx * dx + dz * dz));
    }

    //TODO: Update this code
    public void forceRestore() {
        Iterator i$ = blocksToRestore.keySet().iterator();

        while (i$.hasNext()) {
            Location loc = (Location) i$.next();
            Block b = loc.getBlock();
            String s = blocksToRestore.get(loc);
            Material m = Material.valueOf(s.split(",")[0]);
            byte d = Byte.valueOf(s.split(",")[1]).byteValue();
            b.setType(m);
            b.setData(d);
        }
    }

    //TODO: Update this code
    public void restoreBlockAt(Plugin plugin, Location loc) {
        if (blocksToRestore.containsKey(loc)) {
            Block b = loc.getBlock();
            String s = blocksToRestore.get(loc);
            Material m = Material.valueOf(s.split(",")[0]);
            byte d = Byte.valueOf(s.split(",")[1]).byteValue();
            b.setType(m);
            b.setData(d);
            b.removeMetadata("NoBlockBreak", plugin);
            blocksToRestore.remove(loc);
        }
    }

    //TODO: Update this code
    public void setToRestoreIgnoring(final Plugin plugin, final Block b, Material newType, byte newData, int tickDelay) {
        if (!blocksToRestore.containsKey(b.getLocation())) {
            if (!blocksToRestore.containsKey(b.getLocation())) {
                blocksToRestore.put(b.getLocation(), b.getType().toString() + "," + b.getData());
                b.setType(newType);
                b.setData(newData);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        restoreBlockAt(plugin, b.getLocation());
                    }
                }.runTaskLater(plugin, tickDelay);
            }

        }
    }

    private boolean isBlockNear (Block block, Material target) {
        if (block.getRelative(BlockFace.DOWN).getType() == target) return true;
        if (block.getRelative(BlockFace.EAST).getType() == target) return true;
        if (block.getRelative(BlockFace.NORTH).getType() == target) return true;
        if (block.getRelative(BlockFace.SOUTH).getType() == target) return true;
        if (block.getRelative(BlockFace.UP).getType() == target) return true;
        return block.getRelative(BlockFace.WEST).getType() == target;
    }

    public boolean isNotBreakableOnChange(Block b) {
        if ((b.getType() == Material.YELLOW_FLOWER
                || b.getType() == Material.RED_ROSE
                || b.getType() == Material.DOUBLE_PLANT
                || b.getType() == Material.DEAD_BUSH
                || b.getType() == Material.LONG_GRASS
                || b.getType() == Material.SAPLING
                || b.getType() == Material.WATER_LILY
                || b.getType().name().contains("CHORUS")
                || b.getType() == Material.CACTUS)
                && (b.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.GRASS
                || b.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.DIRT
                || b.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.SAND
                || b.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.ENDER_STONE)) {
            return false;
        } else if ((b.getType() == Material.GRASS || b.getType() == Material.DIRT || b.getType() == Material.ENDER_STONE || b.getType() == Material.SAND)
                && (b.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.YELLOW_FLOWER
                || b.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.RED_ROSE
                || b.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.DOUBLE_PLANT
                || b.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.DEAD_BUSH
                || b.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.LONG_GRASS
                || b.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.SAPLING
                || b.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.WATER_LILY
                || b.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getType().name().contains("CHORUS")
                || b.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.CACTUS)) {
            return false;
        } else if (!b.isLiquid()) {
            if (b.getRelative(BlockFace.UP).getType() == Material.CACTUS)
                return false;
            if (b.getRelative(BlockFace.UP).getType() == Material.WOOD_PLATE)
                return false;
            if (b.getRelative(BlockFace.UP).getType() == Material.STONE_PLATE)
                return false;
            if (b.getRelative(BlockFace.UP).getType() == Material.IRON_PLATE)
                return false;
            if (b.getRelative(BlockFace.UP).getType() == Material.GOLD_PLATE)
                return false;
            if (b.getRelative(BlockFace.UP).getType() == Material.FLOWER_POT)
                return false;
            if (b.getRelative(BlockFace.UP).getType() == Material.FLOWER_POT)
                return false;

            for (BlockFace faces : BlockFace.values()) {
                if (faces != BlockFace.EAST_NORTH_EAST
                        || faces != BlockFace.EAST_SOUTH_EAST
                        || faces != BlockFace.NORTH_EAST
                        || faces != BlockFace.NORTH_NORTH_EAST
                        || faces != BlockFace.NORTH_NORTH_WEST
                        || faces != BlockFace.NORTH_WEST
                        || faces != BlockFace.SOUTH_EAST
                        || faces != BlockFace.SOUTH_SOUTH_EAST
                        || faces != BlockFace.SOUTH_SOUTH_WEST
                        || faces != BlockFace.WEST_NORTH_WEST
                        || faces != BlockFace.SOUTH_WEST
                        || faces != BlockFace.WEST_SOUTH_WEST
                        || faces != BlockFace.SELF) {
                    if (b.getRelative(faces).getType().equals(Material.TORCH))
                        return false;
                    if (b.getRelative(faces).getType().equals(Material.TRIPWIRE_HOOK))
                        return false;
                    if (b.getRelative(faces).getType().equals(Material.LADDER))
                        return false;
                }
                if (b.getRelative(faces).getType().toString().toLowerCase().contains("button")
                        || b.getRelative(faces).getType().toString().toLowerCase().contains("lever")
                        || b.getRelative(faces).getType().toString().toLowerCase().contains("door")
                        || b.getRelative(faces).getType().toString().toLowerCase().contains("farm")
                        || b.getRelative(faces).getType().toString().toLowerCase().contains("crop")
                        || b.getRelative(faces).getType().toString().toLowerCase().contains("beat")
                        || b.getRelative(faces).getType().toString().toLowerCase().contains("fruit")
                        || b.getRelative(faces).getType().toString().toLowerCase().contains("root")
                        || b.getRelative(faces).getType().toString().toLowerCase().contains("seeds")
                        || b.getRelative(faces).getType().toString().toLowerCase().contains("sugar")
                        || b.getRelative(faces).getType().toString().toLowerCase().contains("stem")) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canChange(Block b) {
        if (!blocksToRestore.containsKey(b.getLocation())) {
            if (blockedblocks.isEmpty()) {
                blockedblocks.add(Material.CHEST);
                blockedblocks.add(Material.AIR);
                blockedblocks.add(Material.WALL_SIGN);
                blockedblocks.add(Material.SIGN_POST);
                blockedblocks.add(Material.PORTAL);
                blockedblocks.add(Material.SKULL);
                blockedblocks.add(Material.SKULL_ITEM);
                blockedblocks.add(Material.TORCH);
                blockedblocks.add(Material.REDSTONE);
                blockedblocks.add(Material.WATER);
                blockedblocks.add(Material.STATIONARY_WATER);
                blockedblocks.add(Material.LAVA);
                blockedblocks.add(Material.STATIONARY_LAVA);
                blockedblocks.add(Material.REDSTONE_WIRE);
                blockedblocks.add(Material.REDSTONE_COMPARATOR);
                blockedblocks.add(Material.BEDROCK);
                blockedblocks.add(Material.BANNER);
                blockedblocks.add(Material.FLOWER_POT);
                blockedblocks.add(Material.REDSTONE_COMPARATOR_OFF);
                blockedblocks.add(Material.REDSTONE_COMPARATOR_ON);
                blockedblocks.add(Material.REDSTONE_TORCH_ON);
                blockedblocks.add(Material.REDSTONE_TORCH_OFF);
                blockedblocks.add(Material.CACTUS);
                //blockedblocks.add(Material.MOB_SPAWNER);
                blockedblocks.add(Material.SUGAR_CANE_BLOCK);
                blockedblocks.add(Material.PISTON_BASE);
                blockedblocks.add(Material.PISTON_EXTENSION);
                blockedblocks.add(Material.PISTON_MOVING_PIECE);
                blockedblocks.add(Material.PISTON_STICKY_BASE);
                blockedblocks.add(Material.STRING);
                blockedblocks.add(Material.TRIPWIRE_HOOK);
                blockedblocks.add(Material.COMMAND_MINECART);
                blockedblocks.add(Material.EXPLOSIVE_MINECART);
                blockedblocks.add(Material.TRAP_DOOR);
                blockedblocks.add(Material.WOODEN_DOOR);
                blockedblocks.add(Material.ACACIA_DOOR);
                blockedblocks.add(Material.BIRCH_DOOR);
                blockedblocks.add(Material.IRON_DOOR);
                blockedblocks.add(Material.DARK_OAK_DOOR);
                blockedblocks.add(Material.IRON_TRAPDOOR);
                blockedblocks.add(Material.SPRUCE_DOOR);
                blockedblocks.add(Material.JUNGLE_DOOR);
                blockedblocks.add(Material.WALL_BANNER);
                blockedblocks.add(Material.BED_BLOCK);
                blockedblocks.add(Material.BED);
                blockedblocks.add(Material.JUKEBOX);
                blockedblocks.add(Material.ENDER_PORTAL_FRAME);
                blockedblocks.add(Material.DOUBLE_PLANT);
                blockedblocks.add(Material.ENDER_PORTAL);
                blockedblocks.add(Material.HOPPER_MINECART);
                blockedblocks.add(Material.POWERED_MINECART);
                blockedblocks.add(Material.STORAGE_MINECART);
                blockedblocks.add(Material.RAILS);
                blockedblocks.add(Material.POWERED_RAIL);
                blockedblocks.add(Material.ACTIVATOR_RAIL);
                blockedblocks.add(Material.DETECTOR_RAIL);
                blockedblocks.add(Material.COMMAND);
                blockedblocks.add(Material.VINE);
                blockedblocks.add(Material.WOODEN_DOOR);
                blockedblocks.add(Material.FURNACE);
                blockedblocks.add(Material.BURNING_FURNACE);
                blockedblocks.add(Material.WORKBENCH);
                blockedblocks.add(Material.ANVIL);
                blockedblocks.add(Material.BEACON);
                blockedblocks.add(Material.ENCHANTMENT_TABLE);
                blockedblocks.add(Material.ENDER_CHEST);
                blockedblocks.add(Material.HOPPER);
                blockedblocks.add(Material.DROPPER);
                blockedblocks.add(Material.DISPENSER);
                blockedblocks.add(Material.DIODE);
                blockedblocks.add(Material.ITEM_FRAME);
                blockedblocks.add(Material.PAINTING);
                blockedblocks.add(Material.DIODE_BLOCK_OFF);
                blockedblocks.add(Material.DIODE_BLOCK_ON);
                blockedblocks.add(Material.WOODEN_DOOR);
                blockedblocks.add(Material.IRON_DOOR);
                blockedblocks.add(Material.POTATO);
                blockedblocks.add(Material.CARROT);
                blockedblocks.add(Material.WHEAT);
                blockedblocks.add(Material.POTATO);
                blockedblocks.add(Material.CROPS);
                blockedblocks.add(Material.ARMOR_STAND);
                blockedblocks.add(Material.TRAPPED_CHEST);
                blockedblocks.add(Material.WOOD_PLATE);
                blockedblocks.add(Material.STONE_PLATE);
                blockedblocks.add(Material.IRON_PLATE);
                blockedblocks.add(Material.GOLD_PLATE);
                blockedblocks.add(Material.OBSERVER);
                blockedblocks.add(Material.REDSTONE_BLOCK);
            }


            return !blockedblocks.contains(b.getType())
                    && !b.getType().toString().toLowerCase().contains("door")
                    && b.getType() != Material.BED
                    && !b.getType().toString().toLowerCase().contains("button")
                    && !b.getType().toString().toLowerCase().contains("lever")
                    && !b.getType().toString().toLowerCase().contains("pressure_plate")
                    && !b.getType().toString().toLowerCase().contains("farm")
                    && !b.getType().toString().toLowerCase().contains("banner")
                    && !b.getType().toString().toLowerCase().contains("piston")
                    && !isBlockNear(b, Material.OBSERVER)
                    && !b.hasMetadata("NoBlockBreak")
                    && b.getType().isSolid()
                    && isNotBreakableOnChange(b)
                    && !blocksToRestore.containsKey(b.getLocation());
        }
        return false;
    }

    //TODO: Update this code
    public void setToRestore(final Plugin plugin, final Block b, Material newType, byte newData, int tickDelay) {
        if (canChange(b)) {
            blocksToRestore.put(b.getLocation(), b.getType().toString() + "," + b.getData());
            b.setType(newType);
            b.setData(newData);
            b.setMetadata("NoBlockBreak", new FixedMetadataValue(plugin, "NoBlockBreak"));
            new BukkitRunnable() {
                @Override
                public void run() {
                    restoreBlockAt(plugin, b.getLocation());
                }
            }.runTaskLater(plugin, tickDelay);
        }
    }
}
