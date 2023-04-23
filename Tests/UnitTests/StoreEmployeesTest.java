//package UnitTests;
//
//import BusinessLayer.Market;
//import BusinessLayer.NotificationSystem.Mailbox;
//import BusinessLayer.StorePermissions.StoreManager;
//import BusinessLayer.StorePermissions.StoreOwner;
//import BusinessLayer.Stores.Store;
//import BusinessLayer.Users.RegisteredUser;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class StoreEmployeesTest {
//    static RegisteredUser user1;
//    static RegisteredUser user2;
//    static RegisteredUser user3;
//    static RegisteredUser user4;
//    static RegisteredUser user5;
//    static RegisteredUser user6;
//    static Store store1;
//    static Store store2;
//
//    @BeforeClass
//    public static void setUp() throws Exception {
//        Market market = Market.getInstance();
//        user1 = market.getUserFacade().getRegisteredUser(market.register("testUser", "testPass"));
//        user2 = market.getUserFacade().getRegisteredUser(market.register("testUser2", "testPass"));
//        user3 = market.getUserFacade().getRegisteredUser(market.register("testUser3", "testPass"));
//        user4 = market.getUserFacade().getRegisteredUser(market.register("testUser4", "testPass"));
//        user5 = market.getUserFacade().getRegisteredUser(market.register("testUser5", "testPass"));
//        user6 = market.getUserFacade().getRegisteredUser(market.register("testUser6", "testPass"));
//        store1 = new Store(1, 1, "store1");
//        user1.getStoresIOwn().put(store1.getStoreID(), new StoreOwner(user1.getId(), store1)); //doing it manually to be independent of createStore function
//        store2 = new Store(2, 4, "store2");
//        user4.getStoresIOwn().put(store2.getStoreID(), new StoreOwner(user4.getId(), store2));
//    }
//
////    @After
////    public void tearDown() throws Exception {
////    }
//
//    private void ensureOwnershipWithParent(RegisteredUser child, RegisteredUser parent, Store store) {
//        StoreOwner ownership1 = child.getStoresIOwn().get(store.getStoreID());
//        assertEquals(child.getUsername() + " should have " + store.getStoreName() + "store1 as store he owns",
//                store, ownership1.getStore());
//        assertEquals(child.getUsername() + " should have " + parent.getUsername() + " as parent in ownership",
//                parent.getId(), ownership1.getParentID());
//        StoreOwner ownership2 = parent.getStoresIOwn().get(store.getStoreID());
//        assertTrue(parent.getUsername() + " should have " + child.getUsername() + " as ownerIDefined",
//                ownership2.getOwnersIDefined().contains(child));
//        assertTrue(store.getStoreName() + " should have " + child.getUsername() + "'s id as in owners",
//                store.getStoreOwners().contains(child.getId()));
//    }
//
//    private void ensureManagerWithParent(RegisteredUser child, RegisteredUser parent, Store store) {
//        StoreManager manager = child.getStoresIManage().get(store.getStoreID());
//        assertEquals(child.getUsername() + " should have " + store.getStoreName() + " as store he manages",
//                store, manager.getStore());
//        assertEquals(child.getUsername() + " should have " + parent.getUsername() + " as parent in managing",
//                parent.getId(), manager.getParentID());
//        StoreOwner ownership = parent.getStoresIOwn().get(store.getStoreID());
//        assertTrue(parent.getUsername() + " should have " + child.getUsername() + " as managerIDefined",
//                ownership.getManagersIDefined().contains(child));
//    }
//
//    private void ensureManagerRemovedWithOwner(RegisteredUser manager, RegisteredUser owner, Store store) {
//        assertNull(manager.getUsername() + " shouldnt have the store in stores i manage",
//                manager.getStoreIManage(store.getStoreID()));
//        assertFalse(owner.getUsername() + " should no longer contain user1 in managers i defined",
//                owner.getStoreIOwn(store.getStoreID()).getManagersIDefined().contains(manager));
//        assertFalse(manager.getUsername() + "'s ID shouldnt be in store manager list",
//                store.getStoreManagers().contains(manager.getId()));
//    }
//
//    private void ensureManagerRemovedAfterOwnerDeletion(RegisteredUser manager, RegisteredUser owner, Store store) {
//        assertNull(manager.getUsername() + " shouldnt have the store in stores i manage",
//                manager.getStoreIManage(store.getStoreID()));
//        assertNull(owner.getUsername() + " shouldnt have the store in stores i own",
//                owner.getStoreIOwn(store.getStoreID()));
//        assertFalse(manager.getUsername() + "'s ID shouldnt be in store manager list",
//                store.getStoreManagers().contains(manager.getId()));
//    }
//
//    private void removeOwnerSetup() {
//        user4.addOwner(user5, store2.getStoreID());
//        user4.addOwner(user2, store2.getStoreID());
//        user5.addOwner(user3, store2.getStoreID());
//        //if these fail then the test will be obsolete
//        ensureOwnershipWithParent(user5, user4, store2);
//        ensureOwnershipWithParent(user2, user4, store2);
//        ensureOwnershipWithParent(user3, user5, store2);
//    }
//
//    private void removeManagerSetup(RegisteredUser parent) {
//        parent.addManager(user1, store2.getStoreID());
//        parent.addManager(user6, store2.getStoreID());
//        //if these fail then the test will be obsolete
//        ensureManagerWithParent(user6, parent, store2);
//    }
//
//    @Test
//    public void addOwner() {
//        assertThrows("Should not allow to add owner if you aren't owner",
//                RuntimeException.class, () -> user2.addOwner(user1, store1.getStoreID()));
//        user1.addOwner(user2, store1.getStoreID());
//        ensureOwnershipWithParent(user2, user1, store1);
//        assertThrows("Should not allow to add owner if user is already owner",
//                RuntimeException.class, () -> user1.addOwner(user2, store1.getStoreID()));
//    }
//
//    @Test
//    public void addManager() {
//        assertThrows("Should not allow to add manager if you aren't owner",
//                RuntimeException.class, () -> user3.addManager(user1, store1.getStoreID()));
//        user1.addManager(user3, store1.getStoreID());
//        ensureManagerWithParent(user3, user1, store1);
//        assertThrows("Should not allow to add manager if user is already manager",
//                RuntimeException.class, () -> user1.addOwner(user3, store1.getStoreID()));
//    }
//
//    @Test
//    public void removeOwner() {
//        assertThrows("Should not allow to remove owner if you aren't owner",
//                RuntimeException.class, () -> user1.removeOwner(user4, store2.getStoreID()));
//        assertThrows("Should not allow to remove user that isnt owner",
//                RuntimeException.class, () -> user4.removeOwner(user1, store2.getStoreID()));
//
//        removeOwnerSetup();
//        assertThrows("Should not allow to remove yourself if you are founder",
//                RuntimeException.class, () -> user4.removeOwner(user4, store2.getStoreID()));
//        assertThrows("Should not be able to remove owner you did not define, even if 'grandchild'",
//                RuntimeException.class, () -> user4.removeOwner(user1, store2.getStoreID()));
//        assertThrows("Should not be able to remove owner you did not define",
//                RuntimeException.class, () -> user5.removeOwner(user2, store2.getStoreID()));
//        user4.removeOwner(user5, store2.getStoreID());
//        //ensure user5 and user1 are no longer owners
//        assertNull("user5 should no longer be an owner in stores he owns", user5.getStoreIOwn(store2.getStoreID()));
//        assertFalse("user5 should no longer be an owner in stores owner list", store2.getStoreOwners().contains(user5.getId()));
//        assertFalse("user4 should no longer contain user5 in owners i defined", user4.getStoreIOwn(store2.getStoreID()).getOwnersIDefined().contains(user5));
//        assertNull("user1 should no longer be an owner in stores he owns", user5.getStoreIOwn(store2.getStoreID()));
//        assertFalse("user1 should no longer be an owner in stores owner list", store2.getStoreOwners().contains(user1.getId()));
//        //ensure user2 is still owner
//        ensureOwnershipWithParent(user2, user4, store2);
//    }
//
//    @Test
//    public void removeManager() {
//        assertThrows("Should not allow to remove manager if you aren't owner",
//                RuntimeException.class, () -> user1.removeManager(user4, store2.getStoreID()));
//        removeManagerSetup(user4);
//        user4.removeManager(user1, store2.getStoreID());
//        //should only affect user1
//        ensureManagerWithParent(user6, user4, store2);
//        ensureManagerRemovedWithOwner(user1, user4, store2);
//    }
//
//    @Test
//    public void removeManagerFromCascade() {
//        //test that remove owner also removes relevant managers
//        removeOwnerSetup();
//        removeManagerSetup(user3);
//        user4.removeOwner(user5, store2.getStoreID());
//        ensureManagerRemovedAfterOwnerDeletion(user1, user3, store2);
//        ensureManagerRemovedAfterOwnerDeletion(user6, user3, store2);
//    }
//}