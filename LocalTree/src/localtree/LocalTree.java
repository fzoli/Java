package localtree;

import java.awt.Component;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

public class LocalTree {
    private JTree tree;
    private JFrame frame;

    private static interface MyUserObject {

        public String getName();

        public Icon getIcon();
        
    }

    private static interface MyUserObjectWithFile extends MyUserObject {

        public File[] listFiles();
        
        public boolean isDirectory();
        
        public boolean isExpanded();
        
        public void expand();
        
    }

    private static abstract class AbstractUserObject implements MyUserObjectWithFile {
        private File file;
        private boolean expanded;
        
        public AbstractUserObject(File file) {
            this.file = file;
            expanded = false;
        }
        
        protected File getFile() {
            return file;
        }

        @Override
        public boolean isExpanded() {
            return expanded;
        }
        
        @Override
        public void expand() {
            expanded = true;
        }
        
        @Override
        public File[] listFiles() {
            return getFile().listFiles();
        }
        
    }
    
    private static class ParentUserObject extends AbstractUserObject {
        private static Icon rootIcon = new ImageIcon(LocalTree.class.getResource("/resources/hdd.png"));
        
        public ParentUserObject(File file) {
            super(file);
        }

        @Override
        public String getName() {
            return getFile().getAbsolutePath();
        }

        @Override
        public Icon getIcon() {
            return rootIcon;
        }

        @Override
        public boolean isDirectory() {
            return true;
        }

    }

    private static class ChildUserObject extends AbstractUserObject {
        private static Icon fileIcon = new ImageIcon(LocalTree.class.getResource("/resources/file.png"));
        private static Icon dirIcon = new ImageIcon(LocalTree.class.getResource("/resources/folder.png"));
        
        public ChildUserObject(File file) {
            super(file);
        }

        @Override
        public String getName() {
            return getFile().getName();
        }

        @Override
        public Icon getIcon() {
            return getFile().isFile() ? fileIcon : dirIcon;
        }
        
        @Override
        public boolean isDirectory() {
            return getFile().isDirectory();
        }
        
    }

    public LocalTree() {
        init();
    }

    private void init() {
        setLAF();
        initFrame();
    }

    private void initFrame() {
        initTree();
        JScrollPane scrollPane = new JScrollPane(tree);
        frame = new JFrame("Lista");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 450);
        frame.setLocationRelativeTo(frame);
        frame.getContentPane().add(scrollPane);
        frame.setVisible(true);
    }

    private void setLAF() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception ex) {}
    }

    private void initTree() {
        MutableTreeNode root = new DefaultMutableTreeNode(
            new MyUserObject() {

                @Override
                public String getName() {
                    return "Számítógép";
                }

                @Override
                public Icon getIcon() {
                    return new ImageIcon(LocalTree.class.getResource("/resources/computer.png"));
                }
            
        }, true);
        TreeModel model = new DefaultTreeModel(root);
        this.tree = new JTree(model);
        setTreeParents();
        setTreeEventHandler();
        setTreeCellRenderer();
    }

    private void setTreeEventHandler() {
        tree.addTreeWillExpandListener(new TreeWillExpandListener() {

            @Override
            public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
                for (int i = 0; i < node.getChildCount(); i++) {
                    expandNode((DefaultMutableTreeNode)node.getChildAt(i));
                }
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent e) throws ExpandVetoException {
                ;
            }
            
        });
    }
    
    private void setTreeCellRenderer() {
        tree.setCellRenderer(new TreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                MyUserObject o = (MyUserObject)((DefaultMutableTreeNode)value).getUserObject();
                JLabel label = new JLabel(o.getName());
                label.setIcon(o.getIcon());
                return label;
            }
            
        });
    }

    private void setTreeParents() {
        File[] parents = File.listRoots();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)tree.getModel().getRoot();
        for (File f : parents) {
            expandNode(createNode(root, new ParentUserObject(f)));
        }
        tree.updateUI();
    }
    
    private void expandNode(DefaultMutableTreeNode node) {
        Object o = node.getUserObject();
        if (o instanceof MyUserObjectWithFile) {
            MyUserObjectWithFile uo = (MyUserObjectWithFile)o;
            if (uo.isDirectory() && !uo.isExpanded()) {
                uo.expand();
                File[] files = uo.listFiles();
                if (files != null) {
                    for (File f : files) {
                        createNode(node, new ChildUserObject(f));
                    }
                }
            }
        }
    }
    
    private DefaultMutableTreeNode createNode(DefaultMutableTreeNode root, MyUserObject uo) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(uo, true);
        root.add(node);
        return node;
    }
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                new LocalTree();
            }
            
        });
    }
    
}