package jpatest.primefaces;

import jpatest.Node;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author zoli
 */
public class NodeModel {
    
    private static class MyTreeNode extends DefaultTreeNode {
        
        private final Node node;
        
        public MyTreeNode(Node node, TreeNode parent) {
            super(node.getName(), parent);
            this.node = node;
        }
        
        public Node getNode() {
            return node;
        }
        
    }
    
    private final boolean cache;
    private final TreeNode tn;
    
    public NodeModel(Node node) {
        this(node, false);
    }
    
    public NodeModel(Node node, boolean cache) {
        this(node, null, 0, cache);
    }
    
    private NodeModel(Node node, TreeNode parent, int level, boolean cache) {
        this.cache = cache;
        this.tn = new MyTreeNode(node, parent);
        if ((!cache || level < 2) && node.isChildAvailable()) {
            for (Node n : node.getChildren()) {
                new NodeModel(n, tn, level + 1, cache);
            }
        }
    }
    
    public TreeNode getTreeNode() {
        return tn;
    }
    
    public void onNodeExpand(NodeExpandEvent event) {
        if (event != null) loadNode(event.getTreeNode());
    }
    
    public void onNodeCollapse(NodeCollapseEvent event) {
        if (event != null) unloadNode(event.getTreeNode());
    }
    
    private void loadNode(TreeNode node) {
        if (!cache || node == null) return;
        for (TreeNode tn : node.getChildren()) {
            if (!chkNode(tn)) continue;
            Node nd = ((MyTreeNode)tn).getNode();
            if (nd.isChildAvailable()) {
                for (Node n : nd.getChildren()) {
                    new MyTreeNode(n, tn);
                }
            }
        }
    }
    
    private void unloadNode(TreeNode tn) {
        if (!cache || !chkNode(tn)) return;
        for (TreeNode n : tn.getChildren()) {
            collapseNode(n);    
            n.getChildren().clear();
        }
    }
    
    private void collapseNode(TreeNode tn) {
        tn.setExpanded(false);
        for (TreeNode n : tn.getChildren()) {
            collapseNode(n);
        }
    }
    
    private boolean chkNode(TreeNode node) {
        return (node != null && node instanceof MyTreeNode);
    }
    
}
