package UnitTests.DiscountsAndPolicies;

import BusinessLayer.CartAndBasket.Cart;
import BusinessLayer.Market;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;
import BusinessLayer.Stores.StoreFacade;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.UserFacade;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Calendar;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DiscountsTests {
    static Market market;
    static StoreFacade storeFacade;
    static UserFacade userFacade;
    static RegisteredUser founder1;
    static RegisteredUser storeOwner1;
    static RegisteredUser storeManager1;
    static RegisteredUser noRole;
    static Store store1;
    static CatalogItem item1;
    static CatalogItem item2;


    @BeforeClass
    public static void setUp() throws Exception {
        market = Market.getInstance();
        storeFacade = market.getStoreFacade();
        userFacade = market.getUserFacade();
        int id5 = market.register("userName5", "password5");
        int id6 = market.register("userName6", "password6");
        int id7 = market.register("userName7", "password7");
        int id8 = market.register("userName8", "password8");
        market.login("userName5", "password5");
        market.login("userName6", "password6");
        market.login("userName7", "password7");
        market.login("userName8", "password8");
        founder1 = userFacade.getRegisteredUser(id5);
        storeOwner1 = userFacade.getRegisteredUser(id6);
        storeManager1 = userFacade.getRegisteredUser(id7);
        noRole = userFacade.getRegisteredUser(id8);
        int storeID = market.addStore(founder1.getId(), "storeName1");
        store1 = market.getStoreInfo(storeID);
        market.addOwner(founder1.getId(), id6, storeID);
        market.addManager(founder1.getId(), id7, storeID);
        item1 = market.addItemToStore(storeID, "item1", 10, "Books", 10);
        item2 = market.addItemToStore(storeID, "item2", 10, "Clothing", 10);
        market.addItemAmount(storeID, item1.getItemID(), 50);

    }

    @Test
    public void addVisibleCategoryDiscount(){
        try {
            Calendar date = Calendar.getInstance();
            date.add(5, 1);
            int vcdID = market.addVisibleCategoryDiscount(store1.getStoreID(), "Books", 0.2, date);
            assertTrue(vcdID == 0);
            assertTrue(store1.getStoreVisibleDiscounts().get(vcdID).isDiscountApplyForItem(item1.getItemID(), item1.getCategory()));
            assertFalse(store1.getStoreDiscounts().get(vcdID).isDiscountApplyForItem(item2.getItemID(), item2.getCategory()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void addPurchasePolicy(){
        try {
            String ppbtprString = market.addPurchasePolicyBasketTotalPriceRule(store1.getStoreID(), 50);
            market.addItemToCart(noRole.getId(), store1.getStoreID(), item1.getItemID(), 4);
            market.buyCart(noRole.getId(), "New York");
            fail("Should have thrown an error");
        } catch (Exception e) {
            assertEquals("ERROR: Basket::checkIfPurchaseIsValid: the purchase is not valid!", e.getMessage());
        }
        try {
            market.changeItemQuantityInCart(noRole.getId(), store1.getStoreID(), item1.getItemID(), 5);
            Cart cart = market.buyCart(noRole.getId(), "New York");
            assertTrue(String.valueOf(cart.getBaskets().size()),cart.getBaskets().size() == 0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
