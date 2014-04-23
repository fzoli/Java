package jpatest.products;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author zoli
 */
@Entity
@DiscriminatorValue("CHAIR")
public class Chair extends Furniture {
    
    boolean rolling;

    public boolean isRolling() {
        return rolling;
    }

    public void setRolling(boolean rolling) {
        this.rolling = rolling;
    }
    
}
