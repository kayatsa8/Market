package Acceptance;

import BusinessLayer.Stores.Category;
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
        assertEquals(storeInfo.getStoreName(), "Store2");
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
        CartService cart = this.addItemToBasket(user1GuestId, store2Id, item11Id, 10);
        boolean added = cart.getBasketOfStore(store2Id).hasItem(item11Id);
        assertTrue(added);
    }

    @Test
    public void addToBasketStoreClosed(){
        CartService cart = this.addItemToBasket(user1GuestId, store2ClosedId, item1Id, 19);
        boolean added = cart.getBasketOfStore(store2ClosedId).hasItem(item1Id);
        assertFalse(added);
    }

    @Test
    public void addToBasketNegativeAmount(){
        int item12Id = addItemToStoreForTests(store2Id, "Name11", 10, Category.Kitchen, 100);
        CartService cart = this.addItemToBasket(user1GuestId, store2Id, item12Id, -9);
        boolean added = cart.getBasketOfStore(store2Id).hasItem(item12Id);
        assertFalse(added);
    }

    @Test
    public void addToBasketItemNotInStore(){
        addItemToStoreForTests(store4Id, "NameDD",10, Category.Books, 10);
        CartService cart = this.addItemToBasket(user1GuestId, store2Id, store4Id, 10);
        boolean added = cart.getBasketOfStore(store2Id).hasItem(item2Id);
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
