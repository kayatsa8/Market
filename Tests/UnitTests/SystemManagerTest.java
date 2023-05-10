package UnitTests;

import BusinessLayer.Market;
import BusinessLayer.Stores.StoreFacade;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.UserFacade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static BusinessLayer.Stores.StoreStatus.OPEN;
import static BusinessLayer.Stores.StoreStatus.PERMANENTLY_CLOSE;
import static org.junit.Assert.*;

public class SystemManagerTest {
    Market market;
    String user1 = "user1";
    String user2 = "user2";
    String adminName = "admin";
    RegisteredUser user;
    RegisteredUser secondUser;
    StoreFacade sf;
    UserFacade uf;
    int store1;
    int store2;
    int store3;

    @Before
    public void setUp() throws Exception {
        market = Market.getInstance();
        sf = market.getStoreFacade();
        uf = market.getUserFacade();
        try {
            market.register(user1, user1+user1);
            market.register(user2, user2+user2);
            user = market.getUserFacade().getUser(user1);
            secondUser = market.getUserFacade().getUser(user2);
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
        assertEquals("System should have created an admin", 1, market.getSystemManagerMap().size());
    }

    @Test
    public void closeStorePermanently() {
        int storeToDelete = store1;
        assertThrows("Only admin should be able to close store", RuntimeException.class,
                ()->market.closeStorePermanently(user1, storeToDelete));
        try {
            market.closeStorePermanently(adminName, storeToDelete);
        }
        catch (Exception e) {
            assertTrue(e.getMessage()+" caused us to fail to close store", false);
        }
        assertNull("Store should have been removed from store facade", sf.getStore(storeToDelete));
        assertNull("User should not see anymore that he owns the store", user.getStoreIOwn(storeToDelete));
    }

    @Test
    public void removeUser() {
        int[] storeToDelete = {store1, store2};

        assertThrows("Only admin should be able to remove user", RuntimeException.class,
                ()->market.removeUser(user1, user2));
        try {
            market.removeUser(adminName, user1);
        }
        catch (Exception e) {
            assertTrue(e.getMessage()+" caused us to fail to remove user", false);
        }
        for (int store : storeToDelete)
            assertEquals("User's Store should be closed permanently because he is founder", PERMANENTLY_CLOSE, sf.getStore(store).getStoreStatus());
        assertEquals("Store 3 should not have been permanently closed bc user was not founder", OPEN, sf.getStore(store3).getStoreStatus());
        assertNull("User should not be in userfacade any more", uf.getUser(user1));
        //ensure traces of user are gone in store

    }
}