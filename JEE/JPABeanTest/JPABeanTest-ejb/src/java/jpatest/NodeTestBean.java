package jpatest;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author zoli
 */
@Stateless
public class NodeTestBean implements NodeTestBeanRemote, NodeTestBeanLocal {

    @PersistenceContext
    private EntityManager manager;
    
    @EJB
    private ProductTestBeanLocal productBean;
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void createTestNodes() {
        productBean.createTestProduct();
        Node t = manager.find(Node.class, 1l);
        if (t == null) {
            t = new Node("First entity");
            manager.persist(t);
            Node t2 = new Node("Second entity", t);
            manager.persist(t2);
        }
    }
    
    @Override
    public Node getTree() {
        return new Node(getNodes(false)) {

            @Override
            public String getInfo() {
                return "Root Node";
            }
            
        };
    }
    
    @Override
    public List<Node> getNodes() {
        return getNodes(true);
    }

    private List<Node> getNodes(boolean listAll) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Node> query = builder.createQuery(Node.class);
        Root<Node> root = query.from(Node.class);
        if (!listAll) query.where(builder.isNull(root.get(Node_.parent)));
        query.orderBy(builder.asc(root.get(Node_.name)));
        return manager.createQuery(query).getResultList();
    }
    
    @Override
    public long getRootNodeCount() {
        CriteriaBuilder qb = manager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<Node> root = cq.from(Node.class);
        cq.select(qb.count(root));
        cq.where(qb.isNull(root.get(Node_.parent)));
        return manager.createQuery(cq).getSingleResult();
    }

}
