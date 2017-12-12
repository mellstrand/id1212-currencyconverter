/**
 *
 * @author mellstrand
 * @date 2017-12-06
 */

package se.kth.id1212.currencyconverter.integration;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import se.kth.id1212.currencyconverter.model.Currency;
import se.kth.id1212.currencyconverter.model.Rates;

/**
 * ConverterDAO handles database communication
 */

@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
public class ConverterDAO {

    @PersistenceContext(unitName = "CurrencyConverterPU")
    private EntityManager entityManager;
    
    /**
     * Find currency in database
     * @param id Id for the currency in the database
     * @return Searched currency
     * @throws EntityManagerException If currency could not be found
     */
    public Currency findCurrency(long id) throws EntityManagerException {
        try {
            return entityManager.find(Currency.class, id);
        } catch (Exception e) {
            throw new EntityManagerException("Could not find requested currency in database.");
        }
    }
    
    /**
     * For listing of currencies for the client to choose from
     * @return Returns a list with all currencies in the database
     * @throws EntityManagerException
     */
    public List<Currency> getAllCurrencies() throws EntityManagerException {
        try {
            return entityManager.createNamedQuery("getAllCurrencies", Currency.class).getResultList();
        } catch(Exception e) {
            throw new EntityManagerException("Could not get currencies from database");
        }
    }
    
    /**
     * Get currencies that are connected in the database to the 'from' currency
     * @param baseCurrency The currency the user wants to convert from
     * @return List of Rates object <base,to,rate>
     * @throws EntityManagerException 
     */
    public List<Rates> getRelatedCurrencies(Currency baseCurrency) throws EntityManagerException {
	try {
	    return entityManager.createNamedQuery("getRelatedCurrencies", Rates.class)
		    .setParameter("baseCurrency", baseCurrency).getResultList();
	} catch(Exception e) {
	    throw new EntityManagerException("Could not get related currencies in database for: " + baseCurrency);
	}
    }
    
    /**
     * Get searched currency pair to be able to get the conversion rate
     * @param baseCurrency Currency to convert from
     * @param toCurrency Currency to convert to
     * @return Rates object
     * @throws EntityManagerException 
     */
    public Rates getConversionRate(Currency baseCurrency, Currency toCurrency) throws EntityManagerException {
        try {
            return entityManager.createNamedQuery("getConversionRate", Rates.class)
                    .setParameter("baseCurrency", baseCurrency)
                    .setParameter("toCurrency", toCurrency).getSingleResult();
        } catch (Exception e) {
            throw new EntityManagerException("Did not find conversion rate for "
                                                + baseCurrency + " to " + toCurrency);
        }
    }
    
    /**
     * Check in a currency exists in the database's Currency table
     * @param currency Searched currency
     * @return A Currency object if it exists
     * @throws EntityManagerException 
     */
    private Currency currencyExists(Currency currency) throws EntityManagerException {
	String shortName = currency.getShortName();
	try {
	    return entityManager.createNamedQuery("currencyExists", Currency.class)
		    .setParameter("shortName", shortName).getSingleResult();
	} catch(NoResultException nre) {
	    return null;
	}
    }

    /**
     * Add currency object to the database Currency table
     * @param currency Currency to store
     * @throws EntityManagerException 
     */
    public void addCurrencyToDataBase(Currency currency) throws EntityManagerException {
	if(currencyExists(currency) != null) {
	    throw new EntityManagerException("Could not create: " + currency + ". Already in database.");
	}
	try {
	    entityManager.persist(currency);
	    System.out.println("DEBUG: addCurrencyToDatabase(): " + currency);
	} catch(Exception e) {
	    throw new EntityManagerException("Problem with adding currency... ");
	}
    }
    
    /**
     * Check if a rate connection are existing
     * @param baseCurrency From currency
     * @param toCurrency To currency
     * @return Rates object for searched currencies
     * @throws EntityManagerException 
     */
    private Rates ratesExists(Currency baseCurrency, Currency toCurrency) throws EntityManagerException {
	try {
	    return entityManager.createNamedQuery("ratesExists", Rates.class)
		    .setParameter("baseCurrency", baseCurrency)
		    .setParameter("toCurrency", toCurrency).getSingleResult();
	} catch(NoResultException nre) {
	    return null;
	}
    }

    /**
     * Update the rate between two currencies. If none in table, create the Rates object
     * Else update the rate for the searched currencies
     * @param baseCurrency From currency
     * @param toCurrency To currency
     * @param value Conversion rate
     * @throws EntityManagerException 
     */
    public void updateConversionRate(Currency baseCurrency, Currency toCurrency, double value) throws EntityManagerException {
	Rates rate = ratesExists(baseCurrency, toCurrency);
	if(rate == null) {
	    try {
		entityManager.persist(new Rates(baseCurrency, toCurrency, value));
		System.out.println("DEBUG: updateConversionRate(), new Rates(): " + baseCurrency + toCurrency);
	    } catch(Exception e) {
		throw new EntityManagerException("Problem with adding new conversion rate... ");
	    }
	} else {
	    try {
		rate.setRate(value);
		System.out.println("DEBUG: updateConversionRate(), SetRate(): " + baseCurrency + toCurrency);
	    
	    } catch(Exception e) {
		throw new EntityManagerException("Problem with updating conversion rate... ");
	    }
	}
	
    }

    /**
     * Add a initial connection between two currencies, so that a rate can be fetched for these
     * @param baseCurrency From currency
     * @param toCurrency To currency
     * @throws EntityManagerException
     */
    public void addRatesBindingToDataBase(Currency baseCurrency, Currency toCurrency) throws EntityManagerException {
	try {
	    entityManager.persist(new Rates(baseCurrency, toCurrency));
	    System.out.println("DEBUG: addRatesBindingToDatabase(): " + baseCurrency + toCurrency);
	} catch(Exception e) {
	    throw new EntityManagerException("Could not create Rates object");
	}
    }
    
}
