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
        //storeFacade = new StoreFacade();
        //userFacade = new UserFacade();
        int id9 = market.register("userName9K", "password9");
        int id10 = market.register("userName10K", "password10");
        int id11 = market.register("userName11K", "password11");
        int id12 = market.register("userName12K", "password12");
        //int id9 = userFacade.registerUser("userName9", "password9");
        //int id10 = userFacade.registerUser("userName10", "password10");
        //int id11 = userFacade.registerUser("userName11", "password11");
        //int id12 = userFacade.registerUser("userName12", "password12");
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
        //store1 = storeFacade.addStore(founder1.getId(), "storeName1");
        //userFacade.addStore(founder1.getId(), store1);
        //store2 = storeFacade.addStore(founder1.getId(), "storeName2");
        //userFacade.addStore(founder1.getId(), store2);
        //store3 = storeFacade.addStore(founder1.getId(), "storeName3");
        //userFacade.addStore(founder1.getId(), store3);
        market.addOwner(founder1.getId(), id10, storeID1);
        market.addManager(founder1.getId(), id11, storeID1);
        //userFacade.addOwner(founder1.getId(), id10, store1.getStoreID());
        //userFacade.addManager(founder1.getId(), id11, store1.getStoreID());
        market.addItemToStore(storeID1, "item1K", 10, "category1k");
        market.addItemToStore(storeID1, "item2K", 20, "category2K");
        market.addItemToStore(storeID1, "item5K", 50, "category5K");
        market.addItemToStore(storeID2, "item1K", 10, "category1K");
        market.addItemToStore(storeID2, "item2K", 20, "category2K");
        market.addItemToStore(storeID2, "item3K", 30, "category3K");
        market.addItemToStore(storeID2, "item4K", 40, "category4K");
        //storeFacade.addCatalogItem(store1.getStoreID(), "item1", 10, "category1");
        //storeFacade.addCatalogItem(store1.getStoreID(), "item2", 20, "category2");
        //storeFacade.addCatalogItem(store1.getStoreID(), "item5", 50, "category5");
        //storeFacade.addCatalogItem(store2.getStoreID(), "item1", 10, "category1");
        //storeFacade.addCatalogItem(store2.getStoreID(), "item2", 20, "category2");
        //storeFacade.addCatalogItem(store2.getStoreID(), "item3", 30, "category3");
        //storeFacade.addCatalogItem(store2.getStoreID(), "item4", 40, "category4");
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
