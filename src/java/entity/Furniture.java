/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Melnikov
 */
@Entity
@XmlRootElement
public class Furniture implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kitchenName;
    private String material;
    private String width;
    private String height;
    private Integer price;
    @OneToOne
    private Cover cover;
    private int discount;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date discountDate;
    private int discountDuration;
    
    

    public Furniture() {
    }

    public Furniture(String kitchenName, String material, String width, String height, Integer price, Cover cover) {
        this.kitchenName = kitchenName;
        this.material = material;
        this.width = width;
        this.height = height;
        this.price = price;
        this.cover = cover;
    }

    public Furniture(String kitchenName, String material, String width, String height, String price, Cover cover) {
        this.kitchenName = kitchenName;
        this.material = material;
        this.width = width;
        this.height = height;
        this.setPriceStr(price);
        this.cover = cover;
    }

    public String getKitchenName() {
        return kitchenName;
    }

    public void setKitchenName(String kitchenName) {
        this.kitchenName = kitchenName;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.kitchenName);
        hash = 53 * hash + Objects.hashCode(this.material);
        hash = 53 * hash + Objects.hashCode(this.width);
        hash = 53 * hash + Objects.hashCode(this.height);
        hash = 53 * hash + this.price;
        return hash;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Furniture other = (Furniture) obj;
        if (this.price != other.price) {
            return false;
        }
        if (this.discount != other.discount) {
            return false;
        }
        if (this.discountDuration != other.discountDuration) {
            return false;
        }
        if (!Objects.equals(this.kitchenName, other.kitchenName)) {
            return false;
        }
        if (!Objects.equals(this.material, other.material)) {
            return false;
        }
        if (!Objects.equals(this.width, other.width)) {
            return false;
        }
        if (!Objects.equals(this.height, other.height)) {
            return false;
        }
        if (!Objects.equals(this.cover, other.cover)) {
            return false;
        }
        if (!Objects.equals(this.discountDate, other.discountDate)) {
            return false;
        }
        return true;
    }



    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public int getPrice() {
        return price;
    }
    public double getPriceDouble() {
        return (double)this.price/100;
    }
    public String getPriceStr() {
        Double dPrice = (double)this.price/100;
        return dPrice.toString();
    }

    public void setPriceStr(String price) {
        price = price.trim().replaceAll(",", ".");
        try {
            double dPrice = Double.parseDouble(price);
            this.price = (int)(dPrice * 100);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Неправильный формат числа");
        }
    }
    public void setPriceDouble(double price) {
        this.price = (int)(price * 100);
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Date getDiscountDate() {
        return discountDate;
    }

    public void setDiscountDate(Date discountDate) {
        this.discountDate = discountDate;
    }

    public int getDiscountDuration() {
        return discountDuration;
    }

    public void setDiscountDuration(int discountDuration) {
        this.discountDuration = discountDuration;
    }
    
    
    
    @Override
    public String toString() {
        return "Furniture{" 
                + "id=" + id 
                + ", kitchenName=" + kitchenName 
                + ", material=" + material 
                + ", width=" + width 
                + ", height=" + height 
                + ", price=" + price 
                + '}';
    }

    
}
