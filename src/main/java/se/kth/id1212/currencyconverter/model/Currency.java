/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.currencyconverter.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author mellstrand
 */
@Entity(name = "Currency")
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(unique = true, nullable = false)
    private String longName;
    @Column(unique = true, nullable = false)
    private String shortName;
    
    @OneToMany(mappedBy = "baseCurrency", cascade = CascadeType.ALL)
    private Collection<Rates> rates = new ArrayList<>();
    
    public Currency() {
    }
    
    public Currency(String longName, String shortName) {
        this.longName = longName;
        this.shortName = shortName.toUpperCase();
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }
    
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName.toUpperCase();
    }
    
    
    
    public Collection<Rates> getRates() {
        return rates;
    }
    
    public void setRates(Collection<Rates> rates) {
        this.rates = rates;
    }
 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (shortName != null ? shortName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Currency)) {
            return false;
        }
        Currency other = (Currency) object;
        if ((this.shortName == null && other.shortName != null) || (this.shortName != null && !this.shortName.equals(other.shortName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return shortName + " - " + longName;
    }
    
}
