package Acceptance;

import BusinessLayer.Stores.Category;
import ServiceLayer.Objects.ReceiptService;
import ServiceLayer.Objects.StoreService;
import ServiceLayer.Objects.UserStaffInfoService;
import org.junit.After;
import org.junit.Test;


import java.util.HashMap;
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
        int itemId = this.addCatalogItem(store2Id, "itemName", 10, Category.Kitchen);
        addItemAmount(store2Id, itemId, 10);
        assertTrue(itemId > 0);

        StoreService store = this.getStoreInfo(store2Id);
        assertTrue(store.hasItem(itemId));
    }

    @Test
    public void addItemToStore_PriceWrong(){
        int itemId = this.addCatalogItem(store2Id, "itemName2", -1, Category.Kitchen);
        assertTrue(itemId < 0);
    }

    @Test
    public void addItemToStore_StoreNotExisting(){
        int itemId = this.addCatalogItem(-1, "itemName2", 1, Category.Kitchen);
        assertTrue(itemId < 0);
    }

    @Test
    public void addItemToStore_ItemExistsInStore(){
        int itemId = this.addCatalogItem(store2Id, getTestItemName(store2Id, item2Id), 10, Category.Kitchen);
        assertTrue(itemId < 0);
    }

    /**
     * Manage Inventory - Remove #28
     */
    @Test
    public void removeItemFromStore_Valid(){
        boolean removed = this.removeItemFromStore(store2Id, item2ToBeRemovedId);
        assertTrue(removed);

        StoreService store = this.getStoreInfo(store2Id);
        assertTrue(store.hasItem(item2ToBeRemovedId));
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

    @Test    //Checks circle of owners!
    public void defineStoreOwner_DefineMyOwner(){
        boolean defined = this.defineStoreOwner(store2Id, user6ManagerOwnerOfStore2, user2LoggedInId);
        assertFalse(defined);
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

    @Test    //Checks circle of Managers!
    public void defineStoreManager_DefineMyManager(){
        boolean defined = this.defineStoreManager(store2Id, user6ManagerOwnerOfStore2, user2LoggedInId);
        assertFalse(defined);
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
     * Remove Store Manager #33.5  NotForVersion1
     */
    /*@Test
    public void removeStoreManager_Valid(){
        boolean removed = this.removeStoreManager(store2Id, user2LoggedInId, user5ManagerOwnerOfStore2ToBeRemoved);
        assertTrue(removed);

        boolean check = this.checkIfStoreManager(user5ManagerOwnerOfStore2ToBeRemoved, store2Id);
        assertFalse(check);
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
    }*/


    /**
     * Remove Store Owner #34   NotForVersion1
     */
    /*@Test
    public void removeStoreOwner_Valid(){
        boolean removed = this.removeStoreOwner(store2Id, user2LoggedInId, user5ManagerOwnerOfStore2ToBeRemoved);
        assertTrue(removed);

        boolean check = this.checkIfStoreOwner(user5ManagerOwnerOfStore2ToBeRemoved, store2Id);
        assertTrue(check);
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
    }*/

    /**
     * Close Store #35
     */
    @Test
    public void closeStore_Valid(){
        boolean closed = this.closeStore(user4LoggedInId, store4Id);
        assertTrue(closed);

        //user4 is still owner of this store!
        //boolean stillOwner = this.checkIfStoreOwner(user4LoggedInId, store4Id);
        //assertTrue(stillOwner);
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
     * Reopen Store #36 NotForVersion1
     */
    /*@Test
    public void reopenStore_Valid(){
        int storeId = openAndCloseStoreForUser2();

        boolean reopened = this.reopenStore(user2LoggedInId, storeId);
        assertTrue(reopened);

        List<String> notifications = this.getNotifications(user2LoggedInId);
        assertTrue(notifications.contains("Reopened Store!"));
    }

    private int openAndCloseStoreForUser2() {
        int storeId = this.createStore(user2LoggedInId);
        this.closeStore(user2LoggedInId, storeId);
        return storeId;
    }

    @Test
    public void reopenStore_StoreOpened(){
        boolean reopened = this.reopenStore(user2LoggedInId, store2Id);
        assertFalse(reopened);
    }

    @Test
    public void reopenStore_NotStoreOwner(){
        int storeId = openAndCloseStoreForUser2();
        boolean reopened = this.reopenStore(user1GuestId, store2Id);
        assertFalse(reopened);
    }*/

    /**
     * Show information of store staff  #37
     */
    @Test
    public void showStaffInfo_Valid(){
        List<UserStaffInfoService> staff = this.showStaffInfo(store2Id, user2LoggedInId);
        assertTrue(false);
    }

    @Test
    public void showStaffInfo_UserNotManagerOrOwner(){
        List<UserStaffInfoService> staffInfo = this.showStaffInfo(store2Id, user1GuestId);
        assertTrue(false);
    }

    /**
     * Get store info and answer request as Store manager #38  NotForVersion1
     */
    /*
    @Test
    public void getStoreInformation_Valid(){
        StoreService result = this.getStoreInformationAsStoreManager(store2Id, user2LoggedInId);
        assertTrue(false);
    }

    @Test
    public void getStoreInformation_UserNotManagerOfThisStore(){
        StoreService result = this.getStoreInformationAsStoreManager(store2Id, user1GuestId);
        assertNull(result);
    }

    @Test
    public void getStoreInformation_StoreNotExist(){
        StoreService result = this.getStoreInformationAsStoreManager(-1, user2LoggedInId);
        assertNull(result);
    }

    @Test
    public void getStoreInformation_getRequests_Valid(){
        List<String> requests = this.getRequestsOfStore_AsStoreOwnerManager(user2LoggedInId, store2Id);
        //get the requests from this object
        assertTrue(false);
    }
    */

    /**
     * Get Selling History #39
     */
    @Test
    public void getSellingHistory_Valid(){
        HashMap<Integer,List<ReceiptService>> receipts = this.getSellingHistory(store2Id, user2LoggedInId);
        //assertEquals();
        assertTrue(false);
    }

    @Test
    public void getSellingHistory_UserNotManagerOrOwner(){
        HashMap<Integer,List<ReceiptService>> staffInfo = this.getSellingHistory(store2Id, user1GuestId);
        assertNull(staffInfo);
    }

    @Test
    public void getSellingHistory_StoreNotExist(){
        HashMap<Integer,List<ReceiptService>> staffInfo = this.getSellingHistory(-1, user2LoggedInId);
        assertNull(staffInfo);
    }


}
