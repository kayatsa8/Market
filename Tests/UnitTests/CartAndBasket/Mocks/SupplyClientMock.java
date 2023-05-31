package UnitTests.CartAndBasket.Mocks;

import BusinessLayer.ExternalSystems.Supply.SupplyClient;

public class SupplyClientMock extends SupplyClient {

    private boolean wantedAnswer;

    //_wantedAnswer is the answer we want to be returned in supply
    public SupplyClientMock(boolean _wantedAnswer){
        wantedAnswer = _wantedAnswer;
    }

    @Override
    public boolean supply(int userId, String address){
        return wantedAnswer;
    }

}
