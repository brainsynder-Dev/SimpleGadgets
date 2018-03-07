package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import simple.brainsynder.math.MathUtils;
import simple.brainsynder.sound.SoundMaker;

import java.util.Random;
import java.util.UUID;

public class ChickenBlaster extends Gadget {
    public ChickenBlaster(GadgetPlugin plugin) {
        super(plugin, "chicken_blaster");
    }

    @Override
    public void run(User user) {
        Player p = user.getPlayer();

        final Chicken chicken = getPlugin().getEntityUtils().spawnMob(p.getEyeLocation(), Chicken.class);
        chicken.setNoDamageTicks(180);
        chicken.setVelocity(p.getLocation().getDirection().multiply(3));
        removableEntities.add(chicken);

        SoundMaker.ENTITY_CHICKEN_AMBIENT.playSound(p.getLocation());
        SoundMaker.ENTITY_GENERIC_EXPLODE.playSound(p.getLocation(), 0.3F, 1F);

        Random random = new Random();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isRemoved()) {
                    cancel();
                    return;
                }

                if (!getPlugin().getEntityUtils().isValid(chicken)) {
                    cancel();
                    return;
                }

                SoundMaker.ENTITY_EGG_THROW.playSound(chicken.getLocation());

                for (int i = 0; i < 10; ++i) {
                    int v = MathUtils.random(1, 2);
                    Material material = ((v == 1) ? Material.FEATHER : Material.COOKED_CHICKEN);
                    Vector vector = new Vector(random.nextDouble() - 0.5D, random.nextDouble() / 2, random.nextDouble() - 0.5D);
                    Item item = getPlugin().getEntityUtils().launchItem(chicken.getLocation(), vector, new ItemBuilder(material)
                            .withName(UUID.randomUUID().toString()).build());
                    removableItems.add(item);
                }

                chicken.remove();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        clearItems();
                    }
                }.runTaskLater(Core.get(), 45);
            }
        }.runTaskLater(Core.get(), 9);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.COOKED_CHICKEN).withName("&eChicken blaster");
    }
}
