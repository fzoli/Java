package jpatest;

import java.util.List;

/**
 *
 * @author zoli
 * @param <T>
 */
public interface NodeObject<T extends NodeObject> {
    Long getId();
    String getName();
    boolean isRoot();
    boolean isChildAvailable();
    T getParent();
    List<T> getChildren();
}
