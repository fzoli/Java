package jmxtest;

/**
 *
 * @author zoli
 */
public interface PrimeTreeMBean {
    int getUserActionCount();
    void expandTree();
    void collapseTree();
}
