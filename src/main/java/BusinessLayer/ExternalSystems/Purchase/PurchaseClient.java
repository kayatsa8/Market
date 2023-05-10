package BusinessLayer.ExternalSystems.Purchase;

public class PurchaseClient {
    /**
     * This class will hold all the adapters. Later we will choose which external system to use
     * explanation: <a href="https://www.geeksforgeeks.org/adapter-pattern/">...</a>
     */

    //currently we have 1 external system, so i=only 1 adapter
    private PaymentRequestAdapter adapter = new PaymentRequestAdapter();


    public PurchaseClient(){}


    public boolean pay(int userId, double creditNumber){
        if(adapter != null){
            return adapter.pay(userId, creditNumber);
        }
        return false;
    }

    public void chooseService(){}


}
