package UnitTests;

import BusinessLayer.ExternalSystems.Purchase.PurchaseClient;
import BusinessLayer.ExternalSystems.Supply.SupplyClient;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalSystemsUnitTests {


    private PurchaseClient purchaseClient ;
    private SupplyClient supplyClient;
    @Before
    public void setUp() {
        this.supplyClient = new SupplyClient();
        this.purchaseClient = new PurchaseClient();

    }




    /**
     * Turning to payment service #3
     */

    @Test
    public void turnToPaymentService_Valid(){
        boolean paid = purchaseClient.pay(1, 123456789);
        assertTrue(paid);
    }

    @Test
    public void turnToPaymentService_TransactionInfoInValid(){
        boolean paid = purchaseClient.pay(1, -1);
        assertFalse(paid);
    }

//    @Test
//    public void turnToPaymentService_ServiceNotExisting(){
//        this.purchaseClient.chooseService();
//        assertFalse(paid);
//    }


    /**
     *  Turning to Supply Service #4
     */
    @Test
    public void turnToSupplyService_Valid(){
        boolean received = supplyClient.supply(1, "Address");
        assertTrue(received);
    }

//    @Test
//    public void turnToSupplyService_ItemsWrongDetails(){
//        List<CatalogItemService> wrongItems = new ArrayList<>();
//        boolean received = this.askForSupply(user1GuestId, wrongItems, "SupplyService1");
//        assertFalse(received);
//    }

    @Test
    public void turnToSupplyService_CustomerWrongDetails(){
        boolean received = supplyClient.supply(1, "");
        assertFalse(received);
    }

//    @Test
//    public void turnToSupplyService_ServiceNotExisting(){
//        List<CatalogItemService> items = new ArrayList<>();
//        boolean received = this.askForSupply(user1GuestId, items, "SupplyServiceNotExisting");
//        assertFalse(received);
//    }




}
