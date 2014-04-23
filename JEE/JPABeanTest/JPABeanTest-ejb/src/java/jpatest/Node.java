package jpatest;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author zoli
 */
@Entity(name = "Nodes") // Hibernate uses the name of the entity as table name too, but
@Table(name="\"Nodes\"") // EclipseLink keeps the default name so we have to use Table annotation too
//@Cacheable(false) // Disables the shared cache
public class Node implements Serializable, NodeObject<Node> {
    
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    
    @ManyToOne
    @JoinColumn(name="parent_id", nullable = true)
    private Node parent;
    
    @OneToMany(mappedBy = "parent")
    private List<Node> children;

    protected Node() {
        ;
    }

    public Node(String name) {
        this.name = name;
    }

    public Node(String name, Node parent) {
        this(name);
        this.parent = parent;
    }
    
    public Node(List<Node> children) {
        this.children = children;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isRoot() {
        return getParent() == null;
    }
    
    @Override
    public boolean isChildAvailable() {
        List<Node> children = getChildren();
        return children != null && !children.isEmpty();
    }
    
    @Override
    public Node getParent() {
        return parent;
    }
    
    @Override
    public List<Node> getChildren() {
        return children;
    }
    
    @Override
    public String toString() {
        return getInfo();
    }
    
    public String getInfo() {
        return "Node(id=" + id + ", parent=" + (parent == null ? "null" : parent.id) + ", name='" + name + "')";
    }
    
}
