package old.brainsynder.Gadgets.List;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.event.GadgetListener;
import gadgets.brainsynder.api.gadget.Gadget;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.trailer.AsyncTrailer;
import simple.brainsynder.trailer.IAsyncTrailer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gadget_Rocket extends Gadget implements GadgetListener {
    public Gadget_Rocket() {
        super(18, "rocket", Material.FIREWORK, (byte) 0);
    }

    @Override
    public void loadExtraTags() {
        super.loadExtraTags();
        Core.getLanguage().set(true, "Gadgets." + getIdName() + ".CooldownTime", 15);
    }

    private List<String> isUsing = new ArrayList<>();

    @Override
    public void run(final Player p) {
        final Firework firework = EntityRef.spawnMob(p.getLocation(), Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect effect = randomColor();
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);
        firework.setPassenger(p);
        isUsing.add(p.getName());
        final IAsyncTrailer<Entity> trailer = new AsyncTrailer<>();
        trailer.setTarget(firework);
        trailer.setPlayer(p);
        IStorage<ParticleMaker> iStorage = new StorageList<>();
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.LAVA, 5, 0.2, 0.2, 0.2));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.CLOUD, 5, 0.2, 0.2, 0.2));
        iStorage.add(new ParticleMaker(ParticleMaker.Particle.FLAME, 5, 0.2, 0.2, 0.2));
        trailer.setParticles(iStorage);
        trailer.start((JavaPlugin) getPlugin().getPlugin());
        checkGround(p);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.isOnline()) {
                    return;
                }
                if (!firework.isValid()) {
                    return;
                }
                firework.eject();
                ParticleRef.sendParticle(ParticleMaker.Particle.EXPLOSION_HUGE, firework.getLocation(), 1.0F, 1.0F, 1.0F, 5);
                firework.detonate();
            }
        }.runTaskLater(getPlugin().getPlugin(), 20 * 4);
    }

    private void checkGround(final Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.isOnline()) {
                    if (!isUsing.contains(p.getName ())) {
                        return;
                    }
                    isUsing.remove(p.getName());
                    return;
                }
                if (!isUsing.contains(p.getName())) {
                    return;
                }
                if (BlockUtils.isOnGround(p)) {
                    isUsing.remove(p.getName());
                } else {
                    checkGround(p);
                }
            }
        }.runTaskLater(getPlugin().getPlugin(), 20);
    }

    private FireworkEffect randomColor() {
        Random r = new Random();

        int rt = r.nextInt(4) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        if (rt == 1) type = FireworkEffect.Type.BALL;
        if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
        if (rt == 3) type = FireworkEffect.Type.BURST;
        if (rt == 4) type = FireworkEffect.Type.CREEPER;
        if (rt == 5) type = FireworkEffect.Type.STAR;
        int l1 = r.nextInt(17) + 1;
        int l2 = r.nextInt(17) + 1;
        Color c1 = getColor(l1);
        Color c2 = getColor(l2);
        return FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
    }

    private Color getColor(int i) {
        Color c = null;
        if (i == 1) c = Color.AQUA;
        if (i == 2) c = Color.BLACK;
        if (i == 3) c = Color.BLUE;
        if (i == 4) c = Color.FUCHSIA;
        if (i == 5) c = Color.GRAY;
        if (i == 6) c = Color.GREEN;
        if (i == 7) c = Color.LIME;
        if (i == 8) c = Color.MAROON;
        if (i == 9) c = Color.NAVY;
        if (i == 10) c = Color.OLIVE;
        if (i == 11) c = Color.ORANGE;
        if (i == 12) c = Color.PURPLE;
        if (i == 13) c = Color.RED;
        if (i == 14) c = Color.SILVER;
        if (i == 15) c = Color.TEAL;
        if (i == 16) c = Color.WHITE;
        if (i == 17) c = Color.YELLOW;
        return c;
    }

    @EventHandler
    private void onFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (isUsing.contains(player.getName())) {
                if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    e.setCancelled(true);
                    isUsing.remove(player.getName());
                }
            }
        }
    }

    @Override
    public void onUserMove(Player player) {

    }

    @Override
    public void onProjectileHit(Projectile projectile) {

    }
}
