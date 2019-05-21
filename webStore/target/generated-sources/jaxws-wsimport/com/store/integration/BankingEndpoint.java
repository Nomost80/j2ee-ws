
package com.store.integration;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebService(name = "BankingEndpoint", targetNamespace = "http://facade.paymentmgmt.bank.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface BankingEndpoint {


    /**
     * 
     * @param amountPaid
     * @param cardNumber
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(name = "acceptedPayment", targetNamespace = "")
    @RequestWrapper(localName = "paymentOperation", targetNamespace = "http://facade.paymentmgmt.bank.com/", className = "com.store.integration.PaymentOperation")
    @ResponseWrapper(localName = "paymentOperationResponse", targetNamespace = "http://facade.paymentmgmt.bank.com/", className = "com.store.integration.PaymentOperationResponse")
    @Action(input = "http://facade.paymentmgmt.bank.com/BankingEndpoint/paymentOperationRequest", output = "http://facade.paymentmgmt.bank.com/BankingEndpoint/paymentOperationResponse")
    public Boolean paymentOperation(
        @WebParam(name = "cardNumber", targetNamespace = "")
        String cardNumber,
        @WebParam(name = "amountPaid", targetNamespace = "")
        Double amountPaid);

}
