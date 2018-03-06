package gadgets.brainsynder.api.gadget.list;

import gadgets.brainsynder.api.GadgetPlugin;
import gadgets.brainsynder.api.gadget.Gadget;
import gadgets.brainsynder.api.user.User;
import gadgets.brainsynder.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.plugin.java.JavaPlugin;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.trailer.AsyncTrailer;
import simple.brainsynder.trailer.IAsyncTrailer;

public class TrailBlazer extends Gadget {
    public TrailBlazer(GadgetPlugin plugin) {
        super(plugin, "trail_blazer");
    }

    @Override
    public void run(User user) {
        Player p = user.getPlayer();
        Snowball proj = getPlugin().getEntityUtils().launchProjectile(p, Snowball.class);
        proj.setShooter(p);

        IAsyncTrailer<Entity> trailer = new AsyncTrailer<>();
        trailer.setTarget(proj);
        trailer.setPlayer(p);
        IStorage<ParticleMaker> iStorage = new StorageList<>();
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.LAVA, 10, 0.5, 0.5, 0.5));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.HEART, 10, 0.5, 0.5, 0.5));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.SPELL_WITCH, 10, 0.5, 0.5, 0.5));
        trailer.setParticles(iStorage);
        trailer.start((JavaPlugin) getPlugin().getPlugin());
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SNOW_BALL).withName("&eTrail Blazer");
    }
}
