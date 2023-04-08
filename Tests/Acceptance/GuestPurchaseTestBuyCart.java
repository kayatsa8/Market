package Tests.Acceptance;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class GuestPurchaseTestBuyCart extends ProjectTest{


    @Override
    public void setUp() {
        super.setUp();
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
        Boolean added = this.buyCart(user1GuestId, "PaymentDetails");
        assertTrue(added);
    }

    @Test
    public void buyCartWrongPaymentDetails(){
        Boolean added = this.buyCart(user1GuestId, "WrongPaymentDetails");
        assertFalse(added);
    }

    @Test
    public void buyCartUserNotLoggedIn(){
        Boolean added = this.buyCart(user3NotLoggedInId, "PaymentDetails");
        assertFalse(added);
    }



}
