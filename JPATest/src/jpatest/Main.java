package jpatest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author zoli
 */
public class Main {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPATestPU");
        EntityManager manager = factory.createEntityManager();
        
        Node t = manager.find(Node.class, 1l);
        
        if (t == null) {
            EntityTransaction tr = manager.getTransaction();
            tr.begin();
            t = new Node("First entity");
            manager.persist(t);
            Node t2 = new Node("Second entity", t);
            manager.persist(t2);
            tr.commit();
            
            manager.refresh(t);
        }
        
        int count = t.getChildList().size();
        System.out.println("The first node has " + count + " " + (count > 1 ? "children" : "child") + ".");
        if (count > 0) System.out.println("The name of the first child: " + t.getChildList().get(0).getName());
        
        manager.close();
        factory.close();
    }
    
}
