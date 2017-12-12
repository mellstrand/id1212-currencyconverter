/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.currencyconverter.view;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import se.kth.id1212.currencyconverter.controller.FacadeController;

/**
 *
 * @author mellstrand
 * @date 2017-12-08
 */
@Named("currencyManager")
@RequestScoped
public class CurrencyManager implements Serializable {
    
    @EJB
    private FacadeController fc;
    private String shortName;
    private long fromId;
    private long toId;
    private Exception exception;
    
    public String getShortName() {
	return shortName;
    }
    
    public void setShortName(String shortName) {
	this.shortName = shortName;
    }
    
    public long getFromId() {
	return fromId;
    }
    
    public void setFromId(long fromId) {
	this.fromId = fromId;
    }
    
    public long getToId() {
	return toId;
    }
    
    public void setToId(long toId) {
	this.toId = toId;
    }

    public void addCurrency() {
	exception = null;
	try {
	    fc.addCurrency(shortName);
	} catch (Exception e) {
	    handleException(e);
	}
    }
    
    public void addRatesBinding() {
	exception = null;
	try {
	    fc.addRatesBinding(fromId, toId);
	} catch (Exception e) {
	    handleException(e);
	}
    }
    
    public void updateRates() {
	exception = null;
	try {
	    fc.updateRates();
	} catch (Exception e) {
	    handleException(e);
	}
    }
    
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
    
}
