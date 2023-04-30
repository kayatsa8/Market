package BusinessLayer.ExternalSystems.Supply;

public class SupplyClient {



    private SupplyRequestAdapter adapter;

    public SupplyClient(){
        adapter = new SupplyRequestAdapter();
    }

    public boolean supply(int userId, String address){
        if(adapter != null){
            return adapter.supply(userId, address);
        }
        return false;
    }

    public void chooseService(){}

}
