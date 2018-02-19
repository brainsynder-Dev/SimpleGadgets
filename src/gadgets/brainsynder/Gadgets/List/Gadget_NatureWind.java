package gadgets.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.trailer.AsyncTrailer;
import simple.brainsynder.trailer.IAsyncTrailer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gadget_NatureWind extends Gadget {
    public Gadget_NatureWind() {
        super(15, "nature_wind", Material.LEAVES, (byte) 0);
    }

    private List<Player> isUsing = new ArrayList<>();
    @Override
    public void run(final Player p) {
        ItemStack iceItem = new ItemStack(Material.LEAVES, 1);
        ItemMeta itemMeta = iceItem.getItemMeta();
        itemMeta.setDisplayName(String.valueOf(Math.random()));
        iceItem.setItemMeta(itemMeta);
        final Item leaf = p.getWorld().dropItem(p.getEyeLocation(), iceItem);
        leaf.setPickupDelay(Integer.MAX_VALUE);
        leaf.setVelocity(p.getEyeLocation().getDirection());
        SoundPlayer.playSound(SoundMaker.ENTITY_ARROW_SHOOT, leaf.getLocation());
        final IAsyncTrailer<Entity> trailer = new AsyncTrailer<>();
        trailer.setTarget(leaf);
        trailer.setPlayer(p);
        IStorage<ParticleMaker> iStorage = new StorageList<>();
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.VILLAGER_HAPPY, 5, 0.2, 0.2, 0.2));
        trailer.setParticles(iStorage);
        trailer.start((JavaPlugin) getPlugin().getGadgetPlugin());

        BukkitRunnable natureMaker = new BukkitRunnable() {
            @Override
            public void run() {
                if (!leaf.isValid()) {
                    cancel();
                    return;
                }
                Random rand = new Random();
                int i = rand.nextInt (2) + 1;
                SoundPlayer.playSound(SoundMaker.BLOCK_GRASS_BREAK, p.getLocation());
                for (Block blocks : BlockUtils.getBlocksInRadius(leaf.getLocation(), 4, false)) {
                    BlockUtils.setToRestore(Core.get(), blocks, (i == 1) ? Material.LEAVES : Material.LOG, (byte) 0, 20 * 3);
                    ParticleRef.sendParticle(blocks.getLocation(), (i == 1) ? Material.EMERALD_BLOCK : Material.LOG, (byte) 0, 1.0F, 1.0F, 1.0F, 5);
                    i = rand.nextInt (2) + 1;
                    leaf.remove();
                    trailer.clear();
                }
            }
        };
        natureMaker.runTaskLater(getPlugin().getGadgetPlugin(), 20);
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
