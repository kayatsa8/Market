package UnitTests.StoreTests;

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

import java.time.LocalDate;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemsManagementTests {
    static Market market;
    static StoreFacade storeFacade;
    static UserFacade userFacade;
    static RegisteredUser founder1;
    static RegisteredUser storeOwner1;
    static RegisteredUser storeManager1;
    static RegisteredUser noRole;
    static Store store1;
    static CatalogItem item1;

    @BeforeClass
    public static void setUp() throws Exception {
        market = Market.getInstance();
        storeFacade = market.getStoreFacade();
        userFacade = market.getUserFacade();
        String addressOk="addressOk";
        LocalDate bDayOk= LocalDate.of(2022, 7, 11);
        int id5 = market.register("userName5", "password5",addressOk,bDayOk);
        int id6 = market.register("userName6", "password6",addressOk,bDayOk);
        int id7 = market.register("userName7", "password7",addressOk,bDayOk);
        int id8 = market.register("userName8", "password8",addressOk,bDayOk);
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
    }

    @Test
    public void aAddCatalogItemSuccessfully(){
        try {
            item1 = storeFacade.addCatalogItem(store1.getStoreID(), "Harry Potter Book", 79.90, "Books", 0.8);
            assertNotNull("Item added to store",item1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void bAddCatalogItemUnsuccessfully(){
        try {
            CatalogItem item = storeFacade.addCatalogItem(1656, "Blabla Book", 100, "Books", 0.3);
            fail("Should throw an error for store not exist");
        } catch (Exception e) {
            assertTrue("Store not exist", ("No store with ID: " + 1656).equals(e.getMessage()));
        }
    }

    @Test
    public void cRemoveCatalogItemSuccessfully(){
        try {
            assertNotNull("Item should be found here", store1.getItem(item1.getItemID()));
            CatalogItem item = storeFacade.removeItemFromStore(store1.getStoreID(), item1.getItemID());
            assertNull("Item shouldn't be found here", store1.getItem(item1.getItemID()));
            assertNotNull("Should return non null object", item);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void dRemoveCatalogItemUnsuccessfully(){
        try {
            assertNull("Item shouldn't be found here", store1.getItem(item1.getItemID()));
            CatalogItem item = storeFacade.removeItemFromStore(store1.getStoreID(), item1.getItemID());
            assertNull("Returned item should be null" ,item);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
