/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package old.brainsynder.Gadgets.Errors;

/**
 * This Exception will run when ever a gadget tries to register and has a duplicate already added.
 */
public class GadgetRegisterException extends Exception {
    public GadgetRegisterException(String message) {
        super("SimpleGadgets Gadgets Error >> " + message);
    }
}
