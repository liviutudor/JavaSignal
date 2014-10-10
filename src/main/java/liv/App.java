package liv;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import sun.misc.Signal;

/**
 * Hello world!
 */
public class App implements ExitSignalListener {
    public static void main(String[] args) {
        try {
            new App().go();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void go() throws Exception {
        System.out.println("Registering mbean");
        ObjectName beanName = new ObjectName("liv:type=MagicBean");
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        MagicBean mb = new MagicBean();
        server.registerMBean(mb, beanName);

        // sort out exit handler
        ExitHandler exit = new ExitHandler();
        exit.addListener(this);
        Signal.handle(new Signal("ALRM"), exit);

        // now handle the stats dumping
        PrintOutObjectHandler print = new PrintOutObjectHandler(mb);
        Signal.handle(new Signal("USR2"), print);

        // now start the forever loop
        while (true) {
            mb.request();
            Thread.sleep(1000);
        }
    }

    public void notifyExit() {
        // simply print out a note
        System.out.println("Program exiting, cleaning up.");
    }
}
