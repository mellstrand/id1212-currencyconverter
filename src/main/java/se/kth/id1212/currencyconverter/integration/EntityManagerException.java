/**
 *
 * @author mellstrand
 * @date 2017-12-06
 */

package se.kth.id1212.currencyconverter.integration;

/**
 * An exception class for Exceptions occurring in integration/ConverterDAO.java
 */
public class EntityManagerException extends Exception {

    public EntityManagerException(String message) {
        super(message);
    }
    
}
