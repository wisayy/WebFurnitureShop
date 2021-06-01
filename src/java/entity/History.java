/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.CascadeType;
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
public class History implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne()
    private Furniture furniture;
    @OneToOne()
    private Customer customer;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date giveOutDate;

    public History() {
    }

    public History(Furniture furniture, Customer customer, Date giveOutDate) {
        this.furniture = furniture;
        this.customer = customer;
        this.giveOutDate = giveOutDate;
    }

    public Furniture getFurniture() {
        return furniture;
    }

    public void setFurniture(Furniture furniture) {
        this.furniture = furniture;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer Customer) {
        this.customer = Customer;
    }

    public Date getGiveOutDate() {
        return giveOutDate;
    }

    public void setGiveOutDate(Date giveOutDate) {
        this.giveOutDate = giveOutDate;
    }

    @Override
    public String toString() {
        return "History{" 
                + "furniture=" + furniture.getKitchenName()
                + ", Customer=" + customer.getLastname()
                + ", giveOutDate=" + giveOutDate
                + '}';
    }

    public Long getId() {
        return id;
    }

    

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.furniture);
        hash = 67 * hash + Objects.hashCode(this.customer);
        hash = 67 * hash + Objects.hashCode(this.giveOutDate);
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
        final History other = (History) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.furniture, other.furniture)) {
            return false;
        }
        if (!Objects.equals(this.customer, other.customer)) {
            return false;
        }
        if (!Objects.equals(this.giveOutDate, other.giveOutDate)) {
            return false;
        }
        return true;
       
    }
    
}

