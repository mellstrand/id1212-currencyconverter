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
            throw new EntityManagerException("Could not find requested currency.");
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
            throw new EntityManagerException("Could not get currencies");
        }
    }
    
    public Rates getConversionRate(Currency baseCurrency, Currency name) throws EntityManagerException {
        try {
            return entityManager.createNamedQuery("getConversionRate", Rates.class)
                    .setParameter("baseCurrency", baseCurrency)
                    .setParameter("name", name).getSingleResult();
        } catch (Exception e) {
            throw new EntityManagerException("Did not find conversion rate for "
                                                + baseCurrency + " to " + name);
        }
    }
    
    
}
