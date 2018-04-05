package gadgets.brainsynder.utilities.errors;

public class GadgetRegisterException extends Exception {
    public GadgetRegisterException(String message) {
        super("SimpleGadgets Gadgets Error >> " + message);
    }
}
