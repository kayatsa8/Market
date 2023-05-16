package UnitTests.StoreTests;

import BusinessLayer.Market;
import BusinessLayer.StorePermissions.StoreEmployees;
import BusinessLayer.Stores.Store;
import BusinessLayer.Stores.StoreFacade;
import BusinessLayer.Stores.StoreStatus;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.UserFacade;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StoreStatusTests {
    static StoreFacade storeFacade;
    static UserFacade userFacade;
    static RegisteredUser founder1;
    static RegisteredUser storeOwner1;
    static RegisteredUser storeManager1;
    static RegisteredUser noRole;
    static Store store1;
    static Market market;

    @BeforeClass
    public static void setUp() throws Exception {
        market = Market.getInstance();
        storeFacade = market.getStoreFacade();
        userFacade = market.getUserFacade();
        int id1 = market.register("userName1", "password1");
        int id2 = market.register("userName2", "password2");
        int id3 = market.register("userName3", "password3");
        int id4 = market.register("userName4", "password4");
        market.login("userName1", "password1");
        market.login("userName2", "password2");
        market.login("userName3", "password3");
        market.login("userName4", "password4");
        founder1 = userFacade.getRegisteredUser(id1);
        storeOwner1 = userFacade.getRegisteredUser(id2);
        storeManager1 = userFacade.getRegisteredUser(id3);
        noRole = userFacade.getRegisteredUser(id4);
        int storeID = market.addStore(founder1.getId(), "storeName1");
        store1 = market.getStoreInfo(storeID);
        market.addOwner(founder1.getId(), id2, storeID);
        market.addManager(founder1.getId(), id3, storeID);
    }

    @Test
    public void aCloseStoreWithStoreOwner(){
        try {
            store1.closeStore(storeOwner1.getId());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue("Store should remain OPEN", store1.getStoreStatus()==StoreStatus.OPEN);
            assertTrue("Only the founder of the store can close it", "Only the founder of the store can close it".equals(e.getMessage()));
        }
    }
    @Test
    public void bCloseStoreWithStoreManager(){
        try {
            store1.closeStore(storeManager1.getId());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue("Store should remain OPEN", store1.getStoreStatus()==StoreStatus.OPEN);
            assertTrue("Only the founder of the store can close it", "Only the founder of the store can close it".equals(e.getMessage()));
        }
    }
    @Test
    public void cCloseStoreWithNoRoleUser(){
        try {
            store1.closeStore(noRole.getId());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue("Store should remain OPEN", store1.getStoreStatus()==StoreStatus.OPEN);
            assertTrue("Only the founder of the store can close it", "Only the founder of the store can close it".equals(e.getMessage()));
        }
    }
    @Test
    public void dCloseStoreSuccessfully(){
        try {
            if (store1.getStoreStatus() != StoreStatus.OPEN)
                throw new Exception("store should start as OPEN");
            Set<Integer> ownersAndManagersBefore = new HashSet<>();
            ownersAndManagersBefore.addAll(store1.getStoreOwners().stream().map(StoreEmployees::getUserID).collect(Collectors.toList()));
            ownersAndManagersBefore.addAll(store1.getStoreManagers().stream().map(StoreEmployees::getUserID).collect(Collectors.toList()));
            boolean success = store1.closeStore(founder1.getId());
            Set<Integer> ownersAndManagersAfter = new HashSet<>();
            ownersAndManagersAfter.addAll(store1.getStoreOwners().stream().map(StoreEmployees::getUserID).collect(Collectors.toList()));
            ownersAndManagersAfter.addAll(store1.getStoreManagers().stream().map(StoreEmployees::getUserID).collect(Collectors.toList()));
            if (!(ownersAndManagersBefore.equals(ownersAndManagersAfter)))
                success = false;
            assertTrue("Store should be CLOSE", store1.getStoreStatus()==StoreStatus.CLOSE);
            assertTrue("Store closed successfully",success);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void eCloseStoreWhileAlreadyClosed(){
        try {
            boolean success = store1.closeStore(founder1.getId());
            assertFalse("Close store while already closed",success);
            assertTrue("Store should remain CLOSE",store1.getStoreStatus()==StoreStatus.CLOSE);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void fOpenStoreWithStoreOwner(){
        try {
            store1.reopenStore(storeOwner1.getId());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue("Store should remain CLOSE", store1.getStoreStatus()==StoreStatus.CLOSE);
            assertTrue("Only the founder of the store can open it", "Only the founder of the store can open it".equals(e.getMessage()));
        }
    }
    @Test
    public void gOpenStoreWithStoreManager(){
        try {
            store1.reopenStore(storeManager1.getId());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue("Store should remain CLOSE", store1.getStoreStatus()==StoreStatus.CLOSE);
            assertTrue("Only the founder of the store can open it", "Only the founder of the store can open it".equals(e.getMessage()));
        }
    }
    @Test
    public void hOpenStoreWithNoRoleUser(){
        try {
            store1.reopenStore(noRole.getId());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue("Store should remain CLOSE", store1.getStoreStatus()==StoreStatus.CLOSE);
            assertTrue("Only the founder of the store can open it", "Only the founder of the store can open it".equals(e.getMessage()));
        }
    }
    @Test
    public void iOpenStoreSuccessfully(){
        try {
            if (store1.getStoreStatus() != StoreStatus.CLOSE)
                throw new Exception("store should closed by now");
            Set<Integer> ownersAndManagersBefore = new HashSet<>();
            ownersAndManagersBefore.addAll(store1.getStoreOwners().stream().map(StoreEmployees::getUserID).collect(Collectors.toList()));
            ownersAndManagersBefore.addAll(store1.getStoreManagers().stream().map(StoreEmployees::getUserID).collect(Collectors.toList()));
            boolean success = store1.reopenStore(founder1.getId());
            Set<Integer> ownersAndManagersAfter = new HashSet<>();
            ownersAndManagersAfter.addAll(store1.getStoreOwners().stream().map(StoreEmployees::getUserID).collect(Collectors.toList()));
            ownersAndManagersAfter.addAll(store1.getStoreManagers().stream().map(StoreEmployees::getUserID).collect(Collectors.toList()));
            if (!(ownersAndManagersBefore.equals(ownersAndManagersAfter)))
                success = false;
            assertTrue("Store should be OPEN", store1.getStoreStatus()==StoreStatus.OPEN);
            assertTrue("Store opened successfully", success);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void jOpenStoreWhileAlreadyOpened(){
        try {
            boolean success = store1.reopenStore(founder1.getId());
            assertTrue("Store should remain OPEN", store1.getStoreStatus()==StoreStatus.OPEN);
            assertTrue("Open store while already opened", !success);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void kCloseStoreWhilePermanentlyClosed(){
        try {
            market.closeStorePermanently(userFacade.getUserByName("admin").getId(), store1.getStoreID()); //setup line
            store1.closeStore(founder1.getId());
            fail("Should have throw an error");
        } catch (Exception e) {
            assertTrue("Can't close store while permanently closed", "Store is permanently close and cannot change its status to close".equals(e.getMessage()));
        }
    }
    @Test
    public void lOpenStoreWhilePermanentlyClosed(){
        try {
            store1.reopenStore(founder1.getId());
            fail("Should have throw an error");
        } catch (Exception e) {
            assertTrue("Can't open store while permanently closed", "Store is permanently close and cannot change its status to open".equals(e.getMessage()));
        }
    }
    @Test
    public void mCloseStorePermanentlyWhileAlreadyClosedPermanently(){
        try {
            boolean success = store1.closeStorePermanently();
            assertFalse("Can't close store permanently while already permanently closed", success);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
