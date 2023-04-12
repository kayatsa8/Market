package Acceptance;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class GuestPurchaseTestBuyCart extends ProjectTest{


    @Override
    public void setUp() {
        super.setUp();
        setUpAllMarket();
    }


    @After
    public void tearDown() {
        //delete stores and delete users from DB
    }


    /**
     * Buy cart #15
     */
    @Test
    public void buyCartValid(){
        boolean added = this.buyCart(user1GuestId, "PaymentDetails");
        assertTrue(added);
    }

    @Test
    public void buyCartWrongPaymentDetails(){
        boolean added = this.buyCart(user1GuestId, "WrongPaymentDetails");
        assertFalse(added);
    }

    @Test
    public void buyCartUserNotLoggedIn(){
        boolean added = this.buyCart(user3NotLoggedInId, "PaymentDetails");
        assertFalse(added);
    }



}
