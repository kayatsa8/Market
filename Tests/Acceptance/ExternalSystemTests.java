package Acceptance;

import ServiceLayer.Objects.CatalogItemService;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ExternalSystemTests extends ProjectTest{

    @Override
    public void setUp() {
        super.setUp();
        //setUpAllMarket();
    }


    @After
    public void tearDown() {
        //delete stores and delete users from DB
    }


    /**
     * Change connection with external system #2
     */
    @Test @Ignore
    public void changeConnectionToExternalSystem_Valid(){

    }

    @Test @Ignore
    public void changeConnectionToExternalSystem_WrongStoreInfo(){

    }

    @Test @Ignore
    public void changeConnectionToExternalSystem_WrongSystemInformation(){

    }


    /**
     * Turning to payment service #3
     */
    /*
    @Test
    public void turnToPaymentService_Valid(){
        boolean paid = this.payCart(user1GuestId, "PaymentDetails", "PaymentService1");
        assertTrue(paid);
    }

    @Test
    public void turnToPaymentService_TransactionInfoInValid(){
        boolean paid = this.payCart(user1GuestId, "PaymentDetailsInValid", "PaymentService1");
        assertFalse(paid);
    }

    @Test
    public void turnToPaymentService_ServiceNotExisting(){
        boolean paid = this.payCart(user1GuestId, "PaymentDetails", "PaymentServiceNotExisting");
        assertFalse(paid);
    }
    */

    /**
     *  Turning to Supply Service #4
     */
    /*@Test
    public void turnToSupplyService_Valid(){
        List<CatalogItemService> items = new ArrayList<>();
        boolean received = this.askForSupply(user1GuestId, items, "SupplyService1");
        assertTrue(received);
    }

    @Test
    public void turnToSupplyService_ItemsWrongDetails(){
        List<CatalogItemService> wrongItems = new ArrayList<>();
        boolean received = this.askForSupply(user1GuestId, wrongItems, "SupplyService1");
        assertFalse(received);
    }

    @Test
    public void turnToSupplyService_CustomerWrongDetails(){
        List<CatalogItemService> items = new ArrayList<>();
        //Maybe if I need here an address put a wrong address? currently putting wrong userID
        boolean received = this.askForSupply(-1, items, "SupplyService1");
        assertFalse(received);
    }

    @Test
    public void turnToSupplyService_ServiceNotExisting(){
        List<CatalogItemService> items = new ArrayList<>();
        boolean received = this.askForSupply(user1GuestId, items, "SupplyServiceNotExisting");
        assertFalse(received);
    }
    */

}
