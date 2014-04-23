package jpatest.products;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;

/**
 *
 * @author zoli
 */
@Entity
@DiscriminatorValue("BED")
public class Bed extends Furniture {
    
    @Embeddable
    public static class Size {

        private int width, height;

        public Size() {
        }

        public Size(int w, int h) {
            width = w;
            height = h;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }
        
    }
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="width", column=@Column(name="WIDTH_IN_CM")),
        @AttributeOverride(name="height", column=@Column(name="HEIGHT_IN_CM"))
    })
    private Size sizeInCm;

    public Size getSizeInCm() {
        return sizeInCm;
    }

    public void setSizeInCm(Size sizeInCm) {
        this.sizeInCm = sizeInCm;
    }
    
}
