/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author mellstrand
 */

@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
public class ConverterDAO {

    @PersistenceContext(unitName = "CurrencyConverterPU")
    private EntityManager entityManager;
    
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
    
    public List<Rates> getRelatedCurrencies(Currency baseCurrency) throws EntityManagerException {
	try {
	    return entityManager.createNamedQuery("getRelatedCurrencies", Rates.class)
		    .setParameter("baseCurrency", baseCurrency).getResultList();
	} catch(Exception e) {
	    throw new EntityManagerException("Could not get related currencies in database for: " + baseCurrency);
	}
    }
    
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
    
    private Currency currencyExists(Currency currency) throws EntityManagerException {
	String shortName = currency.getShortName();
	try {
	    return entityManager.createNamedQuery("currencyExists", Currency.class)
		    .setParameter("shortName", shortName).getSingleResult();
	} catch(NoResultException nre) {
	    return null;
	}
    }

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
    
    private Rates ratesExists(Currency baseCurrency, Currency toCurrency) throws EntityManagerException {
	try {
	    return entityManager.createNamedQuery("ratesExists", Rates.class)
		    .setParameter("baseCurrency", baseCurrency)
		    .setParameter("toCurrency", toCurrency).getSingleResult();
	} catch(NoResultException nre) {
	    return null;
	}
    }

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

    public void addRatesBindingToDataBase(Currency baseCurrency, Currency toCurrency) {
	entityManager.persist(new Rates(baseCurrency, toCurrency));
	System.out.println("DEBUG: addRatesBindingToDatabase(): " + baseCurrency + toCurrency);
    }
    
    
}
