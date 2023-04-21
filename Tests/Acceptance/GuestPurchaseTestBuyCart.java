package Acceptance;

import ServiceLayer.Objects.CartService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GuestPurchaseTestBuyCart extends ProjectTest{


    public static boolean doneSetUp = false;

    @Before
    public void setUp() {
        super.setUp();
        if(!doneSetUp) {
            setUpAllMarket();
            doneSetUp = true;
        }
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

        CartService afterCart = this.getCart(user1GuestId);
        //assertTrue(afterCart.isEmpty());

        //check if cartService has isEmpty() func
        assertTrue(false);
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
