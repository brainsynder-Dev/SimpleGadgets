/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package gadgets.brainsynder.Gadgets;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.Events.GadgetListener;
import gadgets.brainsynder.Events.Gadgets.GadgetActivateEvent;
import gadgets.brainsynder.Events.Gadgets.GadgetProjectileHitEvent;
import gadgets.brainsynder.Events.Gadgets.GadgetRegisterEvent;
import gadgets.brainsynder.Events.Gadgets.GadgetSelectEvent;
import gadgets.brainsynder.GadgetPlugin;
import gadgets.brainsynder.Gadgets.Errors.GadgetRegisterException;
import gadgets.brainsynder.Gadgets.List.*;
import gadgets.brainsynder.Utils.Cooldown;
import gadgets.brainsynder.Utils.MathUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.nms.ITitleMessage;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.AdvMap;
import simple.brainsynder.utils.Reflection;

import java.util.*;

public abstract class Gadget {
    public static final Gadget FUN_CANNON = new Gadget_FunCannon();
    public static final Gadget BAT_BLASTER = new Gadget_BatBlaster();
    public static final Gadget TRAIL_BLAZER = new Gadget_TrailBlazer();
    public static final Gadget CONFETTI = new Gadget_Confetti();
    public static final Gadget EXPLOSIVE_SNOWBALL = new Gadget_ExplosiveSnowball();
    public static final Gadget WATER_BOMB = new Gadget_WaterBomb();
    public static final Gadget FIREWORKS = new Gadget_Firework();
    public static final Gadget MELON_BLASTER = new Gadget_MelonBlaster();
    public static final Gadget QUAKE_GUN = new Gadget_QuakeGun();
    public static final Gadget WINTER_BREEZE = new Gadget_WinterBreeze();
    public static final Gadget NETHER_BLAZE = new Gadget_NetherBlaze();
    public static final Gadget PAINT_SPRAYER = new Gadget_Paintsprayer();
    public static final Gadget PAINT_TRAIL = new Gadget_PaintTrail();
    public static final Gadget FIRE_BENDER = new Gadget_FireBender();
    public static final Gadget NATURE_WIND = new Gadget_NatureWind();
    public static final Gadget SHEEP_BOMB = new Gadget_SheepBomb();
    public static final Gadget FALL_SCARE = new Gadget_FallScare();
    public static final Gadget ROCKET = new Gadget_Rocket();
    public static final Gadget PARTICLE_LAZER = new Gadget_Lazer();
    public static final Gadget POOP_BOMB = new Gadget_PoopBomb();
    public static final Gadget NINJA_VANISH = new Gadget_NinjaVanish();
    public static final Gadget BIRTHDAY_CANNON = new Gadget_BirthdayCannon();
    public static final Gadget BBQ_CANNON = new Gadget_BBQ();
    public static final Gadget FREEZE_BOMB = new Gadget_FreezeBomb();
    public static final Gadget STAR_BLAZING = new Gadget_StarBlazing();
    public static final Gadget BANANA_CANNON = new Gadget_BananaCannon();
    public static final Gadget GRAVITY_SURGE = new Gadget_GravitySurge();
    public static final Gadget PANCAKE_CANNON = new Gadget_PancakeCannon();
    public static final Gadget FIREWORK_CANNON = new Gadget_FireworkCannon();
    private int id;
    private String idName;
    private Material _material = null;
    private byte _data = 0;

    public Gadget(int id, String idName, Material mat, byte data) {
        this.id = id;
        this.idName = idName;
        _material = mat;
        _data = data;
    }

    public Gadget(int id, String idName, Material mat) {
        this.id = id;
        this.idName = idName;
        _material = mat;
        _data = (byte) 0;
    }

