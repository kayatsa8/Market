//package UnitTests;
//
//import BusinessLayer.StorePermissions.StoreManager;
//import BusinessLayer.StorePermissions.StoreOwner;
//import BusinessLayer.Stores.Store;
//import BusinessLayer.Users.RegisteredUser;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class StorePermissionsTest {
//    RegisteredUser user1;
//    RegisteredUser user2;
//    RegisteredUser user3;
//    RegisteredUser user4;
//    RegisteredUser user5;
//    RegisteredUser user6;
//    Store store1;
//    Store store2;
//
//    @Before
//    public void setUp() throws Exception {
//        user1 = new RegisteredUser("testUser", "testPass", 1);
//        user2 = new RegisteredUser("testUser2", "testPass", 2);
//        user3 = new RegisteredUser("testUser3", "testPass", 3);
//        user4 = new RegisteredUser("testUser4", "testPass", 4);
//        user5 = new RegisteredUser("testUser5", "testPass", 5);
//        user6 = new RegisteredUser("testUser6", "testPass", 6);
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