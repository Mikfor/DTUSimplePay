package com.example;

//@Path(("/simpleDTUPay"))
//public class SimpleDTUPayResource {
//
//    private SimpleDTUPayService simpleDTUPayService = new SimpleDTUPayService();
//
//
//    public SimpleDTUPayResource() {
//        System.out.println("SimpleDTUPayResource Router Created");
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    public boolean transactionJSON(SimpleDTUPayLedger simpleDTUPayLedger) {
//        // deserialize JSON to SimpleDTUPay object before using values of that object
//        return simpleDTUPayService.transaction(simpleDTUPayLedger.getTransactionAmount(), simpleDTUPayLedger.getPayer(), simpleDTUPayLedger.getPayee());
//    }
//
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public HashMap<Integer, SimpleDTUPayLedger> getTransactionsJSON() {
//        return simpleDTUPayService.getTransactions();
//    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_XML)
//    public HashMap<Integer, SimpleDTUPayLedger> getTransactionsXML() {
//        return simpleDTUPayService.getTransactions();
//    }
//}

