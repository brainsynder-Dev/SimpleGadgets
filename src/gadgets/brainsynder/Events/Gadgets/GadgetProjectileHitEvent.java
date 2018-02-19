package gadgets.brainsynder.Events.Gadgets;

import gadgets.brainsynder.Events.GadgetEvent;
import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;

public class GadgetProjectileHitEvent extends GadgetEvent implements Cancellable {
    private Projectile projectile;
    private boolean canceled = false;

    public GadgetProjectileHitEvent(Gadget gadget, Projectile projectile) {
        super(gadget);
        this.projectile = projectile;
    }

    public Projectile getProjectile () {
        return projectile;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.canceled = b;
    }
}
