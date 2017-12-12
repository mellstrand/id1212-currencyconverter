/**
 *
 * @author mellstrand
 * @date 2017-12-06
 */
package se.kth.id1212.currencyconverter.view;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import se.kth.id1212.currencyconverter.controller.FacadeController;
import se.kth.id1212.currencyconverter.integration.EntityManagerException;
import se.kth.id1212.currencyconverter.model.Currency;

/**
 * Bean for the "normal" users interactions to the web page
 */
@Named("converterManager")
@SessionScoped
public class ConverterManager implements Serializable {
    
    @EJB private FacadeController facadeController;
    private Exception exception;
    private double amount = 0;
    private double convertedAmount = 0;
    private long fromId;
    private long toId;
   
    /**
     * Print and set an exception 
     */
    private void handleException(Exception e) {
	e.printStackTrace(System.err);
	exception = e;
    }
    
    /**
     * Enables 'errorConverter.xhtml' to retrieve exception for display 
     * @return 
     */
    public Exception getException() {
        return exception;
    }
    
    /**
     * For 'faces-config.xml' to see if any exceptions has occurred 
     * @return 
     */
    public boolean getSuccess() {
	return exception == null;
    }
    
    /**
     * To display all currencies in a list for the user 
     * @return 
     */
    public List<Currency> getAllCurrencies() {
        exception = null;
        try {
            return facadeController.getAllCurrencies();
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }
    
    /**
     * Update Conversion Rates button
     * Calls controller for fetching new rates
     */
    public void updateRates() {
	exception = null;
	try {
	    facadeController.updateRates();
	} catch (Exception e) {
	    handleException(e);
	}
    }
    
    /**
     * Convert button
     * Calls controller to convert currency
     */
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
