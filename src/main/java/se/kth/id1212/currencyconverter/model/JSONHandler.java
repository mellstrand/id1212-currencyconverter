/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.currencyconverter.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mellstrand
 */
public class JSONHandler {
    
    private final String urlBase;
    
    public JSONHandler() {
	
	this.urlBase = "http://api.fixer.io/latest?base=";
	 
    }
    
    public Set<Map.Entry<String, JsonElement>> getCurrencyUpdate(String base) throws MalformedURLException, IOException {
	
	URL url;
	String u = urlBase.concat(base);
	url = new URL(u);
	
	HttpURLConnection request = (HttpURLConnection) url.openConnection();
	request.connect();
	
	JsonParser jp = new JsonParser();
	JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
	JsonObject rootObject = root.getAsJsonObject();
	JsonElement rates = rootObject.get("rates");
	JsonObject ratesObject = rates.getAsJsonObject();
	
	return ratesObject.entrySet();
    }
   
    
}
