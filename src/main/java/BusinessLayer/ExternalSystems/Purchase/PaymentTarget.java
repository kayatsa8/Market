package BusinessLayer.ExternalSystems.Purchase;

public interface PaymentTarget {
    /**
     * The Interface the client knows about
     */

    public boolean pay(int userId, double creditNumber);

}
