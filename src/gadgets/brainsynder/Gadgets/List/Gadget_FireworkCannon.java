package gadgets.brainsynder.Gadgets.List;

import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class Gadget_FireworkCannon extends Gadget {
    public Gadget_FireworkCannon() {
        super(29, "firework_cannon", Material.FIREWORK);
    }

    @Override
    public void run(Player p) {
        List<Location> locations = RandomRef.getStraightLine(p, 10);
        int i = 1;
        for (Location l : locations) {
            if (l.getBlock() != null) {
                if (l.getBlock().getType().isSolid()) {
                    final Firework fw = (Firework) EntityRef.spawnMob(l, Firework.class);
                    FireworkMeta meta = fw.getFireworkMeta();
                    meta.addEffect(randomColor());
                    fw.setFireworkMeta(meta);
                    new BukkitRunnable() {
                        @Override public void run() {
                            fw.detonate();
                        }
                    }.runTaskLaterAsynchronously(getPlugin().getGadgetPlugin(), 2);
                    break;
                }
            }
            if ((i >= locations.size())) {
                final Firework fw = (Firework) EntityRef.spawnMob(l, Firework.class);
                FireworkMeta meta = fw.getFireworkMeta();
                meta.addEffect(randomColor());
                fw.setFireworkMeta(meta);
                new BukkitRunnable() {
                    @Override public void run() {
                        fw.detonate();
                    }
                }.runTaskLaterAsynchronously(getPlugin().getGadgetPlugin(), 2);
                break;
            }
            i++;
        }
    }

    private FireworkEffect randomColor() {
        Random r = new Random();

        int rt = r.nextInt(4) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        if (rt == 1) type = FireworkEffect.Type.BALL;
        if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
        if (rt == 3) type = FireworkEffect.Type.BURST;
        if (rt == 4) type = FireworkEffect.Type.CREEPER;
        if (rt == 5) type = FireworkEffect.Type.STAR;
        int l1 = r.nextInt(17) + 1;
        int l2 = r.nextInt(17) + 1;
        Color c1 = getColor(l1);
        Color c2 = getColor(l2);
        return FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
    }

    private Color getColor(int i) {
        Color c = null;
        if (i == 1) c = Color.AQUA;
        if (i == 2) c = Color.BLACK;
        if (i == 3) c = Color.BLUE;
        if (i == 4) c = Color.FUCHSIA;
        if (i == 5) c = Color.GRAY;
        if (i == 6) c = Color.GREEN;
        if (i == 7) c = Color.LIME;
        if (i == 8) c = Color.MAROON;
        if (i == 9) c = Color.NAVY;
        if (i == 10) c = Color.OLIVE;
        if (i == 11) c = Color.ORANGE;
        if (i == 12) c = Color.PURPLE;
        if (i == 13) c = Color.RED;
        if (i == 14) c = Color.SILVER;
        if (i == 15) c = Color.TEAL;
        if (i == 16) c = Color.WHITE;
        if (i == 17) c = Color.YELLOW;
        return c;
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
