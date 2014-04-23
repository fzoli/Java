package jpatest.products;

import java.util.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;

/**
 *
 * @author zoli
 */
@Entity
@DiscriminatorValue("FOOD")
public class Food extends Product {
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date bestBefore;

    public Date getBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(Date bestBefore) {
        this.bestBefore = bestBefore;
    }
    
}
