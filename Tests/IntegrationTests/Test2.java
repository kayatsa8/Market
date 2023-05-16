package IntegrationTests;

import BusinessLayer.CartAndBasket.Cart;
import BusinessLayer.MarketMock;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;
import BusinessLayer.Stores.StoreFacade;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.UserFacade;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;



// Focused on actions while login and logout
public class Test2 {
    static StoreFacade storeFacade;
    static UserFacade userFacade;
    static RegisteredUser founder1;
    static RegisteredUser storeOwner1;
    static RegisteredUser storeManager1;
    static RegisteredUser noRole;
    static CatalogItem item1;
    static Store store1;
    static MarketMock market;

    @BeforeClass
    public static void setUp() throws Exception {
        market = new MarketMock();
        storeFacade = market.getStoreFacade();
        userFacade = market.getUserFacade();
        int id1 = market.register("userName1", "password1");
        int id2 = market.register("userName2", "password2");
        int id3 = market.register("userName3", "password3");
        int id4 = market.register("userName4", "password4");
        founder1 = userFacade.getRegisteredUser(id1);
        storeOwner1 = userFacade.getRegisteredUser(id2);
        storeManager1 = userFacade.getRegisteredUser(id3);
        noRole = userFacade.getRegisteredUser(id4);
        market.login("userName1", "password1");
        market.login("userName2", "password2");
        market.login("userName3", "password3");
        market.login("userName4", "password4");
        int storeID = market.addStore(founder1.getId(), "storeName1");
        store1 = market.getStoreInfo(storeID);
        market.addOwner(founder1.getId(), id2, storeID);
        market.addManager(founder1.getId(), id3, storeID);
        item1 = market.addItemToStore(storeID, "item1", 49.90, "Clothing", 0.2);
        market.addItemAmount(storeID, item1.getItemID(), 20);
    }


    @Test
    public void test2(){
        int storeID = store1.getStoreID();
        int founderID = founder1.getId();
        int noRoleID = noRole.getId();
        int item1ID = item1.getItemID();

        //close store - SUCCESS
        try {
            boolean closeResult = market.closeStore(founderID, storeID);
            assertTrue("returned false, because store was already closed for some reason", closeResult);
        } catch (Exception e) {
            fail("ERROR: " + e.getMessage());
        }
        //add item of closed store to cart - FAIL
        try {
            market.addItemToCart(noRoleID, storeID, item1ID, 3);
            fail("ERROR: should have thrown an exception");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().equals("Error: Can't add item to cart from unopened store"));
        }
        //open store - SUCCESS
        try {
            boolean openResult = market.reopenStore(founderID, storeID);
            assertTrue("returned false, because store was already opened for some reason", openResult);
        } catch (Exception e) {
            fail("ERROR: " + e.getMessage());
        }
        //add non-existing item to cart - FAIL
        try {
            market.addItemToCart(noRoleID, storeID, -1, 3);
            fail("ERROR: should have thrown an exception");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().equals("ERROR: Basket:: the item ID you entered does not exist in the given store"));
        }
        //add item to cart - SUCCESS
        try {
            Cart noRoleCart = noRole.getCart();
            assertTrue(noRoleCart.getItemsInBasket("storeName1").size() == 0);
            noRoleCart = market.addItemToCart(noRoleID, storeID, item1ID, 3);
            assertTrue(noRoleCart.getItemsInBasket("storeName1").size() == 1);
        } catch (Exception e) {
            fail("ERROR: " + e.getMessage());
        }
        //buy cart with basket of closed store - FAIL
        try {
            market.closeStore(founderID, storeID);
            market.buyCart(noRoleID, "Be'er Sheva");
            fail("ERROR: should have thrown an exception");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "You can't buy items from a closed store");
        }
        //buy cart - SUCCESS
        try {
            market.reopenStore(founderID, storeID);
            market.buyCart(noRoleID, "Be'er Sheva");
            assertTrue("Item amount hasn't decreased from 20 to 17 after buying 3", store1.getItemAmount(item1ID) == 17);
        } catch (Exception e) {
            fail("ERROR: " + e.getMessage());
        }
    }
}