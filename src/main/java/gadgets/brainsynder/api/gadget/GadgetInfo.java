package gadgets.brainsynder.api.gadget;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GadgetInfo {
    /**
     * Should the Gadget run checks for the PlayerMoveEvent
     */
    boolean moveMethods () default false;

    /**
     * Should the Gadget run checks for the ProjectileHitEvent
     */
    boolean projectileHit () default false;

    /**
     * Should the Gadget Cancel Fall damage when it is in use?
     */
    boolean cancelFall () default false;
}
