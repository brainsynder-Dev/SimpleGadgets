package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.BlockChanger;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.trailer.ITrailer;
import simple.brainsynder.trailer.Trailer;

import java.util.Random;

public class NatureWind extends BlockChanger {
    public NatureWind(GadgetPlugin plugin) {
        super(plugin, "nature_wind");
    }

    @Override
    public void run(User user) {
        Player player = user.getPlayer();

        Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemBuilder(Material.LEAVES)
                .withName(String.valueOf(Math.random())).build());
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setVelocity(player.getEyeLocation().getDirection());

        SoundMaker.ENTITY_ARROW_SHOOT.playSound(item.getLocation());
        ITrailer<Item> trailer = new Trailer<>();
        trailer.setTarget(item);
        trailer.setPlayer(player);

        IStorage<ParticleMaker> iStorage = new StorageList<>();
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.VILLAGER_HAPPY, 5, 0.2, 0.2, 0.2));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, Color.GREEN));
        trailer.setParticles(iStorage);

        trailer.start((JavaPlugin) getPlugin().getPlugin());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isRemoved()) {
                    cancel ();
                    item.remove();
                    return;
                }
                if (!getPlugin().getEntityUtils().isValid(item)) {
                    cancel();
                    return;
                }

                Random rand = new Random();
                SoundMaker.BLOCK_GRASS_BREAK.playSound(player.getLocation());
                for (Block block : getPlugin().getBlockUtils().getBlocksInRadius(item.getLocation(), 4, false)) {
                    if (!getPlugin().getBlockUtils().canChange(block)) continue;
                    int i = rand.nextInt (2) + 1;
                    if (storage.contains(block)) continue;
                    storage.addBlock(block);
                    Material material = (i == 1) ? Material.LEAVES : Material.LOG;
                    block.setType(material);
                    ParticleMaker maker = new ParticleMaker(ParticleMaker.Particle.BLOCK_DUST, 5, 1.0);
                    maker.setData(material);
                    maker.sendToLocation(block.getLocation());
                }
                item.remove();
                trailer.clear();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        storage.reset();
                    }
                }.runTaskLater(getPlugin().getPlugin(), 20 * 3);
            }
        }.runTaskLater(getPlugin().getPlugin(), 20);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.LEAVES)
                .withName("&eNature Wind");
    }
}
