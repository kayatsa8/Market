package BusinessLayer.ExternalSystems.Purchase;

public class PaymentProxyService {
    /**
     * Dumb implementation until we will connect to external service
     */

    public PaymentProxyService(){

    }


    public boolean pay(int userId, double creditNumber) {
        //for testing
        return creditNumber > 0 ;
    }



}
