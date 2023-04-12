package Tests.Acceptance;

import Tests.Objects.TestCartInfo;
import Tests.Objects.TestItemInfo;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GuestPurchaseTests extends ProjectTest{


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
     * Get Store info #11
     * #18 is the same
     */
    @Test
    public void getStoreInfoValid(){
        //Maybe save the information in an object
        String info = this.getStoreInfo(store2Id);
        //assertTrue();
    }

    @Test
    public void getStoreInfoStoreClosed(){
        String info = this.getStoreInfo(store2ClosedId);
        assertEquals("Store is Closed!", info);
    }

    @Test
    public void getStoreInfoWrongId(){
        String info = this.getStoreInfo(-1);
        assertEquals("Store does not exists!", info);
    }


    /**
     * Search items #12
     */
    @Test
    public void searchItemsValid(){
        List<String> filters= new ArrayList<>();
        filters.add("filter1");
        filters.add("filter2");
        List<TestItemInfo> itemsFound = this.searchItems("existItemName", filters);
        //assertEqulas();
    }

    @Test
    public void searchItemsNoMatch(){
        List<String> filters= new ArrayList<>();
        filters.add("filter1");
        filters.add("filter2");
        List<TestItemInfo> itemsFound = this.searchItems("NotExistItemName", filters);
        //assertEqulas();
    }

    /**
     * Add to Basket #13
     */
    @Test
    public void addToBasketValid(){
        boolean added = this.addItemToBasket(user1GuestId, store2Id, item11Id, 10);
        assertTrue(added);
    }

    @Test
    public void addToBasketStoreClosed(){
        boolean added = this.addItemToBasket(user1GuestId, store2ClosedId, item1Id, 19);
        assertFalse(added);
    }

    @Test
    public void addToBasketNegativeAmount(){
        boolean added = this.addItemToBasket(user1GuestId, store2Id, item11Id, -9);
        assertFalse(added);
    }

    @Test
    public void addToBasketItemNotInStore(){
        boolean added = this.addItemToBasket(user1GuestId, store2Id, item2Id, 10);
        assertFalse(added);
    }

    /**
     * Show cart #14
     */
    @Test
    public void showCartValid(){
        TestCartInfo cart = this.showCart(user1GuestId);
        //assertTrue();
    }

    @Test
    public void showCartUserNotExist(){
        TestCartInfo cart = this.showCart(userNotExistId);
        assertNull(cart);
    }

    @Test
    public void showCartNotLoggedInUser(){
        TestCartInfo cart = this.showCart(user3NotLoggedInId);
        assertNull(cart);
    }





}
