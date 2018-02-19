package gadgets.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import simple.brainsynder.sound.SoundMaker;

import java.util.ArrayList;
import java.util.List;

public class Gadget_MelonBlaster extends Gadget {
    public Gadget_MelonBlaster() {
        super(8, "melon_blaster", Material.MELON_BLOCK);
    }

    private List<Item> items = new ArrayList<>();
    @Override
    public void run(final Player p) {
        final Item it = p.getWorld().dropItem(p.getEyeLocation(), new ItemStack(Material.MELON_BLOCK));
        it.setVelocity(p.getEyeLocation().getDirection());
        it.setMetadata("takeable", new FixedMetadataValue(Core.get(), "takeable"));
        SoundPlayer.playSound(SoundMaker.ENTITY_GENERIC_EXPLODE, p.getLocation());

        new BukkitRunnable() {
            @Override
            public void run() {
                ParticleRef.sendParticle(it.getLocation(), Material.MELON_BLOCK, (byte) 0, 0.8, 0.8, 0.8, 15);
                it.remove();
                SoundPlayer.playSound(SoundMaker.ENTITY_PLAYER_BURP, it.getLocation ());

                for (int x1 = 0; x1 <= 6; x1++) {
                    double x = -0.5F + (float)(Math.random() * 0.9D);
                    double y = 0.5D;
                    double z = -0.5F + (float)(Math.random() * 0.9D);

                    ItemStack is = new ItemStack(Material.MELON, 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(String.valueOf (Math.random()));
                    is.setItemMeta(im);

                    final Item fb = p.getWorld().dropItem(it.getLocation(), is);
                    fb.setVelocity(new Vector(x, y, z));
                    fb.setMetadata("eatable", new FixedMetadataValue(Core.get(), "eatable"));
                    items.add(fb);
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Item item : items) {
                            item.remove();
                        }
                    }
                }.runTaskLater(Core.get (), 20*20);
            }
        }.runTaskLater(Core.get(), 40L);
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
