package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.api.SkullMaker;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.StringAPI;

import java.util.Arrays;
import java.util.List;

public class BirthdayCannon extends Gadget {
    private List<String> textures = Arrays.asList(
            "http://textures.minecraft.net/texture/bd7a9f6ed08dd217fdf09f4652bf6b7af621e1d5f8963605349da73998a443",
            "http://textures.minecraft.net/texture/9715f537fe7af6f5aa6eb98ad6902c13d05fb36c16b311ed832b09b598828",
            "http://textures.minecraft.net/texture/7fcd1c82e2fb3fa368cfa9a506ab6c98647595d215d6471ad47cce29685af",
            "http://textures.minecraft.net/texture/928e692d86e224497915a39583dbe38edffd39cbba457cc95a7ac3ea25d445",
            "http://textures.minecraft.net/texture/f5612dc7b86d71afc1197301c15fd979e9f39e7b1f41d8f1ebdf8115576e2e",
            "http://textures.minecraft.net/texture/47e55fcc809a2ac1861da2a67f7f31bd7237887d162eca1eda526a7512a64910",
            "http://textures.minecraft.net/texture/512e9451cdb196b78195a8f0a4b9c1c0a04f5827887927b6a82aad39cab2f430",
            "http://textures.minecraft.net/texture/84e1c42f11383b9dc8e67f2846fa311b16320f2c2ec7e175538dbff1dd94bb7"
    );
    public BirthdayCannon(GadgetPlugin plugin) {
        super(plugin, "birthday_cannon");
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

                StringAPI texture = new StringAPI(textures.get(i));
                Item item = getPlugin().getEntityUtils().launchItem(player, new SkullMaker().setUrl(texture.toSkinURL()).setName("" + Math.random()).create());
                removableItems.add(item);
                SoundMaker.ENTITY_EXPERIENCE_ORB_PICKUP.playSound(player.getLocation());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!getPlugin().getEntityUtils().isValid(item)) return;
                        ParticleMaker maker = new ParticleMaker(ParticleMaker.Particle.SPELL_INSTANT, 10, 1, 1, 1);
                        maker.sendToLocation(item.getLocation());
                        SoundMaker.ENTITY_FIREWORK_SHOOT.playSound(player.getLocation());
                        item.remove();
                    }
                }.runTaskLaterAsynchronously(Core.get(), 15);
                i++;
            }
        }.runTaskTimer(Core.get(), 0, 10);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.CAKE, 1).withName("&eBirthday Cannon");
    }
}
