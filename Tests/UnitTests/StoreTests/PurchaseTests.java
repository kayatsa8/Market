//package UnitTests.StoreTests;
//
//import BusinessLayer.Market;
//import BusinessLayer.Stores.CatalogItem;
//import BusinessLayer.Stores.Store;
//import BusinessLayer.Stores.StoreFacade;
//import BusinessLayer.Users.RegisteredUser;
//import BusinessLayer.Users.UserFacade;
//import org.junit.BeforeClass;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//import static org.junit.Assert.*;
//
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class PurchaseTests {
//    static Market market;
//    static StoreFacade storeFacade;
//    static UserFacade userFacade;
//    static RegisteredUser founder1;
//    static RegisteredUser storeOwner1;
//    static RegisteredUser storeManager1;
//    static RegisteredUser noRole;
//    static Store store1;
//    static Store store2;
//    static Store store3;
//
//    @BeforeClass
//    public static void setUp() throws Exception {
//        market = Market.getInstance();
//        storeFacade = market.getStoreFacade();
//        userFacade = market.getUserFacade();
//        int id9 = market.register("userName9", "password9");
//        int id10 = market.register("userName10", "password10");
//        int id11 = market.register("userName11", "password11");
//        int id12 = market.register("userName12", "password12");
//        founder1 = userFacade.getUser(id9);
//        storeOwner1 = userFacade.getUser(id10);
//        storeManager1 = userFacade.getUser(id11);
//        noRole = userFacade.getUser(id12);
//        int storeID1 = market.addStore(founder1.getId(), "storeName1");
//        store1 = market.getStoreInfo(storeID1);
//        int storeID2 = market.addStore(founder1.getId(), "storeName2");
//        store2 = market.getStoreInfo(storeID2);
//        int storeID3 = market.addStore(founder1.getId(), "storeName3");
//        store3 = market.getStoreInfo(storeID3);
//        market.addOwner(founder1.getId(), id10, storeID1);
//        market.addManager(founder1.getId(), id11, storeID1);
//        market.addItemToStore(storeID1, "item1", 10, "category1");
//        market.addItemToStore(storeID1, "item2", 20, "category2");
//        market.addItemToStore(storeID1, "item5", 50, "category5");
//        market.addItemToStore(storeID2, "item1", 10, "category1");
//        market.addItemToStore(storeID2, "item2", 20, "category2");
//        market.addItemToStore(storeID2, "item3", 30, "category3");
//        market.addItemToStore(storeID2, "item4", 40, "category4");
//    }
//
//    @Test
//    public void aSaveItemsForUpcoming(){
//        try {
//
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
//}
