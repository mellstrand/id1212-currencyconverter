/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.currencyconverter.view;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import se.kth.id1212.currencyconverter.controller.FacadeController;
import se.kth.id1212.currencyconverter.integration.EntityManagerException;
import se.kth.id1212.currencyconverter.model.Currency;

/**
 *
 * @author mellstrand
 */

@Named("converterManager")
@ConversationScoped
public class ConverterManager implements Serializable {
    
    @EJB
    private FacadeController facadeController;
    @Inject
    private Conversation conversation;
    
    private Exception exception;
    private double amount;
    private double convertedAmount;
    private long fromId;
    private long toId;
    
    private void startConversation() {
        if(conversation.isTransient()) {
            conversation.begin();
        }
    }
    
    private void stopConversation() {
        if(!conversation.isTransient()) {
            conversation.end();
        }
    }
    
    private void handleException(Exception e) {
	stopConversation();
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
    
    public void convertCurrency() {
	exception = null;
	try {
	    convertedAmount = facadeController.convert(fromId, toId, amount);
	} catch (EntityManagerException ex) {
	    handleException(ex);
	}
    }
    
    public void updateRates() {
	exception = null;
	try {
	    facadeController.updateRates();
	} catch (Exception e) {
	    handleException(e);
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
    
    public void setFromCurrency(long id) {
	fromId = id;
    }
    
    public long getFromCurrency(long id) {
	return fromId;
    }
    
    public void setToCurrency(long id) {
	toId = id;
    }
    
    public long getToCurrency() {
	return toId;
    }
            
}