    public static void registerGadget(Gadget gadget) throws GadgetRegisterException {
        if (Variables.byId.containsKey(gadget.getId())) {
            throw new GadgetRegisterException("The Id value (" + gadget.getId() + ") is already registered.");
        }
        if (Variables.gadgetList.contains(gadget)) {
            throw new GadgetRegisterException("The gadget " + gadget.getIdName() + " is already registered.");
        }
        GadgetRegisterEvent event = new GadgetRegisterEvent(gadget);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

        String gadgetName = WordUtils.capitalizeFully(gadget.getIdName().replace("_", " "));
        gadget.loadExtraTags();
        Core.getLanguage().set(true, "Gadgets." + gadget.getIdName() + ".Enabled", true);
        Core.getLanguage().set(true, "Gadgets." + gadget.getIdName() + ".Name", gadgetName);
        Core.getLanguage().set(true, "Gadgets." + gadget.getIdName() + ".DisplayName", "&e" + gadgetName);
        Core.getLanguage().set(true, "Gadgets." + gadget.getIdName() + ".Description", Arrays.asList("&6Change Me in the", "&6Language.yml file"));
        Core.getLanguage().set(true, "Gadgets." + gadget.getIdName() + ".CooldownTime", 10);
        Core.getLanguage().set(true, "Gadgets." + gadget.getIdName() + ".Permission", "gadget." + gadgetName.replace(" ", ""));
        if (gadget.isEnabled()) {
            Core.get().getGadgetPageMaker().getRaw().add(gadget.getIdName());
        }
        Variables.byId.put(gadget.getId(), gadget);
        Variables.gadgetList.add(gadget);
        if (gadget instanceof GadgetListener) {
            GadgetListener listener = (GadgetListener) gadget;
            Bukkit.getServer().getPluginManager().registerEvents(listener, getPlugin().getGadgetPlugin());
        }
        //System.out.println("SimpleGadgets >> The gadget " + WordUtils.capitalizeFully(gadget.getIdName().replace("_", " ")) + " was successfully registered with the id " + gadget.getId());
    }

    public void loadExtraTags() {
    }

    public boolean hasPermission(Player player) {
        if (!Core.getLanguage().getBoolean("Needs-Permission"))
            return true;

        return (player.hasPermission(getPermission())) || (player.hasPermission("SimpleGadgets.gadget.*"));
    }

    public String getPermission() {
        return "SimpleGadgets." + Core.getLanguage().gadgetData(this, "Permission");
    }

    public int getId() {
        return id;
    }

    public String getActualName() {
        return Core.getLanguage().gadgetData(this, "Name");
    }

    public boolean isEnabled() {
        return Core.getLanguage().getBoolean("Gadgets." + getIdName() + ".Enabled");
    }

    public String getName() {
        return Core.getLanguage().gadgetData(this, "DisplayName");
    }

    public List<String> getDescription() {
        return Core.getLanguage().getStringList("Gadgets." + getIdName() + ".Description");
    }

    public int getCooldown() {
        return Integer.parseInt(Core.getLanguage().gadgetData(this, "CooldownTime"));
    }

    public void run(Player player) {
    }

    public void onBlockClick(Player player, Block block) {
        run(player);
    }

    public void onUserMove(Player player) {
    }

