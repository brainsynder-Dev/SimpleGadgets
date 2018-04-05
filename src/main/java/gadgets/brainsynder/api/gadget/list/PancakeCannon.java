package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.sound.SoundMaker;

import java.util.Arrays;
import java.util.List;

public class PancakeCannon extends Gadget {
    private List<String> textures = Arrays.asList(
            "http://textures.minecraft.net/texture/bbaa7f8e437112b3b6569471596d5b61daa7a3b408391d1ed432f5317796a2",
            "http://textures.minecraft.net/texture/e44ca99e308a186b30281b2017c44189acafb591152f81feea96fecbe57",
            "http://textures.minecraft.net/texture/da7215805fbadbe18d46cc1a61ef21625cafa2b29d8be7ec21545ad48c088",
            "http://textures.minecraft.net/texture/9119fca4f28a755d37fbe5dcf6d8c3ef50fe394c1a7850bc7e2b71ee78303c4c",
            "http://textures.minecraft.net/texture/bbaa7f8e437112b3b6569471596d5b61daa7a3b408391d1ed432f5317796a2",
            "http://textures.minecraft.net/texture/e44ca99e308a186b30281b2017c44189acafb591152f81feea96fecbe57",
            "http://textures.minecraft.net/texture/da7215805fbadbe18d46cc1a61ef21625cafa2b29d8be7ec21545ad48c088",
            "http://textures.minecraft.net/texture/9119fca4f28a755d37fbe5dcf6d8c3ef50fe394c1a7850bc7e2b71ee78303c4c"
    );
    public PancakeCannon(GadgetPlugin plugin) {
        super(plugin, "pancake_cannon");
    }

    @Override
    public void run(User user) {
        Player player = user.getPlayer();
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i >= textures.size()) {
                    cancel();
                    return;
                }
                if (!getPlugin().getEntityUtils().isValid(player)) {
                    cancel();
                    return;
                }
                Item item = getPlugin().getEntityUtils().launchItem(player, new ItemBuilder(Material.SKULL_ITEM).withData(3).setTexture(textures.get(i)).withName("" + Math.random()).build());
                removableItems.add(item);
                SoundMaker.UI_BUTTON_CLICK.playSound(player.getLocation());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!getPlugin().getEntityUtils().isValid(item)) {
                            cancel();
                            return;
                        }
                        item.remove();
                    }
                }.runTaskLaterAsynchronously(getPlugin().getPlugin(), 15);
                i++;
            }
        }.runTaskTimer(getPlugin().getPlugin(), 0, 10);

    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SKULL_ITEM).withData(3)
                .setTexture("http://textures.minecraft.net/texture/6183b1739356d756c75665362f6b63c28ad5764c34bfa5b15468599d7fd247")
                .withName("&ePancake Cannon");
    }
}
