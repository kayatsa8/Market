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
     *  Turning to Supply Service #4
     */
    @Test
    public void turnToSupplyService_Valid(){
        boolean received = supplyClient.supply(1, "Address");
        assertTrue(received);
    }

    @Test
    public void turnToSupplyService_CustomerWrongDetails(){
        boolean received = supplyClient.supply(1, "");
        assertFalse(received);
    }

    @Test
    public void supply_Valid(){
        try {
            int transId = supplyClient.supply("Name", "Address", "City", "Country", "Zip");
            assertTrue(transId >= 10000);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void cancelSupply_Valid(){
        try {
            int transId = supplyClient.supply("Name", "Address", "City", "Country", "Zip");
            boolean supplied = transId >= 10000 ;
            if(supplied){
                boolean canceled = supplyClient.cancelSupply(transId);
                assertTrue(canceled);
            }
        } catch (Exception e) {
            fail();
        }
    }


    /**
     * Turning to payment service #3
     */
    @Test
    public void testHandShake_Valid(){
        try {
            boolean check = purchaseClient.handShake();
            assertTrue(check);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testPay_Valid(){
        try{
            boolean check = purchaseClient.handShake();
            if(check){
                int transId = purchaseClient.pay("2222333344445555", 4, 2021, "Israel Israelovice", 262, 20444444);
                assertTrue(transId >= 10000);
            }
        } catch (Exception e){
            fail();
        }
    }

    @Test
    public void testPay_NoHandShaken(){
        try{
            int transId = purchaseClient.pay("2222333344445555", 4, 2021, "Israel Israelovice", 262, 20444444);
            assertFalse(transId >= 10000);
        } catch (Exception e){
            fail();
        }
    }

    @Test
    public void testCancelPay_Valid(){
        try{
            boolean check = purchaseClient.handShake();
            if(check){
                int transId = purchaseClient.pay("2222333344445555", 4, 2021, "Israel Israelovice", 262, 20444444);
                boolean paid = transId >= 10000;
                if(paid){
                    boolean canceled = purchaseClient.cancelPay(transId);
                    assertTrue(canceled);
                }
            }
        } catch (Exception e){
            fail();
        }
    }

    @Test
    public void testCancelPay_NotHandShaken(){
        try{
            int transId = 100000;
            boolean canceled = purchaseClient.cancelPay(transId);
            assertFalse(canceled);
        } catch (Exception e){
            fail();
        }
    }

}
