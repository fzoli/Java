package jpatest.products;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author zoli
 */
@Entity
@DiscriminatorValue("FURNITURE")
public class Furniture extends Product {
    
    private String material;

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
    
}
