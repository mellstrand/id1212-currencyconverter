/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.currencyconverter.controller;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import se.kth.id1212.currencyconverter.integration.ConverterDAO;
import se.kth.id1212.currencyconverter.integration.EntityManagerException;
import se.kth.id1212.currencyconverter.model.Currency;
import se.kth.id1212.currencyconverter.model.Rates;

/**
 *
 * @author mellstrand
 */

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class FacadeController {
    
    @EJB
    ConverterDAO converterDAO;
    
    public List<Currency> getAllCurrencies() throws EntityManagerException  {
        return converterDAO.getAllCurrencies();
    }
    
    public double convert(long baseID, long nameID, double amount) throws EntityManagerException {
        Currency base = converterDAO.findCurrency(baseID);
        Currency to = converterDAO.findCurrency(nameID);
        Rates rate = converterDAO.getConversionRate(base, to);
        
        return rate.getRate() * amount;
    }
    
}
