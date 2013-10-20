package jpatest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * JPA example.
 * @author zoli
 */
public class Main {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPATestPU"); // create factory using META-INF/persistence.xml that contains database informations
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
            
            manager.refresh(t); // the first node has just been uploaded, we have to refresh the values of the reference object (t)
        }
        
        int count = t.getChildList().size();
        System.out.println("The first node has " + count + " " + (count > 1 ? "children" : "child") + ".");
        if (count > 0) System.out.println("The name of the first child: " + t.getChildList().get(0).getName());
        
        manager.close();
        factory.close();
    }
    
}
