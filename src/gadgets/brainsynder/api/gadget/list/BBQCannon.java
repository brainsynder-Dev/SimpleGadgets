package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;

public class BBQCannon extends Gadget {
    public BBQCannon(GadgetPlugin plugin) {
        super(plugin, "bbq_cannon");
    }

    @Override
    public void run(User user) {
        Player player = user.getPlayer();

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i >= 8) {
                    cancel();
                    return;
                }
                if (!getPlugin().getEntityUtils().isValid(player)) {
                    cancel();
                    return;
                }

                Item item = getPlugin().getEntityUtils().launchItem(player, new ItemMaker(Material.COOKED_BEEF).setName("" + Math.random()).create());
                item.setFireTicks(156151);
                SoundMaker.ENTITY_CHICKEN_EGG.playSound(player.getLocation());
                BukkitRunnable flameParticles = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (isRemoved()) {
                            cancel();
                            return;
                        }

                        if (!getPlugin().getEntityUtils().isValid(item)) {
                            cancel();
                            return;
                        }
                        ParticleMaker maker = new ParticleMaker(ParticleMaker.Particle.ITEM_CRACK, 2, 0.2);
                        maker.setData(Material.COOKED_BEEF);
                        maker.sendToLocation(item.getLocation());
                        new ParticleMaker (ParticleMaker.Particle.FLAME, 2,
                                0.0F, 0.2F, 0.0F).sendToLocation(item.getLocation());
                    }
                };
                flameParticles.runTaskTimer(getPlugin().getPlugin(), 0, 2);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        flameParticles.cancel();
                        if (!getPlugin().getEntityUtils().isValid(item)) {
                            cancel();
                            return;
                        }
                        ParticleMaker maker = new ParticleMaker(ParticleMaker.Particle.SPELL_INSTANT, 10, 1, 1, 1);
                        maker.sendToLocation(item.getLocation());
                        SoundMaker.ENTITY_FIREWORK_SHOOT.playSound(player.getLocation());
                        item.remove();
                    }
                }.runTaskLaterAsynchronously(getPlugin().getPlugin(), 15);
                i++;
            }
        }.runTaskTimer(getPlugin().getPlugin(), 0, 10);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return null;
    }
}
