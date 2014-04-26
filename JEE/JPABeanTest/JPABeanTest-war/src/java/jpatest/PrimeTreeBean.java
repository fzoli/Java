package jpatest;

import java.io.Serializable;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;  
import javax.faces.bean.SessionScoped;  
import javax.faces.context.FacesContext;  
import jmxtest.PrimeTree;
import jpatest.primefaces.NodeModel;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.model.TreeNode;

@SessionScoped
@ManagedBean(name = "primeTree")
public class PrimeTreeBean implements Serializable {
    
// Implements Serializable to remove warning:
// WARNING! Setting non-serializable attribute value into HttpSession (key: primeTree, value class: jpatest.PrimeTreeBean).
    
    private NodeModel model, fullModel;
    
    @EJB
    private NodeTestBeanLocal bean;
    
    private PrimeTree mbean = new PrimeTree(this);
    
    public PrimeTreeBean() {
        mbean.open();
    }

    @PreDestroy
    private void close() {
        mbean.close();
    }

    public void onNodeExpand(NodeExpandEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lenyitva", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
        mbean.onNodeChanged(event.getTreeNode().toString(), true);
        model.onNodeExpand(event);
    }
    
    public void onNodeCollapse(NodeCollapseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Becsukva", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
        mbean.onNodeChanged(event.getTreeNode().toString(), false);
        model.onNodeCollapse(event);
    }
    
    public TreeNode getRoot() {
        if (model == null) {
            System.out.println("prime tree root init");
            model = new NodeModel(bean.getTree(), true);
        }
        return model.getTreeNode();  
    }
    
    public TreeNode getFullRoot() {
        if (fullModel == null) {
            System.out.println("prime tree full root init");
            fullModel = new NodeModel(bean.getTree());
        }
        return fullModel.getTreeNode();  
    }
    
}
