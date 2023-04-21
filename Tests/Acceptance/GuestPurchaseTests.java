package Acceptance;

import ServiceLayer.Objects.CartService;
import ServiceLayer.Objects.CatalogItemService;
import ServiceLayer.Objects.StoreService;
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
        StoreService storeInfo = this.getStoreInfo(store2Id);
        //assertEquals(storeInfo.getStoreName(), "Store2");
    }

    @Test
    public void getStoreInfoStoreClosed(){
        StoreService storeInfo = this.getStoreInfo(store2ClosedId);
        assertNull(storeInfo);
    }

    @Test
    public void getStoreInfoWrongId(){
        StoreService storeInfo = this.getStoreInfo(-1);
        assertNull(storeInfo);
    }


    /**
     * Search items #12
     */
    @Test
    public void searchItemsValid(){
        List<String> filters= new ArrayList<>();
        filters.add("Dairy");
        List<CatalogItemService> itemsFound = this.searchItems("Milk", filters);
        assertEquals(itemsFound.get(0).getItemName(), "Item1");
    }

    @Test
    public void searchItemsNoMatch(){
        List<String> filters= new ArrayList<>();
        filters.add("filter1");
        filters.add("filter2");
        List<CatalogItemService> itemsFound = this.searchItems("NotExistItemName", filters);
        assertEquals(0, itemsFound.size());
    }

    /**
     * Add to Basket #13
     */
    @Test
    public void addToBasketValid(){
        boolean added = this.addItemToBasket(user1GuestId, store2Id, item11Id, 10);
        assertTrue(added);

        CartService cart = this.getCart(user1GuestId);
        boolean hasItem = cart.getBasketOfStore(store2Id).hasItem(item11Id);
        assertTrue(hasItem);
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
        CartService cart = this.getCart(user1GuestId);
        assertTrue(cart.getBasketOfStore(store2Id).hasItem(item1Id));
    }

    @Test
    public void showCartUserNotExist(){
        CartService cart = this.getCart(userNotExistId);
        assertNull(cart);
    }

    @Test
    public void showCartNotLoggedInUser(){
        CartService cart = this.getCart(user3NotLoggedInId);
        assertNull(cart);
    }





}
