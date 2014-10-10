package liv;

import sun.misc.Signal;
import sun.misc.SignalHandler;
/**
 * Signal handler which prints out to console a given object.
 *
 * @author Liviu Tudor http://about.me/liviutudor
 */
public class PrintOutObjectHandler implements SignalHandler {
    /** Object to print out to console. */
    private Object o;

    public PrintOutObjectHandler(Object o) {
        this.o = o;
    }

    /**
     * Handle the signal: simply print out the object.
     */
    public void handle(Signal sig) {
        System.out.println( o.toString());
    }
}
