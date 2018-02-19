/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package gadgets.brainsynder.Utils;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.Gadgets.Gadget;
import org.bukkit.entity.Player;
import simple.brainsynder.nms.IActionMessage;
import simple.brainsynder.utils.AdvMap;
import simple.brainsynder.utils.Reflection;

public class Cooldown {
	private static AdvMap< String, Long > cooldownMap = new AdvMap<> ();
	private static AdvMap< String, Gadget > gadgetMap = new AdvMap<> ();

    public static boolean hasCooldown (Player p, Gadget gadget) {
        int cooldown = gadget.getCooldown ();
        if (cooldownMap.containsKey (p.getName ()) && gadgetMap.containsKey (p.getName ())) {
            if (gadget.getIdName ().equals (gadgetMap.getKey (p.getName ()).getIdName ())) {
                long secondsLeft = cooldownMap.getKey (p.getName ()) / 1000L + cooldown - System.currentTimeMillis () / 1000L;
                if (secondsLeft > 0L) {
                    IActionMessage message = Reflection.getActionMessage();
                    String cooldownMessage = Core.getLanguage().getString("Messages.Cooldown", true).replace ("%gadget", gadget.getActualName()).replace("%sec", "" + secondsLeft);
                    message.sendMessage(p, cooldownMessage);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasCooldown (Player p, Gadget gadget, boolean msg) {
        if (msg) {
            return hasCooldown(p, gadget);
        }else{
            int cooldown = gadget.getCooldown ();
            if (cooldownMap.containsKey (p.getName ()) && gadgetMap.containsKey (p.getName ())) {
                if (gadget.getIdName ().equals (gadgetMap.getKey (p.getName ()).getIdName ())) {
                    long secondsLeft = cooldownMap.getKey (p.getName ()) / 1000L + cooldown - System.currentTimeMillis () / 1000L;
                    if (secondsLeft > 0L) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static long getTimeLeft (Player p) {
        if (!Gadget.Variables.hasGadget(p))
            return 0L;
        if (hasCooldown(p, Gadget.Variables.getGadget(p), false)) {
            int cooldown = Gadget.Variables.getGadget(p).getCooldown();
            return cooldownMap.getKey (p.getName ()) / 1000L + cooldown - System.currentTimeMillis () / 1000L;
        }else{
            return 0L;
        }
    }

    public static void removeCooldown (Player p) {
        cooldownMap.remove(p.getName ());
        gadgetMap.remove(p.getName ());
    }

	public static void giveCooldown (Player p, Gadget gadget) {
		cooldownMap.put (p.getName (), System.currentTimeMillis ());
		gadgetMap.put (p.getName (), gadget);
	}
}
