package UnitTests.CartAndBasket.Mocks;

import BusinessLayer.ExternalSystems.Purchase.PurchaseClient;

public class PurchaseClientMock extends PurchaseClient {

    boolean wantedAnswer;

    //_wantedAnswer is the answer we want to be returned in pay
    public PurchaseClientMock(boolean _wantedAnswer){
        wantedAnswer = _wantedAnswer;
    }

    public int pay(String cardNumber, int month, int year, String buyerName, int ccv, int buyerId){
        return 10000;
    }

}
