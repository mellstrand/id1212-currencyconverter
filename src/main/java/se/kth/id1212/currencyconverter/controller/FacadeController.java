/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.currencyconverter.controller;

import com.google.gson.JsonElement;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
 *
 * @author mellstrand
 * @date 2017-12-06
 */

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class FacadeController {
    
    @EJB
    ConverterDAO converterDAO;
    private JSONHandler json;
    private final HashMap<String,String> ratesList = new HashMap<>();
    private boolean init = false;
   
    
    public List<Currency> getAllCurrencies() throws EntityManagerException  {
        return converterDAO.getAllCurrencies();
    }
    
    public List<Rates> getRelatedCurrencies(Currency currency) throws EntityManagerException  {
        System.out.println("DEBUG: getRelatedCurrencies(): " + currency);
	return converterDAO.getRelatedCurrencies(currency);
    }
    
    public double convert(long fromID, long toID, double amount) throws EntityManagerException {
        Currency from = converterDAO.findCurrency(fromID);
        Currency to = converterDAO.findCurrency(toID);
        Rates rate = converterDAO.getConversionRate(from, to);
        
        return rate.getRate() * amount;
    }
   
    public Currency getCurrency(long fromId) throws EntityManagerException {
	System.out.println("DEBIG: getCurrency: " + fromId);
	return converterDAO.findCurrency(fromId);
    }
    
    
    
    public void updateRates() throws IOException, EntityManagerException {
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
			converterDAO.updateConversionRate(baseCurrency, toCurrency, temp.getValue().getAsDouble());
		    }
		}
	    }
	}	
    }
    
    public void addRatesBinding(long fromId, long toId) throws EntityManagerException {
	Currency from = converterDAO.findCurrency(fromId);
	Currency to = converterDAO.findCurrency(toId);
	converterDAO.addRatesBindingToDataBase(from, to);
    }
    
    public void addCurrency(String shortName) throws EntityManagerException, Exception {
	if(!init) {
	    initRatesList();
	}
	
    	String longName = ratesList.get(shortName.toUpperCase());
	if(longName == null) {
	    throw new Exception("Currency not in list...");
	}
	System.out.println("DEBUG: " + longName);
	converterDAO.addCurrencyToDataBase(new Currency(shortName, longName));
	 
    }

    private void initRatesList() {
	ratesList.put("AUD", "Australinsk Dollar");
	ratesList.put("BGN", "Bulgarisk Ny Lev");
	ratesList.put("BRL", "Brasiliansk Real");
	ratesList.put("CAD", "Kanadensisk Dollar");
	ratesList.put("CHF", "Schweizisk Franc");
	ratesList.put("CNY", "Kinesisk Yuan Renminbi");
	ratesList.put("CZK", "Tjeckisk Koruna");
	ratesList.put("DKK", "Dansk Krona");
	ratesList.put("EUR", "Euro");
	ratesList.put("GBP", "Brittisk Pund Sterling");
	ratesList.put("HKD", "Hongkong-dollar");
	ratesList.put("HRK", "Kroatisk Kuna");
	ratesList.put("HUF", "Ungersk Forint");
	ratesList.put("IDR", "Indonesisk Rupiah");
	ratesList.put("ILS", "Israelisk Ny Shekel");
	ratesList.put("INR", "Indisk Rupie");
	ratesList.put("JPY", "Japansk Yen");
	ratesList.put("KRW", "Sydkoreansk Won");
	ratesList.put("MXN", "Mexikansk Peso");
	ratesList.put("MYR", "Malayisk Ringgit");
	ratesList.put("NOK", "Norsk Krona");
	ratesList.put("NZD", "Nyzeeländsk Dollar");
	ratesList.put("PHP", "Filippinsk Peso");
	ratesList.put("PLN", "Polsk Zloty");
	ratesList.put("RON", "Rumänsk Leu");
	ratesList.put("RUB", "Rysk Rubel");
	ratesList.put("SEK", "Svensk Krona");
	ratesList.put("SGD", "Singapore-dollar");
	ratesList.put("THB", "Thailändsk Baht");
	ratesList.put("TRY", "Ny Turkisk Lira");
	ratesList.put("USD", "US-dollar");
	ratesList.put("ZAR", "Sydafrikansk Rand");
	init = true;
    }
}
