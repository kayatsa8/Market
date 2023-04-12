package Tests.Acceptance;

import Tests.Objects.TestReceipt;
import Tests.Objects.TestStaffInfo;
import Tests.Objects.TestStoreInfo;
import org.junit.After;
import org.junit.Test;


import java.util.List;

import static org.junit.Assert.*;

public class StoreOwnerManagerTests extends ProjectTest{



    @Override
    public void setUp() {
        super.setUp();
        setUpAllMarket();
    }


    @After
    public void tearDown() {
        //delete stores and delete users from DB
    }



    /**
     * Manage Inventory - Add #27
     */
    @Test
    public void addItemToStore_Valid(){
        int itemId = this.addItemToStore(store2Id, "itemName", 10);
        assertTrue(itemId > 0);
    }

    @Test
    public void addItemToStore_PriceWrong(){
        int itemId = this.addItemToStore(store2Id, "itemName2", -1);
        assertTrue(itemId < 0);
    }

    @Test
    public void addItemToStore_StoreNotExisting(){
        int itemId = this.addItemToStore(-1, "itemName2", 1);
        assertTrue(itemId < 0);
    }

    @Test
    public void addItemToStore_ItemExistsInStore(){
        int itemId = this.addItemToStore(store2Id, getTestItemName(store2Id, item2Id), 10);
        assertTrue(itemId < 0);
    }

    /**
     * Manage Inventory - Remove #28
     */
    @Test
    public void removeItemFromStore_Valid(){
        boolean removed = this.removeItemFromStore(store2Id, item2ToBeRemovedId);
        assertTrue(removed);
    }

    @Test
    public void removeItemFromStore_StoreNotExisting(){
        boolean removed = this.removeItemFromStore(-1, item2ToBeRemovedId);
        assertFalse(removed);
    }

    @Test
    public void removeItemFromStore_ItemNotInStore(){
        boolean removed = this.removeItemFromStore(store2Id, -1);
        assertFalse(removed);
    }

    /**
     * Manage Inventory - Change #29
     */
    @Test
    public void changeItemDetails_Valid(){
        boolean changed = this.changeItemName(store2Id, item2Id, "newName");
        assertTrue(changed);
    }

    @Test
    public void changeItemDetails_StoreNotExisting(){
        boolean changed = this.changeItemName(-1, item2Id, "newName" );
        assertFalse(changed);
    }

    @Test
    public void changeItemDetails_ItemNotInStore(){
        boolean changed = this.changeItemName(store2Id, -1, "newName");
        assertFalse(changed);
    }


    /**
     * Change discount and buying policies #30
     */
    @Test
    public void changePolicies_Valid(){
        //NOT IMPLEMENTED YET
        //add negative tests
    }

    /**
     * Define Store Owner #32
     */
    @Test
    public void defineStoreOwner_Valid(){
        boolean defined = this.defineStoreOwner(store2Id, user2LoggedInId, user3NotLoggedInId);
        assertTrue(defined);
    }

    @Test
    public void defineStoreOwner_AlreadyStoreOwner(){
        boolean defined = this.defineStoreOwner(store2Id, user2LoggedInId, user6ManagerOwnerOfStore2);
        assertFalse(defined);
    }

    @Test
    public void defineStoreOwner_UserNotStoreOwner(){
        boolean defined = this.defineStoreOwner(store4Id, user2LoggedInId, user6ManagerOwnerOfStore2);
        assertFalse(defined);
    }


    /**
     * Define Store Manager #33
     */
    @Test
    public void defineStoreManager_Valid(){
        boolean changed = this.defineStoreManager(store2Id, user2LoggedInId, user3NotLoggedInId);
        assertTrue(changed);
    }

    @Test
    public void defineStoreManager_AlreadyStoreManager(){
        boolean changed = this.defineStoreManager(store2Id, user2LoggedInId, user6ManagerOwnerOfStore2);
        //If its failing, check if the user4 is owner or manager of store2!!
        assertFalse(changed);
    }

    @Test
    public void defineStoreManager_UserNotThisStoreManager(){
        boolean changed = this.defineStoreManager(store4Id, user2LoggedInId, user6ManagerOwnerOfStore2);
        assertFalse(changed);
    }


