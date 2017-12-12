/**
 *
 * @author mellstrand
 * @date 2017-12-06
 */

package se.kth.id1212.currencyconverter.controller;

import com.google.gson.JsonElement;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import se.kth.id1212.currencyconverter.integration.ConverterDAO;
import se.kth.id1212.currencyconverter.integration.EntityManagerException;
import se.kth.id1212.currencyconverter.model.Currency;
import se.kth.id1212.currencyconverter.model.JSONHandler;
import se.kth.id1212.currencyconverter.model.Rates;

/**
 * Handles requests from client (view)
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class FacadeController {
    
    @EJB ConverterDAO converterDAO;
    private JSONHandler json;
    private final HashMap<String,String> currencyList = new HashMap<>();
   
    /**
     * A PostConstruct method to init the HashMap of currencies
     */
    @PostConstruct
    public void init() {
	initCurrencyList();
    }
    
    /**
     * Get all currencies in Currency Table
     * @return List of Currencies
     * @throws EntityManagerException 
     */
    public List<Currency> getAllCurrencies() throws EntityManagerException  {
        return converterDAO.getAllCurrencies();
    }
    
    /**
     * Get related currencies to a base currency, i.e. currencies it has rates
     * connections to
     * @param currency Base currency
     * @return List of Rates
     * @throws EntityManagerException 
     */
    public List<Rates> getRelatedCurrencies(Currency currency) throws EntityManagerException  {
        System.out.println("DEBUG: getRelatedCurrencies(): " + currency);
	return converterDAO.getRelatedCurrencies(currency);
    }
    
    /**
     * Convert from one currency to another currency
     * @param fromID From currency's id in db table
     * @param toID To currency's id in db table
     * @param amount Amount to convert
     * @return Converted amount
     * @throws EntityManagerException 
     */
    public double convert(long fromID, long toID, double amount) throws EntityManagerException {
        Currency from = converterDAO.findCurrency(fromID);
        Currency to = converterDAO.findCurrency(toID);
        Rates rate = converterDAO.getConversionRate(from, to);
        
        return rate.getRate() * amount;
    }
   
    /**
     * Get new rates for each currency to their related currencies
     * Fetched from API, more info in model/JSONHandler.java
     * @throws IOException
     * @throws EntityManagerException
     * @throws Exception 
     */
    public void updateRates() throws IOException, EntityManagerException, Exception {
	List<Currency> currencies = getAllCurrencies();
	json = new JSONHandler();
	
	for(Currency baseCurrency : currencies) {
	    String baseCurrencyShortName = baseCurrency.getShortName();
	    Set<Map.Entry<String, JsonElement>> entry = json.getCurrencyUpdate(baseCurrencyShortName);
	    
	    List<Rates> relatedCurrencies = getRelatedCurrencies(baseCurrency);
	    for(Rates rate : relatedCurrencies) {
		Currency toCurrency = rate.getTo();
		String toCurrencyShortName = toCurrency.getShortName();
		
		for (Map.Entry<String,JsonElement> temp : entry) {
		    if(temp.getKey().equals(toCurrencyShortName)) {
			System.out.println("DEBUG updateRates(): " + baseCurrency + " - " + toCurrency + " - " + temp.getValue());
			updateConversionRate(baseCurrency, toCurrency, temp.getValue().getAsDouble());
		    }
		}
	    }
	}	
    }
    
    /**
     * Adds a connection between two currencies
     * @param fromId Currency to convert from
     * @param toId Currency to convert to
     * @throws EntityManagerException 
     */
    public void addRatesBinding(long fromId, long toId) throws EntityManagerException {
	Currency from = converterDAO.findCurrency(fromId);
	Currency to = converterDAO.findCurrency(toId);
	converterDAO.addRatesBindingToDatabase(from, to);
    }
    
    /**
     * Update the rate between two currencies. If none in table, create the Rates object
     * Else update the rate for the searched currencies
     * @param baseCurrency From currency
     * @param toCurrency To currency
     * @param value Conversion rate
     * @throws Exception 
     */
    public void updateConversionRate(Currency baseCurrency, Currency toCurrency, double value) throws Exception {
	Rates rate = converterDAO.ratesExists(baseCurrency, toCurrency);
	if(rate == null) {
	    converterDAO.addRatesToDatabase(baseCurrency, toCurrency, value);
	} else {
	    try {
		rate.setRate(value);
	    } catch(Exception e) {
		throw new Exception("Could not update rate...");
	    }
	}
	
    }
    
    /**
     * Add currency to database table Currency
     * @param shortName Three letter name for the currency
     * @throws EntityManagerException
     * @throws Exception 
     */
    public void addCurrency(String shortName) throws EntityManagerException, Exception {
	String longName = currencyList.get(shortName.toUpperCase());
	if(longName == null) {
	    throw new Exception("Currency not in list...");
	}
	System.out.println("DEBUG: " + longName);
	converterDAO.addCurrencyToDataBase(new Currency(shortName, longName));
	 
    }
    
    /**
     * Get Currency object from Id
     * @param fromId
     * @return Currency object
     * @throws EntityManagerException 
     */
    public Currency getCurrency(long fromId) throws EntityManagerException {
	System.out.println("DEBIG: getCurrency: " + fromId);
	return converterDAO.findCurrency(fromId);
    }
    
    /**
     * HashMap of available rates in API
     * Swedish strings for use in 'longName' variable
     */
    private void initCurrencyList() {
	currencyList.put("AUD", "Australinsk Dollar");
	currencyList.put("BGN", "Bulgarisk Ny Lev");
	currencyList.put("BRL", "Brasiliansk Real");
	currencyList.put("CAD", "Kanadensisk Dollar");
	currencyList.put("CHF", "Schweizisk Franc");
	currencyList.put("CNY", "Kinesisk Yuan Renminbi");
	currencyList.put("CZK", "Tjeckisk Koruna");
	currencyList.put("DKK", "Dansk Krona");
	currencyList.put("EUR", "Euro");
	currencyList.put("GBP", "Brittisk Pund Sterling");
	currencyList.put("HKD", "Hongkong-dollar");
	currencyList.put("HRK", "Kroatisk Kuna");
	currencyList.put("HUF", "Ungersk Forint");
	currencyList.put("IDR", "Indonesisk Rupiah");
	currencyList.put("ILS", "Israelisk Ny Shekel");
	currencyList.put("INR", "Indisk Rupie");
	currencyList.put("JPY", "Japansk Yen");
	currencyList.put("KRW", "Sydkoreansk Won");
	currencyList.put("MXN", "Mexikansk Peso");
	currencyList.put("MYR", "Malayisk Ringgit");
	currencyList.put("NOK", "Norsk Krona");
	currencyList.put("NZD", "Nyzeeländsk Dollar");
	currencyList.put("PHP", "Filippinsk Peso");
	currencyList.put("PLN", "Polsk Zloty");
	currencyList.put("RON", "Rumänsk Leu");
	currencyList.put("RUB", "Rysk Rubel");
	currencyList.put("SEK", "Svensk Krona");
	currencyList.put("SGD", "Singapore-dollar");
	currencyList.put("THB", "Thailändsk Baht");
	currencyList.put("TRY", "Ny Turkisk Lira");
	currencyList.put("USD", "US-dollar");
	currencyList.put("ZAR", "Sydafrikansk Rand");
    }
}
