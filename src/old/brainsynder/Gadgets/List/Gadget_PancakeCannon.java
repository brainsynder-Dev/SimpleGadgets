package old.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.gadget.Gadget;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.SkullMaker;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.StringAPI;

public class Gadget_PancakeCannon extends Gadget {
    public Gadget_PancakeCannon() {
        super(28, "pancake_cannon", Material.INK_SACK, (byte)8);
    }

    @Override
    public void run(final Player p) {
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i >= 8) {
                    cancel();
                    return;
                }
                if (!p.isValid()) {
                    cancel();
                    return;
                }
                if (!p.isOnline()) {
                    cancel();
                    return;
                }

                StringAPI texture = new StringAPI("http://textures.minecraft.net/texture/77dac695a1cac324cbc2a2871b4c77ccfca5481e7a7cd3c6f350bde4bb8827d4");
                final Item item = p.getWorld().dropItem(p.getEyeLocation(), new SkullMaker().setUrl(texture.toSkinURL()).setName("" + Math.random()).create());
                item.setVelocity(p.getEyeLocation().getDirection());
                item.setMetadata("takeable", new FixedMetadataValue(Core.get(), "takeable"));
                SoundPlayer.playSound(SoundMaker.UI_BUTTON_CLICK, p.getLocation());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        item.remove();
                    }
                }.runTaskLaterAsynchronously(Core.get(), 15);
                i++;
            }
        }.runTaskTimer(Core.get(), 0, 10);
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
