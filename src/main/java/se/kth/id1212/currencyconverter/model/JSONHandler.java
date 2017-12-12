/**
 *
 * @author mellstrand
 * @date 2017-12-08
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
 * JSON Handler for fetching current currency rates from an online API
 * For more info...
 * @url http://api.fixer.io
 */
public class JSONHandler {
    
    private final String urlBase;
    
    public JSONHandler() {
	
	this.urlBase = "http://api.fixer.io/latest?base=";
    }
    /**
     * 
     * @param base The base currency for which to get conversion rates to
     *	      Currency on a 3 letter form, e.g. USD, SEK, EUR.
     * @return Rates for the requested base,  Entry example: "EUR": 0.84774
     * @throws MalformedURLException Requested URL not valid
     * @throws IOException Could not open connection to requested URL
     */
    public Set<Map.Entry<String, JsonElement>> getCurrencyUpdate(String base) throws MalformedURLException, IOException {
	
	String urlComplete = urlBase.concat(base);
	URL requestURL = new URL(urlComplete);
	HttpURLConnection request = (HttpURLConnection) requestURL.openConnection();
	request.connect();
	JsonParser parser = new JsonParser();
	JsonElement rootElement = parser.parse(new InputStreamReader((InputStream) request.getContent()));
	JsonObject rootObject = rootElement.getAsJsonObject();
	JsonElement ratesElement = rootObject.get("rates");
	JsonObject ratesObject = ratesElement.getAsJsonObject();
	
	return ratesObject.entrySet();
    }
    
}
