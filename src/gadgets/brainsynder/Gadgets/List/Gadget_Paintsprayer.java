package gadgets.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.Gadgets.Gadget;
import gadgets.brainsynder.Utils.Cooldown;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import simple.brainsynder.nms.IActionMessage;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.Reflection;

import java.util.HashSet;
import java.util.Random;

public class Gadget_Paintsprayer extends Gadget {
    public Gadget_Paintsprayer() {
        super(12, "paint_sprayer", Material.GOLD_BARDING, (byte) 0);
    }

    @Override
    public void loadExtraTags() {
        super.loadExtraTags();
        Core.getLanguage().set(true, "Gadgets." + getIdName() + ".Target-Block-Distance", 50);
    }

    @Override
    public void run(final Player p) {
        int target_distance = Core.getLanguage().getInt("Gadgets." + getIdName() + ".Target-Block-Distance");
        Block block = p.getTargetBlock((HashSet<Byte>) null, target_distance);
        if (block.isLiquid()) {
            Cooldown.removeCooldown(p);
            IActionMessage message = Reflection.getActionMessage();
            message.sendMessage(p, Core.getLanguage().getString("Messages.Target-Block-is-liquid"));
            return;
        }

        if (block.getType() == Material.AIR) {
            Cooldown.removeCooldown(p);
            IActionMessage message = Reflection.getActionMessage();
            message.sendMessage(p, Core.getLanguage().getString("Messages.No-Block-Within-Distance").replace("%dist%", "" + target_distance));
            return;
        }
        SoundPlayer.playSound(SoundMaker.ENTITY_CHICKEN_EGG, p.getLocation(), 0.5F, 0.3F);

        Random rand = new Random();
        int i = rand.nextInt(2) + 1;
        int data = rand.nextInt(15) + 1;
        for (Block block2 : BlockUtils.getBlocksInRadius(block.getLocation(), 3, false)) {
            BlockUtils.setToRestore(Core.get(), block2, (i == 0) ? Material.WOOL : Material.STAINED_CLAY, (byte) data, 50);
            ParticleRef.sendParticle(block2.getLocation(), (i == 1) ? Material.WOOL : Material.STAINED_CLAY, (byte) data, 1.0F, 1.0F, 1.0F, 5);
            i = rand.nextInt(2) + 1;
            data = rand.nextInt(15) + 1;
        }
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
