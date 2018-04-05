/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package gadgets.brainsynder.files;

import gadgets.brainsynder.Core;
import gadgets.brainsynder.api.gadget.Gadget;
import simple.brainsynder.files.FileMaker;

import java.util.Arrays;

@SuppressWarnings("all")
public class Language extends FileMaker {
    public Language(Core core) {
        super(core, "Language.yml");
    }

    public String gadgetData(Gadget gadget, String tag) {
        return getString("Gadgets." + gadget.getIdName() + "." + tag);
    }

    public void loadDefaults() {
        if (!isSet("Messages.Cooldown"))
            set("Messages.Cooldown", "&eSimpleGadgets &6&l>> &c%gadget &7gadget has &c%sec &7left");
        if (!isSet("Messages.No-Permission"))
            set("Messages.No-Permission", "&eSimpleGadgets &6&l>> &7You do not have permission to run this task.");
        if (!isSet("Messages.OpenMenu"))
            set("Messages.OpenMenu", "&eSimpleGadgets &6&l>> &7Opening the gadgets menu.");
        if (!isSet("Messages.No-Block-Within-Distance"))
            set("Messages.No-Block-Within-Distance", "&6&lNo block found within %dist% blocks.");
        if (!isSet("Messages.Target-Block-is-liquid"))
            set("Messages.Target-Block-is-liquid", "&6&lBlock must not be a liquid.");
        if (!isSet("Needs-Permission"))
            set("Needs-Permission", false);
        if (!isSet("Slots-For-Gadgets")) {
            set("Slots-For-Gadgets", Arrays.asList(
                    "11", "12", "13", "14", "15", "16", "17",
                    "20", "21", "22", "23", "24", "25", "26",
                    "29", "30", "31", "32", "33", "34", "35",
                    "38", "39", "40", "41", "42", "43", "44"
            ));
        }
    }
}
