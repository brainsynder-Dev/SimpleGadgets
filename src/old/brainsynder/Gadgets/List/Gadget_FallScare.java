package old.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.gadget.Gadget;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
import simple.brainsynder.trailer.ITrailer;
import simple.brainsynder.trailer.Trailer;

import java.util.Random;

public class Gadget_FallScare extends Gadget {
    public Gadget_FallScare() {
        super(17, "fall_scare", Material.PUMPKIN, (byte) 0);
    }

    @Override
    public void run(final Player p) {
        ItemStack itemStack = new ItemStack(Material.PUMPKIN, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(String.valueOf(Math.random()));
        itemStack.setItemMeta(itemMeta);
        final Item item = p.getWorld().dropItem(p.getEyeLocation(), itemStack);
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setVelocity(p.getEyeLocation().getDirection());
        SoundPlayer.playSound(SoundMaker.ENTITY_ENDERDRAGON_GROWL, item.getLocation());
        final ITrailer<Item> trailer = new Trailer<>();
        trailer.setTarget(item);
        trailer.setPlayer(p);
        IStorage<ParticleMaker> iStorage = new StorageList<>();
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, Color.fromRGB(255, 132, 0)));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.REDSTONE, 5, Color.fromRGB(0, 0, 0)));
        trailer.setParticles(iStorage);
        trailer.start((JavaPlugin) getPlugin().getPlugin());
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!item.isValid()) {
                    cancel();
                    return;
                }
                Random rand = new Random();
                int i = rand.nextInt (2) + 1;
                SoundPlayer.playSound(SoundMaker.BLOCK_GRASS_BREAK, p.getLocation());
                for (Block blocks : BlockUtils.getBlocksInRadius(item.getLocation(), 4, false)) {
                    BlockUtils.setToRestore(Core.get(), blocks, (i == 1) ? Material.COAL_BLOCK
                            : Material.JACK_O_LANTERN, (byte) 0, 20 * 3);
                    ParticleRef.sendParticle(blocks.getLocation(), (i == 1) ? Material.COAL_BLOCK
                            : Material.JACK_O_LANTERN, (byte) 0, 1.0F, 1.0F, 1.0F, 5);
                    i = rand.nextInt (2) + 1;
                    item.remove();
                    trailer.clear();
                }
            }
        };
        runnable.runTaskLater(getPlugin().getPlugin(), 20);
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
