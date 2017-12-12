/**
 *
 * @author mellstrand
 * @date 2017-12-08
 */

package se.kth.id1212.currencyconverter.view;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import se.kth.id1212.currencyconverter.controller.FacadeController;

/**
 * Bean for the 'Dev Section' on the web page 
 */
@Named("currencyManager")
@SessionScoped
public class CurrencyManager implements Serializable {
    
    @EJB private FacadeController fc;
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

    /**
     * Add currency button
     * Calls the controller to add the requested currency to the db
     */
    public void addCurrency() {
	exception = null;
	try {
	    fc.addCurrency(shortName);
	} catch (Exception e) {
	    handleException(e);
	}
    }
    
    /**
     * Add Rate binding button
     * Calls controller to add a binding between the two currencies
     */
    public void addRatesBinding() {
	exception = null;
	try {
	    fc.addRatesBinding(fromId, toId);
	} catch (Exception e) {
	    handleException(e);
	}
    }
    
    /**
     * Print and set an exception 
     */
    private void handleException(Exception e) {
	e.printStackTrace(System.err);
	exception = e;
    }
    
    /**
     * Enables 'errorCurrancy.xhtml' to retrieve exception for display 
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
    
}
