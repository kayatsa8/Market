package BusinessLayer.ExternalSystems.Supply;

public class SupplyProxyService {

    public SupplyProxyService(){}



    public boolean supply(int userId, String address) {
        if(address.length() == 0)
            return false;
        return true;
    }
}
