package com.example;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;

/*
@Path(("/simpleDTUPay"))
public class SimpleDTUPayResource {

    private SimpleDTUPayService simpleDTUPayService = new SimpleDTUPayService();


    public SimpleDTUPayResource() {
        System.out.println("SimpleDTUPayResource Created");
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public boolean transactionJSON(SimpleDTUPay simpleDTUPay) {
        // deserialize JSON to SimpleDTUPay object before using values of that object
        return simpleDTUPayService.transaction(simpleDTUPay.getAmount(), simpleDTUPay.getCid(), simpleDTUPay.getMid());
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<Integer, SimpleDTUPay> getTransactionsJSON() {
        return simpleDTUPayService.getTransactions();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public HashMap<Integer, SimpleDTUPay> getTransactionsXML() {
        return simpleDTUPayService.getTransactions();
    }
}
*/
