package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;

import java.util.ArrayList;
import java.util.List;

public class ConfettiCannon extends Gadget {
    private List<ParticleMaker> particles = new ArrayList<>();

    public ConfettiCannon(GadgetPlugin plugin) {
        super(plugin, "confetti");

        for (DyeColor color : DyeColor.values()) {
            ParticleMaker maker = new ParticleMaker(ParticleMaker.Particle.ITEM_CRACK, 10, 0.3F, 0.3F, 0.3F);
            maker.setData(Material.INK_SACK, (short) color.ordinal());
            particles.add(maker);
        }
    }

    @Override
    public void run(User user) {
        Player player = user.getPlayer();

        Location localLocation1 = player.getEyeLocation();
        double x = localLocation1.getX();
        double y = localLocation1.getY();
        double z = localLocation1.getZ();
        double yaw = Math.toRadians(localLocation1.getYaw() + 90.0F);
        double pitch = Math.toRadians(localLocation1.getPitch() + 90.0F);
        double cos = Math.sin(pitch) * Math.cos(yaw);
        double sin = Math.sin(pitch) * Math.sin(yaw);
        double cosP = Math.cos(pitch);

        int delay = 1;
        for (double i = 0.9; i < 2.5; i += 0.1) {
            Location localLocation2 = new Location(player.getWorld(), x + i * cos, (y + i * cosP), z + i * sin);
            if (localLocation2.getBlock().getType().isSolid()) break;
            new BukkitRunnable() {
                @Override
                public void run() {
                    particles.forEach(maker -> maker.sendToLocation(localLocation2));
                }
            }.runTaskLater(getPlugin().getPlugin(), delay);
            delay++;
        }
        SoundMaker.ENTITY_EXPERIENCE_ORB_PICKUP.playSound(player.getLocation());
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.DIAMOND_BARDING, 1).withName("&eConfetti Cannon");
    }
}
