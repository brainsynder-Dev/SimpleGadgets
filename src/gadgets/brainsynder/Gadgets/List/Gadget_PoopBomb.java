package gadgets.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

public class Gadget_PoopBomb extends Gadget {
    public Gadget_PoopBomb() {
        super(20, "poop_bomb", Material.WOOL, (byte) 12);
    }

    @Override
    public void loadExtraTags() {
        Core.getLanguage().set(true, "Gadgets." + getIdName() + ".CooldownTime", 15);
    }

    @Override
    public void run(final Player player) {
        final Item it = ItemUtils.launchItem(player, new ItemStack(Material.WOOL, 1, (byte) 12));
        final List<Item> items = new ArrayList<>();
        SoundPlayer.playSound(SoundMaker.ENTITY_PLAYER_BURP, player.getLocation (), 1.0F, 0.5F);
        new BukkitRunnable() {
            @Override
            public void run() {
                it.remove();
                SoundPlayer.playSound(SoundMaker.ENTITY_GENERIC_EXPLODE, player.getLocation(), 0.2F, 0.2F);
                for (Item item : ItemUtils.spawnItems(it.getLocation(), new ItemStack(Material.INK_SACK, 1, (byte) 3), 6)) {
                    final IAsyncTrailer<Entity> trailer = new AsyncTrailer<>();
                    trailer.setTarget(item);
                    trailer.setPlayer(player);
                    IStorage<ParticleMaker> iStorage = new StorageList<>();
                    iStorage.add(new ParticleMaker(ParticleMaker.Particle.CLOUD, 5, 0.2, 0.2, 0.2));
                    trailer.setParticles(iStorage);
                    trailer.start((JavaPlugin) getPlugin().getGadgetPlugin());
                    items.add(item);
                }
            }
        }.runTaskLater(Core.get(), 40L);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Item item : items) {
                    item.remove();
                }
            }
        }.runTaskLater(Core.get(), 20*8);
    }
}
