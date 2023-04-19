package BusinessLayer.ExternalSystems.Purchase;

public class PaymentRequestAdapter implements PaymentTarget {

    /**
     * This is the adapter which links between the unknown service to the interface we are working with (PaymentTarget)
     */

    private final PaymentProxyService service;
    public PaymentRequestAdapter(){
        this.service = new PaymentProxyService();
    }


    @Override
    public boolean pay(int userId, double creditNumber) {
        return service.pay(userId, creditNumber);
    }
}
