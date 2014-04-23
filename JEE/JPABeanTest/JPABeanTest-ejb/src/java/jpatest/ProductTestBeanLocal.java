package jpatest;

import javax.ejb.Local;

/**
 *
 * @author zoli
 */
@Local
public interface ProductTestBeanLocal {
    void createTestProduct();
}
