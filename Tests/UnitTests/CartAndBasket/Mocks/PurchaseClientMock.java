package UnitTests.CartAndBasket.Mocks;

import BusinessLayer.ExternalSystems.Purchase.PurchaseClient;

public class PurchaseClientMock extends PurchaseClient {

    boolean wantedAnswer;

    //_wantedAnswer is the answer we want to be returned in pay
    public PurchaseClientMock(boolean _wantedAnswer){
        wantedAnswer = _wantedAnswer;
    }

    @Override
    public boolean pay(int userId, double creditNumber){
        return wantedAnswer;
    }

}
