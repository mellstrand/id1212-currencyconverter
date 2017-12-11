/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.currencyconverter.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author mellstrand
 */

@NamedQueries({
    @NamedQuery(
        name = "getConversionRate",
        query = "SELECT rate FROM Rates rate WHERE rate.baseCurrency=:baseCurrency AND rate.toCurrency=:toCurrency"
    )
})

@Entity(name = "Rates")
public class Rates implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @ManyToOne(optional = false)
    private Currency baseCurrency;
    @JoinColumn(nullable = false)
    private Currency toCurrency;
    @Column(nullable = false)
    private double rate;
    
    public Rates() {
        
    }
    
    public Rates(Currency baseCurrency, Currency toCurrency, double rate) {
        this.baseCurrency = baseCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
    }

    public long getId() {
        return id;
    }
    
    public double getRate() {
        return rate;
    }
    
    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (baseCurrency != null ? baseCurrency.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rates)) {
            return false;
        }
        Rates other = (Rates) object;
        if ((this.baseCurrency == null && other.baseCurrency != null) || (this.baseCurrency != null && !this.baseCurrency.equals(other.baseCurrency))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "["+ toCurrency + " - " + rate + "]";
    }
    
}
