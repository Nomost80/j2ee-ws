/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.store.business.logic;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author cesi
 */
@Rest
@Stateless
public class RestPaymentValidator implements PaymentValidator {
    
    @Inject private Client client;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public Boolean process(String ccNumber, Double amount) {
        //création d'un builder pour créer un objet java représentant un message json
        JsonObjectBuilder paymentBuilder = Json.createObjectBuilder();
        //utilisation du builder pour créer la représentation JSON du paiement
        JsonObject paymentObject = paymentBuilder.add("ccNumber",ccNumber).add("amount",amount).build();
        WebTarget target = client.target("http://localhost:11080/bankFacade-war/banking/payment");
        Response resp = target.request().post(Entity.entity(paymentObject.toString(), MediaType.APPLICATION_JSON_TYPE));
        Boolean success = resp.getStatus() == 202;
        resp.close();
        client.close();
        return success;
    }
}
