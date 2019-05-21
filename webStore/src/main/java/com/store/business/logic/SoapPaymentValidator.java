/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.store.business.logic;

import com.store.integration.BankingEndpoint;
import com.store.integration.BankingService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author cesi
 */
@Soap
@Stateless
public class SoapPaymentValidator implements PaymentValidator {

    @WebServiceRef(BankingService.class) 
    private BankingEndpoint banking;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Boolean process(String ccNumber, Double amount) {
        Boolean isValid = banking.paymentOperation(ccNumber, amount);         
        return isValid; 
    }
}
