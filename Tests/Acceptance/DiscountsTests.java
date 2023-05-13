package Acceptance;

import ServiceLayer.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static org.junit.Assert.*;

public class DiscountsTests extends ProjectTest{

    public static boolean doneSetUp = false;

    @Before
    public void setUp() {
        super.setUp();
        if(!doneSetUp) {
            setUpUser2();
            doneSetUp = true;
        }
    }


    @After
    public void tearDown() {
    }


    @Test
    public void addVisibleItemsDiscounts_Valid(){
        List<Integer> ids = new ArrayList<>();
        ids.add(item2Id);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 4);
        boolean added = this.addVisibleItemsDiscount(store2Id, ids, 50, calendar);
        assertTrue(added);
    }


    @Test
    public void addVisibleItemsDiscounts_ItemsNotExist(){
        List<Integer> ids = new ArrayList<>();
        ids.add(-1);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 4);
        boolean added = this.addVisibleItemsDiscount(store2Id, ids, 50, calendar);
        assertFalse(added);
    }


    @Test
    public void addVisibleItemsDiscounts_DateNotValid(){
        List<Integer> ids = new ArrayList<>();
        ids.add(item1Id);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -4);
        boolean added = this.addVisibleItemsDiscount(store2Id, ids, 50, calendar);
        assertFalse(added);
    }


    @Test
    public void addVisibleCategoryDiscounts_Valid(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 4);
        boolean added = this.addVisibleCategoryDiscount(store2Id, "Books", 40, calendar);
        assertTrue(added);
    }

    @Test
    public void addVisibleCategoryDiscounts_NonValidCategory(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 4);
        boolean added = this.addVisibleCategoryDiscount(store2Id, "NotValidCategory", 40, calendar);
        assertFalse(added);
    }


    @Test
    public void addConditionalStoreDiscounts_Valid(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 4);
        boolean added = this.addConditionalStoreDiscount(store2Id,40, calendar);
        assertTrue(added);

        //discountId = this.addConditionalStoreDiscount(store2Id,40, calendar);
        assertTrue(discountId == -1); //change it to !=
    }

    //do here test of conditionalStore and add Rules, but wait for amir's PR





















    protected static int discountId = -1;
    protected static int user2LoggedInId = -1;
    protected static int user5ManagerOfStore2ToBeRemoved = -1; //Owner/Manager of store2, to be removed positioned  by user2
    protected static int user6OwnerOfStore2 = -1;            //Owner/Manager of store2, positioned by user2
    protected static int store2Id = -1;             //store is open
    protected static int store2ClosedId = -1;
    protected static int item1Id = -1;              //item1 in user1 basket
    protected static int item11Id = -1;             //item11 in store2 but not in basket
    protected static int item2Id = -1;              //item2 in store2
    protected static int item2ToBeRemovedId = -1;

    /**
     * User2: Member, logged in, Store Owner and Manager of store2
     */
    protected void setUpUser2(){
        if(user2LoggedInId != -1){
            return;
        }
        user2LoggedInId = setUser("User2UserPurchase","User2!", MEMBER, LOGGED);
        user5ManagerOfStore2ToBeRemoved = setUser("User5UserPurchase", "User5!", MEMBER, NOT_LOGGED);
        user6OwnerOfStore2 = setUser("User6UserPurchase", "User6!", MEMBER, LOGGED);
        store2Id = createStore(user2LoggedInId, "Store2"); //store is open
        store2ClosedId = createStore(user2LoggedInId, "Store22"); //store is close
        closeStore(user2LoggedInId, store2ClosedId);

        //Make user6 and user5 manager Owner
        defineStoreOwner(store2Id, user2LoggedInId, user6OwnerOfStore2);
        defineStoreManager(store2Id , user2LoggedInId, user5ManagerOfStore2ToBeRemoved);

        //add items
        item1Id = addItemToStoreForTests(store2Id, "item1", 10, "Books", 10);
        item11Id = addItemToStoreForTests(store2Id, "item11", 10, "Books", 10);
        item2Id = addItemToStoreForTests(store2Id, "item2", 10, "Kitchen", 10);
        item2ToBeRemovedId = addItemToStoreForTests(store2Id, "Name2", 10, "Kitchen", 10);
    }




}
