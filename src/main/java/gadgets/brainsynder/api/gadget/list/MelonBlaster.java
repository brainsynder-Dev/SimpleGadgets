package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;

public class MelonBlaster extends Gadget {
    public MelonBlaster(GadgetPlugin plugin) {
        super(plugin, "melon_blaster");
    }

    @Override
    public void run(User user) {
        Player p = user.getPlayer();
        Item it = getPlugin().getEntityUtils().launchItem(p, new ItemBuilder(Material.MELON_BLOCK)
                .withName(String.valueOf(Math.random())).build());
        SoundMaker.ENTITY_GENERIC_EXPLODE.playSound(p.getLocation());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (isRemoved()) {
                    it.remove();
                    cancel();
                    return;
                }
                if (!getPlugin().getEntityUtils().isValid(it)) {
                    cancel();
                    return;
                }
                ParticleMaker maker = new ParticleMaker(ParticleMaker.Particle.BLOCK_DUST, 15, 0.8);
                maker.setData(it.getItemStack());
                maker.sendToLocation(it.getLocation());
                SoundMaker.ENTITY_PLAYER_BURP.playSound(it.getLocation());
                for (int x1 = 0; x1 <= 6; x1++) {
                    double x = -0.5F + (float)(Math.random() * 0.9D);
                    double y = 0.5D;
                    double z = -0.5F + (float)(Math.random() * 0.9D);

                    ItemStack is = new ItemStack(Material.MELON, 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(String.valueOf (Math.random()));
                    is.setItemMeta(im);

                    Item fb = p.getWorld().dropItem(it.getLocation(), is);
                    fb.setVelocity(new Vector(x, y, z));
                    fb.setMetadata("eatable", new FixedMetadataValue(getPlugin().getPlugin(), "eatable"));
                    removableItems.add(fb);
                }
                it.remove();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        clearItems();
                    }
                }.runTaskLater(getPlugin().getPlugin(), 20*20);
            }
        }.runTaskLater(getPlugin().getPlugin(), 40L);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.MELON_BLOCK).withName("&eMelon Blaster");
    }
}
