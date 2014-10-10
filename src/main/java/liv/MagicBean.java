package liv;

import java.util.concurrent.atomic.AtomicLong;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

/**
 * Simple MBean which is also dumped to console. We print the contents on
 * console based on the {@link #toString()} method.
 *
 * @author Liviu Tudor http://about.me/liviutudor
 */
public class MagicBean implements DynamicMBean {
    /* Name of JMX attributes */
    private static final String ATTR_UPTIME   = "uptime";
    private static final String ATTR_REQUESTS = "requests";
    /**
     * Keeps track of requests processed so far.
     */
    private AtomicLong          requests;

    /**
     * Stores the start time - when the bean was created. Based on this we
     * compute uptime.
     */
    private long                startTime;

    public MagicBean() {
        startTime = System.currentTimeMillis();
        requests = new AtomicLong();
    }

    public long getRequests() {
        return requests.get();
    }

    public void request() {
        requests.incrementAndGet();
    }

    public long getUptime() {
        return System.currentTimeMillis() - startTime;
    }

    public Object getAttribute(String attribute) throws AttributeNotFoundException {
        if (ATTR_REQUESTS.equals(attribute)) {
            return getRequests();
        }
        if (ATTR_UPTIME.equals(attribute)) {
            return getUptime();
        }
        throw new AttributeNotFoundException();
    }

    public void setAttribute(Attribute attribute) throws InvalidAttributeValueException {
        throw new InvalidAttributeValueException("Read only attribute");
    }

    public AttributeList getAttributes(String[] attributes) {
        AttributeList list = new AttributeList();
        for (String a : attributes) {
            try {
                list.add(new Attribute(a, getAttribute(a)));
            } catch (AttributeNotFoundException e) {
                // we don't re-throw this, just ignore it
            }
        }
        return list;
    }

    public AttributeList setAttributes(AttributeList attributes) {
        // NOTE: all attributes read-only so don't do anything
        return null;
    }

    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException,
            ReflectionException {
        // NOTE we have no methods so just throw exception
        throw new ReflectionException(new IllegalArgumentException("Method not found"));
    }

    public MBeanInfo getMBeanInfo() {
        MBeanAttributeInfo attrs[] = new MBeanAttributeInfo[] {
                new MBeanAttributeInfo(ATTR_REQUESTS, int.class.getName(), "Number of requests processed", true,
                        false, false),
                new MBeanAttributeInfo(ATTR_UPTIME, long.class.getName(), "Uptime in miliseconds", true, false,
                        false)};
        return new MBeanInfo(MagicBean.class.getName(), "Simple JMX bean", attrs, null, null, null);
    }

    @Override
    public String toString() {
        return "Uptime: " + getUptime() + "ms, Requests: " + getRequests();
    }
}
