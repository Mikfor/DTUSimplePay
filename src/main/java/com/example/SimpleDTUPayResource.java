package com.example;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

@Path(("/simpleDTUPay"))
public class SimpleDTUPayResource {

    private final SimpleDTUPayService simpleDTUPayService = new SimpleDTUPayService();


    public SimpleDTUPayResource() {
        System.out.println("SimpleDTUPayResource Router Created");
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response transactionJSON(SimpleDTUPay simpleDTUPay) {
        // deserialize JSON to SimpleDTUPay object before using values of that object
        simpleDTUPayService.initiateTransaction(simpleDTUPay.getAmount(), simpleDTUPay.getCid(), simpleDTUPay.getMid());
        return Response.ok().build();
    }

    // I don't know if this can be done nor how to create subroutes in JAX-RS
    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response registerUserJSON(SimpleDTUPayUser simpleDTUPayUser) {
        // FIXME: There is an issue with the Exception being returned to the client via JSON; I don't know why
        // Generate a random UUID for testing since our accounts are not persistent
        simpleDTUPayService.registerUser(simpleDTUPayUser.getBankId().toString());
        return Response.ok().build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<Integer, SimpleDTUPayLedger> getTransactionsJSON() {
        return simpleDTUPayService.getTransactions();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public HashMap<Integer, SimpleDTUPayLedger> getTransactionsXML() {
        return simpleDTUPayService.getTransactions();
    }
}

