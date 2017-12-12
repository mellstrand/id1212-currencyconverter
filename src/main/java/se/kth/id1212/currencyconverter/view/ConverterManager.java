/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.currencyconverter.view;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import se.kth.id1212.currencyconverter.controller.FacadeController;
import se.kth.id1212.currencyconverter.integration.EntityManagerException;
import se.kth.id1212.currencyconverter.model.Currency;
import se.kth.id1212.currencyconverter.model.Rates;

/**
 *
 * @author mellstrand
 */

@Named("converterManager")
@RequestScoped
public class ConverterManager implements Serializable {
    
    @EJB
    private FacadeController facadeController;
    
    private Exception exception;
    private double amount = 0;
    private double convertedAmount = 0;
    private long fromId;
    private long toId;
    
   
    private void handleException(Exception e) {
	e.printStackTrace(System.err);
	exception = e;
    }
    
    public Exception getException() {
        return exception;
    }
    
    public boolean getSuccess() {
	return exception == null;
    }
    
    public List<Currency> getAllCurrencies() {
        exception = null;
        try {
            return facadeController.getAllCurrencies();
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }
    
    public List<Rates> getRelatedCurrencies() {
        exception = null;
        try {
	    Currency fromCurrency = facadeController.getCurrency(fromId);
            return facadeController.getRelatedCurrencies(fromCurrency);
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }
    
    public void convertCurrency() {
	exception = null;
	try {
	    convertedAmount = facadeController.convert(fromId, toId, amount);
	} catch (EntityManagerException ex) {
	    handleException(ex);
	}
    }
    
    public void setAmount(double amount) {
	this.amount = amount;
    }
    
    public double getAmount() {
	return amount;
    }
    
    public void setConvertedAmount(double convertedAmount) {
	this.convertedAmount = convertedAmount;
    }
    
    public double getConvertedAmount() {
	return convertedAmount;
    }
    
    public void setFromId(long fromId) {
	this.fromId = fromId;
    }
    
    public long getFromId() {
	return fromId;
    }
    
    public void setToId(long toId) {
	this.toId = toId;
    }
    
    public long getToId() {
	return toId;
    }
            
}
