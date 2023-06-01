package UnitTests;

import BusinessLayer.Market;
import BusinessLayer.Stores.StoreFacade;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.UserFacade;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;

import static BusinessLayer.Stores.StoreStatus.OPEN;
import static BusinessLayer.Stores.StoreStatus.PERMANENTLY_CLOSE;
import static org.junit.Assert.*;

public class SystemManagerTest {
    static Market market;
    static String user1Name = "user1A";
    static String user2Name = "user2A";
    static String adminName = "admin";
    static RegisteredUser user;
    static RegisteredUser secondUser;
    static StoreFacade sf;
    static UserFacade uf;
    static int store1;
    static int store2;
    static int store3;
    static int user1;
    static int user2;
    static int adminID;

    @BeforeClass
    public static void setUp() throws Exception {
        market = Market.getInstance();
        sf = market.getStoreFacade();
        uf = market.getUserFacade();
        adminID = uf.getUserByName(adminName).getId();
        String addressOk="addressOk";
        LocalDate bDayOk= LocalDate.of(2022, 7, 11);
        try {
            user1 = market.register(user1Name, user1Name+user1Name,addressOk,bDayOk);
            user2 = market.register(user2Name, user2Name+user2Name,addressOk,bDayOk);
            market.login(user1Name, user1Name+user1Name);
            market.login(user2Name, user2Name+user2Name);
            user = market.getUserFacade().getRegisteredUser(user1);
            secondUser = market.getUserFacade().getRegisteredUser(user2);
            store1 = market.addStore(user.getId(), "test store 1");
            store2 = market.addStore(user.getId(), "test store 2");
            store3 = market.addStore(secondUser.getId(), "test store 3");
            market.addOwner(secondUser.getId(), user.getId(), secondUser.getStoresIOwn().keySet().iterator().next());
        }
        catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void startWithAnAdmin() {
        assertTrue("System should have created an admin", market.getSystemManagerMap().size()>0);
    }

    @Test
    public void closeStorePermanently() {
        int storeToDelete = store1;
        try {
            market.closeStorePermanently(user1, storeToDelete);
            fail("Only admin should be able to close store");
        }
        catch (Exception e) {

        }
        try {
            boolean success = market.closeStorePermanently(adminID, storeToDelete);
            assertTrue("Store closed permanently", success);
            assertEquals("Store should have been removed from store facade", PERMANENTLY_CLOSE, sf.getStore(storeToDelete).getStoreStatus());
            assertNull("User should not see anymore that he owns the store", user.getStoreIOwn(storeToDelete));
            assertTrue("Store should remove all owners and managers", sf.getStore(storeToDelete).getStoreOwners().isEmpty() && sf.getStore(storeToDelete).getStoreManagers().isEmpty());
        }
        catch (Exception e) {
            fail(e.getMessage()+" caused us to fail to close store");
        }
    }

    @Test
    public void removeUserShouldFailOwnsStore() {
        int[] storeToDelete = {store1, store2};
        try {
            if (market==null)
                fail("market is null!");

            market.removeUser(adminID, user1);
            fail("Cannot remove User who owns or manages a store");
//            for (int store : storeToDelete)
//                assertEquals("User's Store should be closed permanently because he is founder", PERMANENTLY_CLOSE, sf.getStore(store).getStoreStatus());
//            assertEquals("Store 3 should not have been permanently closed bc user was not founder", OPEN, sf.getStore(store3).getStoreStatus());
//            assertNull("User should not be in userfacade any more", uf.getUser(user1));
        }
        catch (Exception e) {
            assertTrue(true);

        }
        //ensure traces of user are gone in store
    }
    @Test
    public void removeUserShouldFail() {
        int[] storeToDelete = {store1, store2};
        try {
            market.removeUser(user1, user2);
            fail("Only admin should be able to remove user");
        }
        catch (Exception e) {
            assertTrue(e.getMessage() + " caused us to fail to remove user",true);
        }

    }
}