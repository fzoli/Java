package jpatest.client;

import javax.ejb.EJB;
import jpatest.NodeTestBeanRemote;

/**
 *
 * @author zoli
 */
public class Main {
    
    @EJB
    private static NodeTestBeanRemote nodeInfoBean;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Count: " + nodeInfoBean.getRootNodeCount());
    }
    
}
