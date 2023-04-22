//package UnitTests.StoreTests;
//
//import BusinessLayer.Stores.Store;
//import BusinessLayer.Stores.StoreFacade;
//import BusinessLayer.Users.RegisteredUser;
//import BusinessLayer.Users.UserFacade;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
//
//public class SearchTests {
//    static StoreFacade storeFacade;
//    static UserFacade userFacade;
//    static RegisteredUser registeredUser1;
//    static Store store1;
//
//    @BeforeClass
//    public static void setUp() throws Exception {
//        storeFacade=new StoreFacade();
//        userFacade = new UserFacade();
//        int id = userFacade.registerUser("userName1", "password1");
//        registeredUser1 = userFacade.getUser(id);
//        store1 = new Store(1, id, "storeName1");
//    }
//
//    @Test
//    public void createNewStore(){
//        try {
//            //store1.;
//            assertTrue("Registration succeeded",true);
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
//}
