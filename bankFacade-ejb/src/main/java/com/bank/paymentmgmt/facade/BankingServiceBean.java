/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.paymentmgmt.facade;

import com.bank.paymentmgmt.domain.Payment;
import com.bank.paymentmgmt.integration.PaymentDAO;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author cesi
 */
@Stateless
@LocalBean
@WebService(
    endpointInterface ="com.bank.paymentmgmt.facade.BankingServiceEndpointInterface",
    portName = "BankingPort", 
    serviceName = "BankingService"
)
public class BankingServiceBean implements BankingServiceEndpointInterface, BankingServiceRemote {
    @Inject private PaymentDAO paymentDAO;
    
    @Inject private JMSContext context;
    
    @Resource(lookup = "jms/paymentQueue")
    private Queue paymentQueue;

    @Override
    public Boolean createPayment(String ccNumber, Double amount) {
        if(ccNumber.length()== 10 ){
            System.out.println("Montant payé : "+amount +" €");
            Payment p = new Payment();
            p.setCcNumber(ccNumber);
            p.setAmount(amount);
            //pour l’instant le retour n’estpas utilisé
            p = paymentDAO.add(p);
            sendPayment(p);
            return true;
        } else {
            return false;
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<Payment> lookupAllStoredPayments() {
        return paymentDAO.getAllStoredPayments();
    }

    @Override
    public Payment lookupStoredPayment(Long id) {
        return paymentDAO.find(id);
    }

    @Override
    public Payment deleteStoredPayment(Long id) {
        Payment p = paymentDAO.delete(id);
        if (p != null) {
            sendPayment(p);
        }
        return p;
    }
    
    private void sendPayment(Payment payment) {
        //utilisation  de  l'API JAX-B  de  gestion  de  flux  pour  marshaller  (transformer)  l'objet //Payment en chaine XML
        JAXBContext jaxbContext;
        try {
            //obtention d'une instance JAXBContext associée au type Payment annoté avec JAX-B
            jaxbContext = JAXBContext.newInstance(Payment.class);
            //création d'un Marshaller pour transfomer l'objet Java en flux XML
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            StringWriter writer = new StringWriter();
            //transformation de l'objet en flux XML stocké dans un Writer
            jaxbMarshaller.marshal(payment, writer);
            String xmlMessage = writer.toString();
            //affichage du XML dans la console de sortie
            System.out.println(xmlMessage);
            
            //encapsulation du paiement au format XML dans un objet javax.jms.TextMessage
            TextMessage msg = context.createTextMessage(xmlMessage);
            //envoi du message dans la queue paymentQueue
            context.createProducer().send(paymentQueue, msg);
        } catch (JAXBException ex) {
            Logger.getLogger(BankingServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
