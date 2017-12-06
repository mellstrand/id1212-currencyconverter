/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.currencyconverter.view;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author mellstrand
 */

@Named("converterManager")
@ConversationScoped
public class ConverterManager implements Serializable {
    
    @EJB
    //private Controller controller;
    
    @Inject
    private Conversation conversation;
    
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
    
    
    public void convert() {
        
    }
            
}
