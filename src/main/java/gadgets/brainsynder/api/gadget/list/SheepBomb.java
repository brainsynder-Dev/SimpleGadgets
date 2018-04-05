package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.math.MathUtils;
import simple.brainsynder.nms.IClearGoals;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.Reflection;

public class SheepBomb extends Gadget {
    private double time = 5.0;
    public SheepBomb(GadgetPlugin plugin) {
        super(plugin, "sheep_bomb");
    }

    @Override
    public void loadExtraTags() {
        setDefault("cooldown", 20);
    }

    @Override
    public void run(User user) {
        Player p = user.getPlayer();
        Location loc = p.getLocation();
        loc.setY((double)(p.getLocation().getBlockY() + 1));
        Sheep sheep = getPlugin().getEntityUtils().spawnMob(loc, Sheep.class);
        sheep.setNoDamageTicks(100000);
        sheep.setMetadata("NOSHEAR4U", new FixedMetadataValue(getPlugin().getPlugin(), "NOSHEAR4U"));
        sheep.setVelocity(p.getEyeLocation().getDirection().multiply(0.7D));
        IClearGoals clearGoals = Reflection.getClearGoals();
        if (clearGoals != null) clearGoals.clearGoals(sheep);
        removableEntities.add (sheep);

        new BukkitRunnable() {
            boolean red = true;
            @Override
            public void run() {
                if (!getPlugin().getEntityUtils().isValid(sheep)) {
                    cancel ();
                    return;
                }

                if(time < 0.5) {
                    new ParticleMaker(ParticleMaker.Particle.EXPLOSION_HUGE, 2, 0.5).sendToLocation(sheep.getLocation());
                    SoundMaker.ENTITY_GENERIC_EXPLODE.playSound(sheep.getLocation());
                    for (int i = 0; i < 64; i++) {
                        double x = -0.5F + (float)(Math.random() * 0.9D);
                        double y = 0.5D;
                        double z = -0.5F + (float)(Math.random() * 0.9D);
                        final Item item = p.getWorld().dropItem(sheep.getLocation(), new ItemBuilder(Material.WOOL)
                                .withName(String.valueOf (Math.random())).withData(MathUtils.random(0,15)).build());
                        item.setPickupDelay(Integer.MAX_VALUE);
                        item.setVelocity((new Vector(x, y, z)));
                        removableItems.add(item);
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            clearItems();
                        }
                    }.runTaskLater(getPlugin ().getPlugin(), 20 * 3);
                    sheep.remove();
                    time = 5.0;
                    cancel();
                    return;
                }

                if(red) {
                    sheep.setColor(DyeColor.RED);
                    new ParticleMaker(ParticleMaker.Particle.REDSTONE, 10, 0.5)
                            .sendToLocation(sheep.getLocation().add(0, 0.4, 0));
                } else {
                    sheep.setColor(DyeColor.WHITE);
                    new ParticleMaker(ParticleMaker.Particle.SPELL_INSTANT, 10, 0.5)
                            .sendToLocation(sheep.getLocation().add(0, 0.4, 0));
                }
                SoundMaker.BLOCK_METAL_PRESSUREPLATE_CLICK_ON.playSound(sheep.getLocation());

                red = !red;
                time -= 0.2;
            }
        }.runTaskTimer(getPlugin().getPlugin(), 0, (long) time);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.WOOL).withName("&eSheep Bomb");
    }
}
