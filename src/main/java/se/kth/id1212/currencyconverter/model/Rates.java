/**
 *
 * @author mellstrand
 * @date 2017-12-06
 */

package se.kth.id1212.currencyconverter.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
    @NamedQuery(
        name = "getConversionRate",
        query = "SELECT rate FROM Rates rate WHERE rate.baseCurrency=:baseCurrency AND rate.toCurrency=:toCurrency"
    ),
    @NamedQuery(
	name = "getRelatedCurrencies",
	query = "SELECT rate FROM Rates rate WHERE rate.baseCurrency=:baseCurrency"
    ),
    @NamedQuery(
	name = "ratesExists",
	query = "SELECT rate FROM Rates rate WHERE rate.baseCurrency=:baseCurrency AND rate.toCurrency=:toCurrency"
    )
})

/**
 * Entity class for Rates
 * Holds information about rates from a currency to another
 */

@Entity(name = "Rates")
public class Rates implements Serializable {
    
    @Id @ManyToOne
    private Currency baseCurrency;
    @Id @ManyToOne
    private Currency toCurrency;
    @Column(nullable = false)
    private double rate;
    
    public Rates() { 
    }
    
    public Rates(Currency baseCurrency, Currency toCurrency) {
	this.baseCurrency = baseCurrency;
        this.toCurrency = toCurrency;
        this.rate = 0;
    }
    
    public Rates(Currency baseCurrency, Currency toCurrency, double rate) {
        this.baseCurrency = baseCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }
    
    public void setRate(double rate) {
        this.rate = rate;
    }
    
    public Currency getTo() {
	return toCurrency;
    }
    
    public void setTo(Currency toCurrency) {
	this.toCurrency = toCurrency;
    }
    
    public Currency getFrom() {
	return baseCurrency;
    }
    
    public void setFrom(Currency baseCurrency) {
	this.baseCurrency = baseCurrency;
    }

    @Override
    public String toString() {
        return "["+ baseCurrency + " to: " + toCurrency + " - " + rate + "]";
    }
    
}
