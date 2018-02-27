package old.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.gadget.Gadget;
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

import java.util.Random;

public class Gadget_NetherBlaze extends Gadget {
    public Gadget_NetherBlaze() {
        super(11, "nether_blaze", Material.NETHERRACK, (byte) 0);
    }

    @Override
    public void run(final Player p) {
        ItemStack iceItem = new ItemStack(Material.NETHERRACK, 1);
        ItemMeta itemMeta = iceItem.getItemMeta();
        itemMeta.setDisplayName(String.valueOf(Math.random()));
        iceItem.setItemMeta(itemMeta);
        final Item ice = p.getWorld().dropItem(p.getEyeLocation(), iceItem);
        ice.setPickupDelay(Integer.MAX_VALUE);
        ice.setVelocity(p.getEyeLocation().getDirection());
        SoundPlayer.playSound(SoundMaker.ENTITY_ENDERDRAGON_FLAP, ice.getLocation());
        final IAsyncTrailer<Entity> trailer = new AsyncTrailer<>();
        trailer.setTarget(ice);
        trailer.setPlayer(p);
        IStorage<ParticleMaker> iStorage = new StorageList<>();
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.LAVA, 5, 0.2, 0.2, 0.2));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.FLAME, 5, 0.2, 0.2, 0.2));
        trailer.setParticles(iStorage);
        trailer.start((JavaPlugin) getPlugin().getPlugin());

        BukkitRunnable netherMaker = new BukkitRunnable() {
            @Override
            public void run() {
                if (!ice.isValid()) {
                    cancel();
                    return;
                }
                Random rand = new Random();
                int i = rand.nextInt (2) + 1;
                SoundPlayer.playSound(SoundMaker.BLOCK_STONE_BREAK, p.getLocation());
                for (Block blocks : BlockUtils.getBlocksInRadius(ice.getLocation(), 4, false)) {
                    BlockUtils.setToRestore(Core.get(), blocks, (i == 1) ? Material.NETHERRACK : Material.NETHER_BRICK, (byte) 0, 20 * 3);
                    ParticleRef.sendParticle(blocks.getLocation(), (i == 1) ? Material.NETHERRACK : Material.NETHER_BRICK, (byte) 0, 1.0F, 1.0F, 1.0F, 5);
                    i = rand.nextInt (2) + 1;
                    ice.remove();
                    trailer.clear();
                }
            }
        };
        netherMaker.runTaskLater(getPlugin().getPlugin(), 20);
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
