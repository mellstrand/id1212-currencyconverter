/**
 *
 * @author mellstrand
 * @date 2017-12-06
 */

package se.kth.id1212.currencyconverter.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
    @NamedQuery(
	name = "getAllCurrencies",
	query = "SELECT c FROM Currency c"
    )
    ,
    @NamedQuery(
	name = "currencyExists",
	query = "SELECT c FROM Currency c WHERE c.shortName LIKE :shortName"
    )
})

/**
 * Entity class for Currency
 * Holds two string, a short and a long representation of a currency
 * Example: USD - US-dollar
 */
@Entity(name = "Currency")
public class Currency implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String longName;
    @Column(unique = true, nullable = false)
    private String shortName;
    
    public Currency() {
    }
    
    public Currency(String shortName, String longName) {
	this.shortName = shortName.toUpperCase();
    	this.longName = longName;
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

    @Override
    public String toString() {
        return shortName + " - " + longName;
    }
    
}
