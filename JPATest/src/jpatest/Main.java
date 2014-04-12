package jpatest;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * JPA example.
 * @author Zoltan Farkas
 */
public class Main {

    public static void main(String[] args) {
        HashMap<String, String> props = new HashMap<String, String>();
//        props.put("javax.persistence.jdbc.user", "teszt");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPATestPU", props); // create factory using META-INF/persistence.xml that contains database informations
        EntityManager manager = factory.createEntityManager(); // create manager who will execute the requests
        
        Node t = manager.find(Node.class, 1l); // retrieve the first Node object
        
        if (t == null) { // if the first node does not exist
            EntityTransaction tr = manager.getTransaction();
            tr.begin(); // begin a transaction before writing to the database
            t = new Node("First entity"); // constructs the first node
            manager.persist(t); // upload the first node to the databe
            Node t2 = new Node("Second entity", t); // constructs the second node whose parent is the first one
            manager.persist(t2); // upload the second node too
            tr.commit(); // commits the transaction (generates and calls two create SQL command)
            
            manager.refresh(t); // the first node has just been uploaded, so we have to refresh the values of the reference object (t)
        }
        
        int count = t.getChildList().size();
        System.out.println("The first node has " + count + " " + (count > 1 ? "children" : "child") + ".");
        if (count > 0) System.out.println("The name of the first child: " + t.getChildList().get(0).getName());
        
        // retrieve all nodes using JPQL (ordered by names)
        List<Node> ls = manager.createQuery("from Nodes n order by n.name asc", Node.class).getResultList(); // we have to write Nodes because of annotation @Entity(name = "Nodes"), but we can write the full class name too: jpatest.Node
        System.out.println(ls);
        
        // retrieve all nodes using criteria (you don't have to use String so you can rename entity names safely)
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Node> query = builder.createQuery(Node.class); // you will get Node objects
        Root<Node> root = query.from(Node.class); // select from node table without join
        query.orderBy(builder.asc(root.get(Node_.name))); // Node_ is a generated class that contains attributes of Node
        ls = manager.createQuery(query).getResultList();
        System.out.println(ls);
        
        manager.close();
        factory.close();
    }
    
}
