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
    }


    @After
    public void tearDown() {
        //delete stores and delete users from DB
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
     * Manage Inventory - Add #27
     */
    @Test
    public void addItemToStore_Valid(){
        int itemId = this.addItemToStore(store1Id, "itemName", 10);
        assertTrue(itemId > 0);
    }

    @Test
    public void addItemToStore_PriceWrong(){
        int itemId = this.addItemToStore(store1Id, "itemName2", -1);
        assertTrue(itemId < 0);
    }

    @Test
    public void addItemToStore_StoreNotExisting(){
        int itemId = this.addItemToStore(-1, "itemName2", 1);
        assertTrue(itemId < 0);
    }

    @Test
    public void addItemToStore_ItemExistsInStore(){
        int itemId = this.addItemToStore(store1Id, getTestItemName(store1Id, item1Id), 10);
        assertTrue(itemId < 0);
    }

    /**
     * Manage Inventory - Remove #28
     */
    @Test
    public void removeItemFromStore_Valid(){
        Boolean removed = this.removeItemFromStore(store1Id, item12ToBeRemovedId);
        assertTrue(removed);
    }

    @Test
    public void removeItemFromStore_StoreNotExisting(){
        Boolean removed = this.removeItemFromStore(-1, item12ToBeRemovedId);
        assertFalse(removed);
    }

    @Test
    public void removeItemFromStore_ItemNotInStore(){
        Boolean removed = this.removeItemFromStore(store1Id, -1);
        assertFalse(removed);
    }

    /**
     * Manage Inventory - Change #29
     */
    @Test
    public void changeItemDetails_Valid(){
        Boolean changed = this.changeItemName(store1Id, item1Id, "newName");
        assertTrue(changed);
    }

    @Test
    public void changeItemDetails_StoreNotExisting(){
        Boolean changed = this.changeItemName(-1, item2Id, "newName" );
        assertFalse(changed);
    }

    @Test
    public void changeItemDetails_ItemNotInStore(){
        Boolean changed = this.changeItemName(store1Id, -1, "newName");
        assertFalse(changed);
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
    public void getStoreInformation_getReuests(){
        TestStoreInfo result = this.getStoreInformationAsStoreManager(store2Id, user2LoggedInId);
        //get the requests from this object
        //assertEqulas()
    }
}
