package jmxtest;

import java.lang.management.ManagementFactory;
import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import jpatest.PrimeTreeBean;
import jpatest.primefaces.NodeModel;

/**
 *
 * @author zoli
 */
public class PrimeTree extends NotificationBroadcasterSupport implements PrimeTreeMBean {

    private static final MBeanServer MBS = ManagementFactory.getPlatformMBeanServer();
    
    private static final Object LOCK = new Object();
    private static int initCounter = 0;
    
    private final PrimeTreeBean bean;
    
    private ObjectName name;
    
    private int seqNum = 1;
    
    public PrimeTree(PrimeTreeBean bean) {
        this.bean = bean;
    }

    public void open() {
        synchronized (LOCK) {
            if (name == null) try {
                name = new ObjectName("jmxtest:type=PrimeTree,name=" + initCounter++);
                MBS.registerMBean(this, name);
            }
            catch (Exception ex) {
                ;
            }
        }
    }
    
    public void close() {
        synchronized (LOCK) {
            if (name != null) try {
                MBS.unregisterMBean(name);
            }
            catch (Exception ex) {
                ;
            }
        }
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[] {
            AttributeChangeNotification.ATTRIBUTE_CHANGE
        };
        String attrName = AttributeChangeNotification.class.getName();
        String attrDescription = "An attribute of the PrimeTreeBean has changed";
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, attrName, attrDescription);
        return new MBeanNotificationInfo[] {info};
    }

    @Override
    public int getUserActionCount() {
        return seqNum;
    }
    
    @Override
    public void expandTree() {
        setTreeExpanded(true);
    }
    
    @Override
    public void collapseTree() {
        setTreeExpanded(false);
    }

    /**
     * Expand or collapse the whole tree.
     * Refresh page http://localhost:8080/JPABeanTest-war/faces/prime-tree.xhtml
     * to see the results.
     */
    private void setTreeExpanded(boolean expanded) {
        NodeModel.setNodeExpanded(bean.getFullRoot(), expanded);
    }
    
    public void onNodeChanged(String name, boolean expand) {
        Notification n = new AttributeChangeNotification(
                this, // source
                seqNum++,
                System.currentTimeMillis(), // timestamp
                "Node[" + name + "] has been " + (expand ? "expanded" : "collapsed") + ".", // msg
                "expanded", // attr name
                Boolean.class.getCanonicalName(), // attr type
                !expand, // old val
                expand); // new val
        sendNotification(n);
    }
    
}
