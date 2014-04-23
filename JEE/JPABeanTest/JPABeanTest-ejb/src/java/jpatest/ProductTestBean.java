package jpatest;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpatest.products.Bed;

/**
 *
 * @author zoli
 */
@Stateless
public class ProductTestBean implements ProductTestBeanLocal {
    
    @PersistenceContext
    private EntityManager manager;
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createTestProduct() {
        Bed b = manager.find(Bed.class, 1l);
        if (b == null) {
            b = new Bed();
            b.setPrice(1000.99);
            b.setMaterial("wood");
            b.setSizeInCm(new Bed.Size(140, 200));
            manager.persist(b);
        }
    }
    
}
