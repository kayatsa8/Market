package BusinessLayer.ExternalSystems.Supply;

public class SupplyRequestAdapter implements SupplyTarget{


    private final SupplyProxyService service;

    public SupplyRequestAdapter(){
        service = new SupplyProxyService();
    }

    @Override
    public boolean supply(int userId, String address) {
        return service.supply(userId, address);
    }
}
