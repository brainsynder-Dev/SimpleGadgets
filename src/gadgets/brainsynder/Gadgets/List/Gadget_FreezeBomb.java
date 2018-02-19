package gadgets.brainsynder.Gadgets.List;

import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.Material;
import org.bukkit.entity.*;
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

public class Gadget_FreezeBomb extends Gadget {
    public Gadget_FreezeBomb() {
        super(24, "freeze_bomb", Material.PACKED_ICE, (byte) 0);
    }

    @Override
    public void run(final Player p) {
        ItemStack iceItem = new ItemStack(Material.ICE, 1);
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
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.SNOWBALL, 5, 0.2, 0.2, 0.2));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.SNOW_SHOVEL, 5, 0.2, 0.2, 0.2));
        trailer.setParticles(iStorage);
        trailer.start((JavaPlugin) getPlugin().getGadgetPlugin());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!ice.isValid()) {
                    cancel();
                    return;
                }
                trailer.clear();
                Random rand = new Random();
                int i = rand.nextInt(2) + 1;
                SoundPlayer.playSound(SoundMaker.BLOCK_STONE_BREAK, ice.getLocation());
                for (Entity en : RandomRef.getNearbyEntities(ice.getLocation(), 7)) {
                    if (en instanceof LivingEntity) {
                        ParticleMaker maker = new ParticleMaker(ParticleMaker.Particle.BLOCK_DUST, 20, 0.5, 0.5, 0.5);
                        maker.setData(((i == 1) ? Material.ICE : Material.PACKED_ICE));
                        maker.sendToLocation(((LivingEntity)en).getEyeLocation());
                        i = rand.nextInt(2) + 1;
                    }
                }
                ice.remove();
            }
        }.runTaskLater(getPlugin().getGadgetPlugin(), 20);
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
