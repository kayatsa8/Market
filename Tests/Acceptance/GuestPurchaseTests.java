package Acceptance;

import Globals.FilterValue;
import Globals.FilterValue.*;
import Globals.SearchBy;
import Globals.SearchFilter;
import ServiceLayer.Objects.CartService;
import ServiceLayer.Objects.CatalogItemService;
import ServiceLayer.Objects.StoreService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class GuestPurchaseTests extends ProjectTest{


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
     * Get Store info #11
     * #18 is the same
     */
    @Test
    public void getStoreInfoValid(){
        StoreService storeInfo = this.getStoreInfo(store2Id);
        assertEquals(storeInfo.getStoreName(), "Store2");
        assertTrue(storeInfo.getStoreId() >= 0);
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
    public void searchItemsByKeyWord_Valid(){
        addItemToStoreForTests(store2Id, "Bread", 10, "Kitchen", 10);
        addItemToStoreForTests(store2Id, "Bread2", 10, "Kitchen", 10);
        addItemToStoreForTests(store2Id, "Meat2", 10, "Kitchen", 10);
        String keyWords = "Bread";

        List<CatalogItemService> itemsFound = this.searchItems(keyWords, SearchBy.KEY_WORD, null);

        boolean breadExists = false;
        for(CatalogItemService item: itemsFound){
            if(item.getItemName().contains("Bread")){
                breadExists = true;
            }
        }
        assertTrue(breadExists);
    }

    @Test
    public void searchItemsByFilter_Valid(){
        addItemToStoreForTests(store2Id, "Bread", 10, "Clothing", 10);
        addItemToStoreForTests(store2Id, "Bread2", 10, "Kitchen", 10);
        addItemToStoreForTests(store2Id, "Meat2", 10, "Sports", 10);

        HashMap<SearchFilter, FilterValue> filters = new HashMap<>();
        //filters.put(SearchFilter.CATEGORY, Fi);
        List<CatalogItemService> itemsFound = this.searchItems("", SearchBy.CATEGORY, filters);
        assertEquals(itemsFound.get(0).getItemName(), "Item1");

        //Do this test? How to do the filters with the FilterValue
        assertTrue(false);
    }


    @Test
    public void searchItemsNoMatch(){
        String keyWords = "Shoe";
        List<CatalogItemService> itemsFound = this.searchItems(keyWords, SearchBy.KEY_WORD, null);
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
        int item12Id = addItemToStoreForTests(store2Id, "Name11", 10, "Kitchen", 100);
        CartService cart = this.addItemToBasket(user1GuestId, store2Id, item12Id, -9);
        boolean added = cart.getBasketOfStore(store2Id).hasItem(item12Id);
        assertFalse(added);
    }

    @Test
    public void addToBasketItemNotInStore(){
        addItemToStoreForTests(store4Id, "NameDD",10, "Kitchen", 10);
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
