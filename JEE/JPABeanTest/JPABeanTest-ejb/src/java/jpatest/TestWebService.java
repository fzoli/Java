package jpatest;

import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.Oneway;
import javax.jws.WebMethod;

/**
 * This annotation creates a WebService which WSDL URL is http://localhost:8080/TestWebService/Nodes?wsdl
 * @author zoli
 */
@WebService(serviceName = "TestWebService", name = "Nodes")
@Stateless()
public class TestWebService {
    
    @EJB
    private NodeTestBeanLocal ejbRef;

    @WebMethod(operationName = "createTestNodes")
    @Oneway
    public void createTestNodes() {
        ejbRef.createTestNodes();
    }

    @WebMethod(operationName = "getTree")
    public Node getTree() {
        return ejbRef.getTree();
    }
    
}
