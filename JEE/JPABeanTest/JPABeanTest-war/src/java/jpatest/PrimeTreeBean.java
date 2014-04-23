package jpatest;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;  
import javax.faces.bean.SessionScoped;  
import javax.faces.context.FacesContext;  
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.model.TreeNode;
import jpatest.primefaces.NodeModel;

@SessionScoped
@ManagedBean(name = "primeTree")
public class PrimeTreeBean {
    
    private NodeModel model, fullModel;
    
    @EJB
    private NodeTestBeanLocal bean;
    
    public PrimeTreeBean() {
    }
    
    public void onNodeExpand(NodeExpandEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lenyitva", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
        model.onNodeExpand(event);
    }
    
    public void onNodeCollapse(NodeCollapseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Becsukva", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
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
