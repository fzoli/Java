package jpatest;

import javax.ejb.Remote;

/**
 *
 * @author zoli
 */
@Remote
public interface NodeTestBeanRemote {

//    It is not working :-(
//    List<? extends NodeObject> getNodes();
    
    long getRootNodeCount();
    
}
