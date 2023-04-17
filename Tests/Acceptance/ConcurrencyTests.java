package Acceptance;

import BusinessLayer.Stores.Category;
import ServiceLayer.Objects.CartService;
import ServiceLayer.Objects.StoreService;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConcurrencyTests extends ProjectTest{

    @Override
    public void setUp() {
        super.setUp();
        setUpAllMarket();
    }


    @After
    public void tearDown() {
        //delete stores and delete users from DB
    }

    boolean user2Bought = true, user4Bought = true, user2Erased = true;

    /**
     * 2 Users trying to buy the last item available
     */
    @Test
    public void twoUsersBuyingLastItem(){
        //Store2 as only 1 item21 !!
        int item21Id = this.addCatalogItem(store2Id, "Item21", 10, Category.Kitchen);
        addItemAmount(store2Id, item21Id, 1);

        addItemToBasket(user2LoggedInId, store2Id, item21Id, 1);
        addItemToBasket(user4LoggedInId, store2Id, item21Id, 1);


        Thread thread1 = new Thread("User2") {
            public void run(){
                user2Bought = buyCart(user2LoggedInId, "Details");
            }
        };

        Thread thread2 = new Thread("User4") {
            public void run(){
                user4Bought = buyCart(user4LoggedInId, "Details");
            }
        };

        thread1.start();
        thread2.start();

        assertNotEquals(user4Bought, user2Bought);
        StoreService info = getStoreInfo(store2Id);
        //boolean exists = info.itemInStore(item21Id);
        //assertFalse(exists);
        //uncomment above, checks if item is in store
        assertTrue(false);


        CartService cart2 = getCart(user2LoggedInId);
        CartService cart4 = getCart(user4LoggedInId);
        boolean cart2Empty = true, cart4Empty = true;
        //cart2Empty = cart2.isEmpty();
        //cart4Empty = cart4.isEmpty();
        assertNotEquals(cart2Empty, cart4Empty);
    }


    /**
     * user trying to buy an item when a Store Owner deletes it
     */
    @Test
    public void buyingAndDeletingTheSameTime(){
        int item22Id = this.addCatalogItem(store2Id, "Item22", 10, Category.Kitchen);
        addItemAmount(store2Id, item22Id, 10);

        addItemToBasket(user4LoggedInId, store2Id, item22Id, 1);

        Thread thread1 = new Thread("User2") {
            public void run(){
                user4Bought = buyCart(user4LoggedInId, "Details");
            }
        };

        Thread thread2 = new Thread("User4") {
            public void run(){
                user2Erased = removeItemFromStore(store2Id, item22Id);
            }
        };

        thread1.start();
        thread2.start();
        assertNotEquals(user4Bought, user2Erased);
        StoreService info = getStoreInfo(store2Id);

        CartService cart4 = getCart(user4LoggedInId);
        boolean stillInCart = cart4.getBasketOfStore(store2Id).hasItem(item22Id);

        boolean itemExists = stillInCart;  //default for compilation
        //itemExists = info.itemInStore(item22Id);
        assertNotEquals(stillInCart, itemExists);
    }


    /**
     * 2 Store Owners try to put same user as manager
     */
    @Test
    public void simultaneouslyAddManager(){
        Thread thread1 = new Thread("User2") {
            public void run(){
                user4Bought = defineStoreManager(store2Id, user5ManagerOwnerOfStore2ToBeRemoved, user4LoggedInId);
            }
        };

        Thread thread2 = new Thread("User4") {
            public void run(){
                user2Bought = defineStoreManager(store2Id, user6ManagerOwnerOfStore2, user4LoggedInId);
            }
        };

        thread1.start();
        thread2.start();
        assertNotEquals(user4Bought, user2Bought);

        boolean manager = checkIfStoreManager(user4LoggedInId, store2Id);
        assertTrue(manager);
    }


}
