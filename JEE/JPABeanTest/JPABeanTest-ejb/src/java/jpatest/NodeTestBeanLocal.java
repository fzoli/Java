package jpatest;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author zoli
 */
@Local
public interface NodeTestBeanLocal {
    
    void createTestNodes();
    
    List<Node> getNodes();
    
    Node getTree();
    
}