    /**
     * Remove Store Manager #33.5
     */
    @Test
    public void removeStoreManager_Valid(){
        boolean removed = this.removeStoreManager(store2Id, user2LoggedInId, user5ManagerOwnerOfStore2ToBeRemoved);
        assertTrue(removed);
    }

    @Test
    public void removeStoreManager_NotByTheRightManager(){
        //User5 was positioned by User2 and he is removed by User6, error!
        boolean removed = this.removeStoreManager(store2Id, user6ManagerOwnerOfStore2, user5ManagerOwnerOfStore2ToBeRemoved);
        assertFalse(removed);
    }

    @Test
    public void removeStoreManager_NotByTheStoreManager(){
        boolean removed = this.removeStoreManager(store2Id, user1GuestId, user6ManagerOwnerOfStore2);
        assertFalse(removed);
    }


    /**
     * Remove Store Owner #34
     */
    @Test
    public void removeStoreOwner_Valid(){
        boolean removed = this.removeStoreOwner(store2Id, user2LoggedInId, user5ManagerOwnerOfStore2ToBeRemoved);
        assertTrue(removed);
    }

    @Test
    public void removeStoreOwner_NotByTheRightManager(){
        //User5 was positioned by User2 and he is removed by User6, error!
        boolean removed = this.removeStoreOwner(store2Id, user6ManagerOwnerOfStore2, user5ManagerOwnerOfStore2ToBeRemoved);
        assertFalse(removed);
    }

    @Test
    public void removeStoreOwner_NotByStoreOwner(){
        boolean removed = this.removeStoreOwner(store2Id, user1GuestId, user6ManagerOwnerOfStore2);
        assertFalse(removed);
    }

    /**
     * Close Store #35
     */
    @Test
    public void closeStore_Valid(){
        boolean closed = this.closeStore(user4LoggedInId, store4Id);
        assertTrue(closed);
    }

    @Test
    public void closeStore_OwnerNotFounder(){
        boolean closed = this.closeStore(user2LoggedInId, store4Id);
        assertFalse(closed);
    }

    @Test
    public void closeStore_StoreIsAlreadyClosed(){
        boolean closed = this.closeStore(user2LoggedInId, store2ClosedId);
        assertFalse(closed);
    }


    /**
     * Show information of store staff  #37
     */
    @Test
    public void showStaffInfo_Valid(){
        List<TestStaffInfo> staff = this.showStaffInfo(store2Id, user2LoggedInId);
        //assertEquals();
    }

    @Test
    public void showStaffInfo_UserNotManagerOrOwner(){
        List<TestStaffInfo> staffInfo = this.showStaffInfo(store2Id, user1GuestId);
        assertNull(staffInfo);
    }

    /**
     * Get store info and answer request as Store manager #38
     */
    @Test
    public void getStoreInformation_Valid(){
        TestStoreInfo result = this.getStoreInformationAsStoreManager(store2Id, user2LoggedInId);
        //assertEqulas()
    }

    @Test
    public void getStoreInformation_UserNotManagerOfThisStore(){
        TestStoreInfo result = this.getStoreInformationAsStoreManager(store2Id, user1GuestId);
        assertNull(result);
    }

    @Test
    public void getStoreInformation_StoreNotExist(){
        TestStoreInfo result = this.getStoreInformationAsStoreManager(-1, user2LoggedInId);
        assertNull(result);
    }

    @Test
    public void getStoreInformation_getRequests(){
        TestStoreInfo result = this.getStoreInformationAsStoreManager(store2Id, user2LoggedInId);
        //get the requests from this object
        //assertEqulas()
    }


    /**
     * Get Selling History #39
     */
    @Test
    public void getSellingHistory_Valid(){
        List<TestReceipt> receipts = this.getSellingHistory(store2Id, user2LoggedInId);
        //assertEquals();
    }

    @Test
    public void getSellingHistory_UserNotManagerOrOwner(){
        List<TestReceipt> staffInfo = this.getSellingHistory(store2Id, user1GuestId);
        assertNull(staffInfo);
    }

    @Test
    public void getSellingHistory_StoreNotExist(){
        List<TestReceipt> staffInfo = this.getSellingHistory(-1, user2LoggedInId);
        assertNull(staffInfo);
    }


}