    protected void onProjectileHit(Projectile projectile) {
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(_material, 1, (byte) _data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(translate(getName()));
        if ((getDescription() != null) || (!getDescription().isEmpty()))
            meta.setLore(translate(getDescription()));
        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS,
                ItemFlag.HIDE_UNBREAKABLE
        );
        item.setItemMeta(meta);
        return item;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Gadget)) {
            return false;
        } else {
            Gadget other = (Gadget) obj;
            return this.id == other.id;
        }
    }

    public String getIdName() {
        return this.idName;
    }

    public int hashCode() {
        return this.id;
    }

    public String toString() {
        return "Gadget[" + this.id + ", " + this.getIdName() + "]";
    }

    public static Gadget getById(int id) {
        return Variables.byId.getKey(id);
    }

    public static Gadget getByIdName(String idName) {
        for (Gadget gadget : Gadget.values()) {
            if (gadget.getIdName().equals(idName))
                return gadget;
        }
        return null;
    }

    public static Gadget getByItem(ItemStack item) {
        for (Gadget gadget : values()) {
            if (gadget.getItemStack().isSimilar(item)) {
                return gadget;
            }
        }
        return null;
    }

    public static List<Gadget> values() {
        return Variables.gadgetList;
    }

    protected <T> T translate(T value) {
        if (value instanceof List) {
            List<String> org = (List<String>) value;
            List<String> toReturn = new ArrayList<>();
            for (String s : org) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
            }
            return (T) toReturn;
        } else if (value instanceof ArrayList) {
            ArrayList<String> org = (ArrayList<String>) value;
            ArrayList<String> toReturn = new ArrayList<>();
            for (String s : org) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
            }
            return (T) toReturn;
        } else if (value instanceof Gadget) {
            Gadget org = (Gadget) value;
            return (T) org.toString();
        } else {
            String s = (String) value;
            return (T) ChatColor.translateAlternateColorCodes('&', s);
        }
    }

    private static boolean spawnMe = false;

    public static class Listeners implements Listener {
        public Listeners() {
            Bukkit.getServer().getPluginManager().registerEvents(this, Core.get());
        }

        @EventHandler
        private void onSpawn(EntitySpawnEvent e) {
            if (e.isCancelled()) {
                if (spawnMe && e.getEntity().hasMetadata("Spawnable")) {
                    e.setCancelled(false);
                    spawnMe = false;
                }
            }
        }

        @EventHandler
        private void onBlockBreak(BlockBreakEvent e) {
            if (e.getBlock().hasMetadata("GadgetNoBlockBreak"))
                e.setCancelled(true);
        }

        @EventHandler
        public void pick(PlayerPickupItemEvent e) {
            if (e.getItem().hasMetadata("takeable")) e.setCancelled(true);
            if (e.getItem().hasMetadata("eatable")) {
                e.setCancelled(true);
                SoundPlayer.playSound(SoundMaker.ENTITY_GENERIC_EAT, e.getItem().getLocation());
                e.getItem().remove();
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 1));
                return;
            }
            if (e.getItem().hasMetadata("banana")) {
                e.setCancelled(true);
                SoundPlayer.playSound(SoundMaker.BLOCK_TRIPWIRE_CLICK_ON, e.getItem().getLocation());
                e.getItem().remove();
                ITitleMessage message = Reflection.getTitleMessage();
                message.sendMessage(e.getPlayer(), 0, 1, 0, "§e§lBANANA!");
                return;
            }
        }

        @EventHandler
        public void pick(InventoryPickupItemEvent e) {
            if ((e.getItem().hasMetadata("takeable")) || (e.getItem().hasMetadata("eatable"))) e.setCancelled(true);
        }

        @EventHandler
        public void onBlockChangeState(EntityChangeBlockEvent event) {
            if (event.getEntity().hasMetadata("GadgetFB")) {
                event.setCancelled(true);
                FallingBlock fb = (FallingBlock) event.getEntity();
                fb.getWorld().spigot().playEffect(fb.getLocation(), Effect.STEP_SOUND, fb.getBlockId(), fb.getBlockData(), 0.0F, 0.0F, 0.0F, 0.0F, 1, 32);
                event.getEntity().remove();
            }
        }

        @EventHandler
        private void onInteract(PlayerInteractEvent e) {
            if ((e.getAction() == Action.LEFT_CLICK_BLOCK) || (e.getAction() == Action.LEFT_CLICK_AIR)) {
                return;
            }
            Player player = e.getPlayer();
            if (player.getItemInHand() == null) {
                return;
            }
            if (player.getItemInHand().getType() == Material.AIR) {
                return;
            }
            if (!Variables.gadgetMap.containsKey(player.getName())) {
                return;
            }
            Gadget gadget = Variables.gadgetMap.getKey(player.getName());
            if (!Variables.isGadgetItem(gadget, player.getItemInHand())) {
                return;
            }
            e.setCancelled(true);
            GadgetActivateEvent event = new GadgetActivateEvent(gadget, player);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled())
                return;
            if (!Cooldown.hasCooldown(player, gadget)) {
                Cooldown.giveCooldown(player, gadget);
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    gadget.onBlockClick(player, e.getClickedBlock());
                    return;
                }
                gadget.run(player);
            }
        }

        @EventHandler
        public void onMove(PlayerMoveEvent event) {
            Player player = event.getPlayer();
            if (((event.getFrom().getBlockX() != event.getTo().getBlockX()) ||
                    (event.getFrom().getBlockY() != event.getTo().getBlockY()) ||
                    (event.getFrom().getBlockZ() != event.getTo().getBlockZ()))) {
                if (!Variables.gadgetMap.containsKey(player.getName())) {
                    return;
                }
                Gadget gadget = Variables.gadgetMap.getKey(player.getName());
                if (gadget == null) {
                    return;
                }
                gadget.onUserMove(player);
            }
        }

        @EventHandler
        private void onProjHit(ProjectileHitEvent e) {
            if (!e.getEntity().hasMetadata("GadgetProj")) {
                return;
            }
            if (e.getEntity().getShooter() instanceof Player) {
                Player player = (Player) e.getEntity().getShooter();
                if (!Variables.gadgetMap.containsKey(player.getName())) {
                    return;
                }
                Gadget gadget = Variables.gadgetMap.getKey(player.getName());
                if (gadget == null) {
                    return;
                }
                GadgetProjectileHitEvent event = new GadgetProjectileHitEvent(gadget, e.getEntity());
                Bukkit.getServer().getPluginManager().callEvent(event);
                gadget.onProjectileHit(event.getProjectile());
                event.getProjectile().remove();
            }
        }

        @EventHandler
        private void onDrop(PlayerDropItemEvent e) {
            Player player = e.getPlayer();
            if (!Variables.gadgetMap.containsKey(player.getName())) {
                return;
            }
            Gadget gadget = Variables.gadgetMap.getKey(player.getName());
            ItemStack dropped = e.getItemDrop().getItemStack();
            if (dropped.isSimilar(gadget.getItemStack())) {
                e.setCancelled(true);
                Variables.removeGadget(player);
            }
        }

        @EventHandler
        private void onLeave(PlayerQuitEvent e) {
            Player player = e.getPlayer();
            if (!Variables.gadgetMap.containsKey(player.getName())) {
                return;
            }
            Gadget gadget = Variables.gadgetMap.getKey(player.getName());
            if (player.getInventory().contains(gadget.getItemStack())) {
                player.getInventory().remove(gadget.getItemStack());
            }
        }

        @EventHandler
        private void onKick(PlayerKickEvent e) {
            Player player = e.getPlayer();
            if (!Variables.gadgetMap.containsKey(player.getName())) {
                return;
            }
            Gadget gadget = Variables.gadgetMap.getKey(player.getName());
            if (player.getInventory().contains(gadget.getItemStack())) {
                player.getInventory().remove(gadget.getItemStack());
            }
        }

        @EventHandler
        private void onDeath(PlayerDeathEvent e) {
            Player player = e.getEntity();
            if (!Variables.gadgetMap.containsKey(player.getName())) {
                return;
            }
            Gadget gadget = Variables.gadgetMap.getKey(player.getName());
            if (e.getDrops().contains(gadget.getItemStack())) {
                e.getDrops().remove(gadget.getItemStack());
            }
            if (player.getInventory().contains(gadget.getItemStack())) {
                player.getInventory().remove(gadget.getItemStack());
            }
        }

        @EventHandler
        private void onDamage(EntityDamageByEntityEvent e) {
            if (e.getDamager() instanceof Projectile) {
                Projectile proj = (Projectile) e.getDamager();
                if (proj.hasMetadata("GadgetProj")) {
                    e.setCancelled(true);
                }
            }
        }
    }

    public static class RandomRef {
        public static List<Location> getBlockLine(LivingEntity player, int length) {
            List<Location> list = new ArrayList<>();
            for (int amount = length; amount > 0; amount--)
                list.add(player.getTargetBlock(((Set<Material>) null), amount).getLocation());
            return list;
        }

        public static Location getEyeLocation(Entity player) {
            return player.getLocation().add(0.0D, 1.1D, 0.0D);
        }

        public static Location getTargetLocation(LivingEntity player) {
            return player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().multiply(100.0));
        }

        public static Location getTargetLocation(LivingEntity player, double length) {
            return player.getLocation().clone().add(player.getEyeLocation().getDirection().multiply(length));
        }

        public static List<Location> getStraightLine(LivingEntity user, int length) {
            List<Location> locations = new ArrayList<>();
            Location start = user.getEyeLocation();
            Location end = getTargetLocation(user, length);
            double dist = Math.abs(end.distance(start));
            for (int i = -1; i < length; ++i) {
                double delta = (double) i / dist;
                double x = (1.0D - delta) * start.getX() + delta * (end.getX() + 0.5D);
                double y = (1.0D - delta) * start.getY() + delta * (end.getY() + 0.5D);
                double z = (1.0D - delta) * start.getZ() + delta * (end.getZ() + 0.5D);
                Location l = new Location(start.getWorld(), x, y, z);
                locations.add(l);
            }
            return locations;
        }

        public static Vector calculatePath(Player player) {
            Random r = new Random();
            double yaw = Math.toRadians((double) (-player.getLocation().getYaw() - 90.0F));
            double pitch = Math.toRadians((double) (-player.getLocation().getPitch()));
            double x = Math.cos(pitch) * Math.cos(yaw);
            double y = Math.sin(pitch);
            double z = -Math.sin(yaw) * Math.cos(pitch);
            return new Vector(x, y, z);
        }

        public static Location getRandomLoc(Location location) {
            Location loc = location.clone();
            loc.add(MathUtils.random(0.1F, 0.8F), 0, MathUtils.random(0.1F, 0.8F));
            return loc;
        }

        public static Location getRandomLoc(float min, float max, Location location) {
            Location loc = location.clone();
            loc.add(MathUtils.random(min, max), 0, MathUtils.random(min, max));
            return loc;
        }

        public static void noArc(final Entity proj, final org.bukkit.util.Vector direction) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin().getGadgetPlugin(), new Runnable() {
                public void run() {
                    if (proj == null) {
                        return;
                    }

                    if (!proj.isValid()) {
                        proj.setVelocity(direction);
                        RandomRef.noArc(proj, direction);
                    }
                }
            }, 1L);
        }


        public static Entity[] getNearbyEntities(Location l, int radius) {
            int chunk = radius < 16 ? 1 : (radius - radius % 16) / 16;
            ArrayList<Entity> radiusEntities = new ArrayList<>();

            for (int ent = 0 - chunk; ent <= chunk; ++ent) {
                for (int chZ = 0 - chunk; chZ <= chunk; ++chZ) {
                    int x = (int) l.getX();
                    int y = (int) l.getY();
                    int z = (int) l.getZ();
                    Location loc = new Location(l.getWorld(), (double) (x + ent * 16), (double) y, (double) (z + chZ * 16));

                    for (Entity e : loc.getChunk().getEntities()) {
                        if (Objects.equals(e.getLocation().getWorld().getName(), l.getWorld().getName()) && e.getLocation().distance(l) <= (double) radius && e.getLocation().getBlock() != l.getBlock()) {
                            radiusEntities.add(e);
                        }
                    }
                }
            }

            return radiusEntities.toArray(new Entity[radiusEntities.size()]);
        }
    }

    /**
     * Handles Projectile launching.
     */
    public static class ProjectileRef {
        public static Projectile launchProjectile(LivingEntity entity, Class<? extends Projectile> proj) {
            Projectile p = entity.launchProjectile(proj);
            p.setShooter(entity);
            p.setMetadata("GadgetProj", new FixedMetadataValue(Core.get(), "GadgetProj"));
            p.setVelocity(p.getVelocity().multiply(2));
            return p;
        }

        public static Projectile launchProjectile(LivingEntity entity, Class<? extends Projectile> proj, double mod) {
            Projectile p = entity.launchProjectile(proj);
            p.setShooter(entity);
            p.setMetadata("GadgetProj", new FixedMetadataValue(Core.get(), "GadgetProj"));
            p.setVelocity(p.getVelocity().multiply(mod));
            return p;
        }

        public static Projectile launchProjectile(LivingEntity entity, Class<? extends Projectile> proj, int mod) {
            Projectile p = entity.launchProjectile(proj);
            p.setShooter(entity);
            p.setMetadata("GadgetProj", new FixedMetadataValue(Core.get(), "GadgetProj"));
            p.setVelocity(p.getVelocity().multiply(mod));
            return p;
        }

        public static Projectile launchProjectile(LivingEntity entity, Class<? extends Projectile> proj, float mod) {
            Projectile p = entity.launchProjectile(proj);
            p.setShooter(entity);
            p.setMetadata("GadgetProj", new FixedMetadataValue(Core.get(), "GadgetProj"));
            p.setVelocity(p.getVelocity().multiply(mod));
            return p;
        }
    }

    public static GadgetPlugin getPlugin() {
        return Core.get();
    }

    /**
     * Handles Entity spawning.
     */
    public static class EntityRef {
        public static <T extends Entity> T spawnMob(Location location, Class<? extends Entity> entityClass) {
            spawnMe = true;
            Entity ent = location.getWorld().spawn(location, entityClass);
            spawnMe = false;
            ent.setMetadata("Spawnable", new FixedMetadataValue(getPlugin().getGadgetPlugin(), "Spawnable"));
            return (T) ent;
        }

        public static <T extends Entity> T spawnMob(Location location, EntityType entityClass) {
            spawnMe = true;
            Entity ent = location.getWorld().spawnEntity(location, entityClass);
            spawnMe = false;
            ent.setMetadata("Spawnable", new FixedMetadataValue(getPlugin().getGadgetPlugin(), "Spawnable"));
            return (T) ent;
        }
    }

    public static class Variables {
        private static List<Gadget> gadgetList = new ArrayList<>();
        private static AdvMap<Integer, Gadget> byId = new AdvMap<>();
        private static AdvMap<String, Gadget> gadgetMap = new AdvMap<>();

        public static List<Gadget> getGadgetList() {
            return gadgetList;
        }

        /**
         * Checks if the item matches the gadget.
         */
        public static boolean isGadgetItem(Gadget gadget, ItemStack item) {
            return (gadget.getItemStack().isSimilar(item));
        }

        /**
         * Checks if the player has a gadget.
         */
        public static boolean hasGadget(Player player) {
            return gadgetMap.containsKey(player.getName());
        }

        /**
         * Removes the gadget item and also removes the player from the gadgetMap.
         */
        public static void removeGadget(Player player) {
            if (!hasGadget(player))
                return;
            Gadget gadget = gadgetMap.getKey(player.getName());
            if (player.getInventory().contains(gadget.getItemStack())) {
                player.getInventory().remove(gadget.getItemStack());
                player.updateInventory();
            }
            gadgetMap.remove(player.getName());
        }

        /**
         * If the player is in the gadgetMap, It will gadgetData the players current gadget.
         */
        public static Gadget getGadget(Player player) {
            return gadgetMap.getKey(player.getName());
        }

        /**
         * Set the player's gadget to the gadget.
         */
        public static void setGadget(Player player, Gadget gadget) {
            GadgetSelectEvent event = new GadgetSelectEvent(gadget, player);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled())
                return;
            if (hasGadget(player)) {
                removeGadget(player);
            }
            gadgetMap.put(player.getName(), gadget);
            player.getInventory().addItem(gadget.getItemStack());
            player.updateInventory();
        }
    }

    /**
     * Handles location sounds.
     */
    public static class SoundPlayer {
        public static void playSound(SoundMaker var0, Location var1) {
            var0.playSound(var1);
        }

        public static void playSound(Sound var0, Location var1) {
            var1.getWorld().playSound(var1, var0, 1.0F, 1.0F);
        }

        public static void playSound(SoundMaker var0, Location var1, float v, float v1) {
            var0.playSound(var1, v, v1);
        }
    }

    /**
     * Handles particle sending.
     */
    public static class ParticleRef {
        public static void sendParticle(ParticleMaker.Particle particleLib, Location location, double offsetX, double offsetY, double offsetZ, double speed, int amount) {
            ParticleMaker maker = new ParticleMaker(particleLib, speed, amount, offsetX, offsetY, offsetZ);
            maker.sendToLocation(location);
        }

        public static void sendParticle(ParticleMaker.Particle particleLib, Location location, double offsetX, double offsetY, double offsetZ, int amount) {
            sendParticle(particleLib, location, offsetX, offsetY, offsetZ, 0.0F, amount);
        }

        public static void sendParticle(ParticleMaker.Particle particleLib, Location location, double offsetX, double offsetY, double offsetZ, double speed) {
            sendParticle(particleLib, location, offsetX, offsetY, offsetZ, speed, 1);
        }

        public static void sendParticle(ParticleMaker.Particle particleLib, Location location, double offsetX, double offsetY, float offsetZ) {
            sendParticle(particleLib, location, offsetX, offsetY, offsetZ, 0.0F);
        }

        public static void sendParticle(ParticleMaker.Particle particleLib, Location location, int amount) {
            sendParticle(particleLib, location, 0.0F, 0.0F, 0.0F, amount);
        }

        public static void sendParticle(ParticleMaker.Particle particleLib, Location location) {
            sendParticle(particleLib, location, 1);
        }

        public static void sendParticle(Location location, Material material, short data, double offsetX, double offsetY, double offsetZ, double speed, int amount, double range) {
            ParticleMaker maker = new ParticleMaker(ParticleMaker.Particle.ITEM_CRACK, speed, amount, offsetX, offsetY, offsetZ);
            maker.setData(material, data);
            maker.sendToLocation(location);
        }

        public static void sendParticle(Location location, Material material, short data, double offsetX, double offsetY, double offsetZ, int amount, double range) {
            sendParticle(location, material, data, offsetX, offsetY, offsetZ, 0.0F, amount, range);
        }

        public static void sendParticle(Location location, Material material, short data, double offsetX, double offsetY, double offsetZ, int amount) {
            sendParticle(location, material, data, offsetX, offsetY, offsetZ, amount, 128.0);
        }

        public static void sendParticle(Location location, Material material, short data, double offsetX, double offsetY, double offsetZ) {
            sendParticle(location, material, data, offsetX, offsetY, offsetZ, 1);
        }

        public static void sendParticle(Location location, Material material, short data) {
            sendParticle(location, material, data, 0.0F, 0.0F, 0.0F);
        }
    }
}
