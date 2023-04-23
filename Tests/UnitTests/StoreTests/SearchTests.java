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
        int id9 = market.register("userName9", "password9");
        int id10 = market.register("userName10", "password10");
        int id11 = market.register("userName11", "password11");
        int id12 = market.register("userName12", "password12");
        founder1 = userFacade.getRegisteredUser(id9);
        storeOwner1 = userFacade.getRegisteredUser(id10);
        storeManager1 = userFacade.getRegisteredUser(id11);
        noRole = userFacade.getRegisteredUser(id12);
        int storeID1 = market.addStore(founder1.getId(), "storeName1");
        store1 = market.getStoreInfo(storeID1);
        int storeID2 = market.addStore(founder1.getId(), "storeName2");
        store2 = market.getStoreInfo(storeID2);
        int storeID3 = market.addStore(founder1.getId(), "storeName3");
        store3 = market.getStoreInfo(storeID3);
        market.addOwner(founder1.getId(), id10, storeID1);
        market.addManager(founder1.getId(), id11, storeID1);
        market.addItemToStore(storeID1, "item1", 10, "category1");
        market.addItemToStore(storeID1, "item2", 20, "category2");
        market.addItemToStore(storeID1, "item5", 50, "category5");
        market.addItemToStore(storeID2, "item1", 10, "category1");
        market.addItemToStore(storeID2, "item2", 20, "category2");
        market.addItemToStore(storeID2, "item3", 30, "category3");
        market.addItemToStore(storeID2, "item4", 40, "category4");
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
            assertTrue("All stores together should return 7 items", allItems.keySet().size()==7);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getSearchedItemsByItemName(){
        try {
            Map<CatalogItem, Boolean> items1 = store1.getCatalog("item2", SearchBy.ITEM_NAME, new HashMap<>());
            Map<CatalogItem, Boolean> items2 = store2.getCatalog("category2", SearchBy.ITEM_NAME, new HashMap<>());
            Map<CatalogItem, Boolean> items3 = store3.getCatalog("item2", SearchBy.ITEM_NAME, new HashMap<>());
            Map<CatalogItem, Boolean> fromAllStores = storeFacade.getCatalog("item2", SearchBy.ITEM_NAME, new HashMap<>());
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
            Map<CatalogItem, Boolean> items1 = store1.getCatalog("category2", SearchBy.CATEGORY, new HashMap<>());
            Map<CatalogItem, Boolean> items2 = store2.getCatalog("item2", SearchBy.CATEGORY, new HashMap<>());
            Map<CatalogItem, Boolean> items3 = store3.getCatalog("category2", SearchBy.CATEGORY, new HashMap<>());
            Map<CatalogItem, Boolean> fromAllStores = storeFacade.getCatalog("category2", SearchBy.CATEGORY, new HashMap<>());
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
            Map<CatalogItem, Boolean> items1 = store1.getCatalog("item2", SearchBy.KEY_WORD, new HashMap<>());
            Map<CatalogItem, Boolean> items2 = store2.getCatalog("cateGORY2, category3", SearchBy.KEY_WORD, new HashMap<>());
            Map<CatalogItem, Boolean> items3 = store3.getCatalog("item1, category1", SearchBy.KEY_WORD, new HashMap<>());
            Map<CatalogItem, Boolean> fromAllStores = storeFacade.getCatalog("ITeM4, cAtEgOrY1", SearchBy.KEY_WORD, new HashMap<>());
            assertTrue("Store 1 should return 1 items", items1.keySet().size()==1);
            assertTrue("Store 2 should return 1 items", items2.keySet().size()==2);
            assertTrue("Store 3 should return 0 items", items3.keySet().size()==0);
            assertTrue("All stores together should return 3 items", fromAllStores.keySet().size()==3);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
