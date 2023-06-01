package UnitTests.StoreTests;

import BusinessLayer.Market;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;
import BusinessLayer.Stores.StoreFacade;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.UserFacade;
import Globals.SearchBy;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SearchTests {
    static Market market;
    static StoreFacade storeFacade;
    static UserFacade userFacade;
    static RegisteredUser founder1;
    static RegisteredUser storeOwner1;
    static RegisteredUser storeManager1;
    static RegisteredUser noRole;
    static Store store1;
    static Store store2;
    static Store store3;

    @BeforeClass
    public static void setUp() throws Exception {
        market = Market.getInstance();
        storeFacade = market.getStoreFacade();
        userFacade = market.getUserFacade();
        String addressOk="addressOk";
        LocalDate bDayOk= LocalDate.of(2022, 7, 11);
        int id9 = market.register("userName9K", "password9",addressOk,bDayOk);
        int id10 = market.register("userName10K", "password10",addressOk,bDayOk);
        int id11 = market.register("userName11K", "password11",addressOk,bDayOk);
        int id12 = market.register("userName12K", "password12",addressOk,bDayOk);
        market.login("userName9K", "password9");
        market.login("userName10K", "password10");
        market.login("userName11K", "password11");
        market.login("userName12K", "password12");
        founder1 = userFacade.getRegisteredUser(id9);
        storeOwner1 = userFacade.getRegisteredUser(id10);
        storeManager1 = userFacade.getRegisteredUser(id11);
        noRole = userFacade.getRegisteredUser(id12);
        int storeID1 = market.addStore(founder1.getId(), "storeName1K");
        store1 = market.getStoreInfo(storeID1);
        int storeID2 = market.addStore(founder1.getId(), "storeName2K");
        store2 = market.getStoreInfo(storeID2);
        int storeID3 = market.addStore(founder1.getId(), "storeName3K");
        store3 = market.getStoreInfo(storeID3);
        market.addOwner(founder1.getId(), id10, storeID1);
        market.addManager(founder1.getId(), id11, storeID1);
        market.addItemToStore(storeID1, "item1K", 10, "category1k", 1);
        market.addItemToStore(storeID1, "item2K", 20, "category2K", 1);
        market.addItemToStore(storeID1, "item5K", 50, "category5K", 1);
        market.addItemToStore(storeID2, "item1K", 10, "category1K", 1);
        market.addItemToStore(storeID2, "item2K", 20, "category2K", 1);
        market.addItemToStore(storeID2, "item3K", 30, "category3K", 1);
        market.addItemToStore(storeID2, "item4K", 40, "category4K", 1);
    }

    @Test
    public void getAllItems(){
        try {
            Map<CatalogItem, Boolean> items1 = store1.getCatalog();
            Map<CatalogItem, Boolean> items2 = store2.getCatalog();
            Map<CatalogItem, Boolean> items3 = store3.getCatalog();
            Map<CatalogItem, Boolean> allItems = storeFacade.getCatalog();
            assertTrue("Store 1 should return 3 items", items1.keySet().size()==3);
            assertTrue("Store 2 should return 4 items", items2.keySet().size()==4);
            assertTrue("Store 3 should return 0 items", items3.keySet().size()==0);
            assertTrue("All stores together should return 7 items", allItems.keySet().size()>=7);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getSearchedItemsByItemName(){
        try {
            Map<CatalogItem, Boolean> items1 = store1.getCatalog("item2K", SearchBy.ITEM_NAME, new HashMap<>());
            Map<CatalogItem, Boolean> items2 = store2.getCatalog("category2K", SearchBy.ITEM_NAME, new HashMap<>());
            Map<CatalogItem, Boolean> items3 = store3.getCatalog("item2K", SearchBy.ITEM_NAME, new HashMap<>());
            Map<CatalogItem, Boolean> fromAllStores = storeFacade.getCatalog("item2K", SearchBy.ITEM_NAME, new HashMap<>());
            assertTrue("Store 1 should return 1 items", items1.keySet().size()==1);
            assertTrue("Store 2 should return 0 items", items2.keySet().size()==0);
            assertTrue("Store 3 should return 0 items", items3.keySet().size()==0);
            assertTrue("All stores together should return 2 items", fromAllStores.keySet().size()==2);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void getSearchedItemsByItemCategory(){
        try {
            Map<CatalogItem, Boolean> items1 = store1.getCatalog("category2K", SearchBy.CATEGORY, new HashMap<>());
            Map<CatalogItem, Boolean> items2 = store2.getCatalog("item2K", SearchBy.CATEGORY, new HashMap<>());
            Map<CatalogItem, Boolean> items3 = store3.getCatalog("category2K", SearchBy.CATEGORY, new HashMap<>());
            Map<CatalogItem, Boolean> fromAllStores = storeFacade.getCatalog("category2K", SearchBy.CATEGORY, new HashMap<>());
            assertTrue("Store 1 should return 1 items", items1.keySet().size()==1);
            assertTrue("Store 2 should return 0 items", items2.keySet().size()==0);
            assertTrue("Store 3 should return 0 items", items3.keySet().size()==0);
            assertTrue("All stores together should return 2 items", fromAllStores.keySet().size()==2);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void getSearchedItemsByKeywords(){
        try {
            Map<CatalogItem, Boolean> items1 = store1.getCatalog("item2k", SearchBy.KEY_WORD, new HashMap<>());
            Map<CatalogItem, Boolean> items2 = store2.getCatalog("cateGORY2k, category3K", SearchBy.KEY_WORD, new HashMap<>());
            Map<CatalogItem, Boolean> items3 = store3.getCatalog("item1K, category1K", SearchBy.KEY_WORD, new HashMap<>());
            Map<CatalogItem, Boolean> fromAllStores = storeFacade.getCatalog("ITeM4k, cAtEgOrY1k", SearchBy.KEY_WORD, new HashMap<>());
            assertTrue("Store 1 should return 1 items", items1.keySet().size()==1);
            assertTrue("Store 2 should return 1 items", items2.keySet().size()==2);
            assertTrue("Store 3 should return 0 items", items3.keySet().size()==0);
            assertTrue("All stores together should return 3 items", fromAllStores.keySet().size()==3);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
