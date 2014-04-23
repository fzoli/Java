package jpatest;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author zoli
 */
@ApplicationScoped
@ManagedBean(name = "nodes")
public class ManagedNodeBean {

    @EJB
    private NodeTestBeanLocal nodeBean;
    
    public ManagedNodeBean() {
    }
    
    public Node getTree() {
        return nodeBean.getTree();
    }
    
}
