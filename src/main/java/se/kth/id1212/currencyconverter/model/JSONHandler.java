/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.currencyconverter.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

/**
 *
 * @author mellstrand
 */
public class JSONHandler {
    
    private final String urlBase;
    
    public JSONHandler() {
	
	this.urlBase = "http://api.fixer.io/latest?base=";
	 
    }
    
    public void getCurrencyUpdate(String currency) throws MalformedURLException, IOException {
	
	URL url;
	String u = urlBase.concat(currency);
	url = new URL(u);
	
	HttpURLConnection request = (HttpURLConnection) url.openConnection();
	request.connect();
	
	JsonParser jp = new JsonParser();
	JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
	JsonObject rootObj = root.getAsJsonObject();
	
	JsonElement rates = rootObj.get("rates");
	JsonObject ratesObj = rates.getAsJsonObject();
	
	JsonElement aud = ratesObj.get("AUD");
	double AUDrate = aud.getAsDouble();
	
	JsonArray msg = rates.getAsJsonArray();
            Iterator<JsonElement> iterator = msg.iterator();
	    int i = 0;
            while (iterator.hasNext()) {
                System.out.println("Rates: " + i + " - " + iterator.next());
		i++;
	    }
	
    }
   
    
}
