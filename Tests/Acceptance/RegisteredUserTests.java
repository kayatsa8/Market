package Acceptance;

import ServiceLayer.Objects.ReceiptService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class RegisteredUserTests extends ProjectTest{


    public static boolean doneSetUp = false;

    @Before
    public void setUp() {
        super.setUp();
        if(!doneSetUp) {
            setUpUser3();
            setUpUser4();
            setUpBuyUser4();
            doneSetUp = true;
        }
    }


    @After
    public void tearDown() {
    }


    /**
     * Logout #16
     */
    @Test
    public void logout_Valid(){
        boolean loggedOut = this.logOut("User4RegisteredUserTests", "User4!");
        assertTrue(loggedOut);

        boolean check = this.loginUser("User4RegisteredUserTests", "User4!");
        assertTrue(check);
    }

    @Test
    public void logOut_UserNotLoggedIn(){
        boolean loggedOut = this.logOut("User3RegisteredUserTests", "User3!");
        assertFalse(loggedOut);
    }

    /**
     * Open Store #17
     */
    @Test
    public void openStore_Valid(){

        int storeId = this.createStore(user2LoggedInId, "Store2");
        assertTrue(storeId > 0);
    }


    @Test
    public void createStore_NotLoggedIn(){
        int storeId = this.createStore(user3NotLoggedInId, "Store3");
        assertFalse(storeId > 0);
    }


    /**
     * Rate an Item or a Store #19
     */
    /*@Test
    public void rankAStore_Valid(){
        boolean ranked = this.rankAStore(user4LoggedInId, store2Id, 5);
        assertTrue(ranked);

        double rank = this.getStoreRank(user2LoggedInId, store2Id);
        assertEquals(5, rank, 0.0);
    }

    @Test
    public void rankAStore_WrongRank(){
        boolean ranked = this.rankAStore(user4LoggedInId, store2Id, 11);
        assertFalse(ranked);
    }

    @Test
    public void rankAnItemInStore_Valid(){
        boolean ranked = this.rankAnItemInStore(user4LoggedInId, store2Id, item2Id, 5);
        assertTrue(ranked);

        double rank = this.getItemRank(user2LoggedInId, store2Id, item2Id);
        assertTrue(rank == 5);
    }*/

    /**
     * send request to store #20
     */
    /*
    @Test
    public void sendRequestToStore_Valid(){
        String msg = "Item4 is not very good";
        boolean sent = this.sendMsg(user2LoggedInId, store4Id, msg);
        assertTrue(sent);

        HashMap<Integer, List<String>> complaints = this.getMsgs(store4Id);
        assertEquals(complaints.get(user7SystemManagerId).get(0), msg);
    }

    @Test
    public void sendRequestToStore_WrongStore(){
        String msg = "Msg Important";
        boolean sent = this.sendMsg(user2LoggedInId, -1, msg);
        assertFalse(sent);
    }

    @Test
    public void sendRequestToStore_EmptyMsg(){
        String msg = "";
        boolean sent = this.sendMsg(user2LoggedInId, store4Id, msg);
        assertFalse(sent);
    }*/

    /**
     * Make a complaint #21
     */
    /*
    @Test
    public void makeAComplaint_Valid(){
        this.sendComplaint(user4LoggedInId, "Complaint very very");

        HashMap<Integer, String> complaints = this.getComplaints(user7SystemManagerId);
        assertTrue(complaints.get(user4LoggedInId).contains("Complaint very very"));
    }

    @Test
    public void makeAComplaint_WrongId(){
        this.sendComplaint(-1, "Complaint very very2");
        HashMap<Integer, String> complaints = this.getComplaints(user7SystemManagerId);
        assertFalse(complaints.get(user4LoggedInId).contains("Complaint very very2"));
    }

    @Test
    public void makeAComplaint_NoMsg(){
        this.sendComplaint(user4LoggedInId, "");
        HashMap<Integer, String> complaints = this.getComplaints(user7SystemManagerId);
        assertFalse(complaints.get(user4LoggedInId).contains(""));
    }*/

    /**
     * Get Personal history purchase #22
     */
     @Test
    public void getPersonalHistory_Valid(){
         List<ReceiptService> history = this.getPersonalHistory(user2LoggedInId);
         assertNotNull(history);
         assertFalse(true);

         //assertTrue(history.get(0) );
    }

    @Test
    public void getPersonalHistory_NoHistory(){
        List<ReceiptService> history = this.getPersonalHistory(user1GuestId);
        assertNull(history);
        assertFalse(true);
    }

    @Test
    public void getPersonalHistory_NotLoggedIn(){
        List<ReceiptService> history = this.getPersonalHistory(user3NotLoggedInId);
        assertNull(history);
        assertFalse(true);
    }

    /**
     * show and edit personal information #23
     */
    /*
    @Test
    public void showPersonalInformation_Valid(){
        List<String> info = this.showPersonalInformation(user2LoggedInId);
        assertEquals(info.get(0), "User2");
        assertEquals(info.get(1), "User2!");
    }

    @Test
    public void showPersonalInformation_NotAMember(){
        List<String> info = this.showPersonalInformation(user1GuestId);
        assertNull(info);
    }


    @Test
    public void changePassword_Valid(){
        boolean changed = this.changePassword(user6ManagerOwnerOfStore2, "User6!", "User6!!");
        assertTrue(changed);

        List<String> info = this.showPersonalInformation(user6ManagerOwnerOfStore2);
        assertEquals(info.get(0), "User6");
        assertEquals(info.get(1), "User6!!");
    }

    @Test
    public void changePassword_NotCorrectPassword(){
        boolean changed = this.changePassword(user6ManagerOwnerOfStore2, "Us", "User6!!");
        assertFalse(changed);
    }*/





    protected static int user1GuestId = -1;         //guest - active
    protected static int user2LoggedInId = -1;
    protected static int user3NotLoggedInId = -1;   // registered, not logged in
    protected static int user4LoggedInId = -1;      //logged in, have items in carts
    protected static int user5ManagerOfStore2ToBeRemoved = -1; //Owner/Manager of store2, to be removed positioned  by user2
    protected static int user6OwnerOfStore2 = -1;            //Owner/Manager of store2, positioned by user2
    protected static int store2Id = -1;             //store is open
    protected static int store2ClosedId = -1;
    protected static int store4Id = -1;
    protected static int item1Id = -1;              //item1 in user1 basket
    protected static int item11Id = -1;             //item11 in store2 but not in basket
    protected static int item2Id = -1;              //item2 in store2
    protected static int item2ToBeRemovedId = -1;
    protected static int item4Id = -1;

    /**
     * User1: Guest, Not logged In
     */
    protected void setUpUser1(){
        user1GuestId = setUser("User1RegisteredUserTests","User1!", GUEST, NOT_LOGGED);
    }

    /**
     * User2: Member, logged in, Store Owner and Manager of store2
     */
    protected void setUpUser2(){
        if(user2LoggedInId != -1){
            return;
        }
        user2LoggedInId = setUser("User2RegisteredUserTests","User2!", MEMBER, LOGGED);
        user5ManagerOfStore2ToBeRemoved = setUser("User5RegisteredUserTests", "User5!", MEMBER, NOT_LOGGED);
        user6OwnerOfStore2 = setUser("User6RegisteredUserTests", "User6!", MEMBER, LOGGED);
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

    /**
     * User3: Member, Not logged in, Has a cart with items
     */
    protected void setUpUser3() {
        if(user3NotLoggedInId != -1)
            return;
        user3NotLoggedInId = setUser("User3RegisteredUserTests","User3!", MEMBER, NOT_LOGGED);
    }

    /**
     * User4: Member, logged in, Store Owner and founder of store4
     */
    protected void setUpUser4(){
        user4LoggedInId = setUser("User4RegisteredUserTests","User4!", MEMBER, LOGGED);
        if(user2LoggedInId == -1)
            user2LoggedInId = setUser("User2RegisteredUserTests","User2!", MEMBER, LOGGED);   //created for the ownership of the store
        store4Id = createStore(user4LoggedInId, "Store4");  //user4 is founder, user2 is owner
        //add items
        item4Id = addItemToStoreForTests(store4Id, "Item4", 10, "Clothes", 10 );
    }



    /**
     * Set up all Users and Stores. user1 and user2 have carts with items in them
     */
    protected void setUpAllMarket() {
        setUpUser1();
        setUpUser2();
        setUpUser3();
        setUpUser4();
        addItemsToUserForTests(user4LoggedInId, store2Id, item1Id);
        addItemsToUserForTests(user4LoggedInId, store4Id, item4Id);
    }


    protected void setUpBuyUser4() {
        if(user2LoggedInId == -1)
            setUpUser2();
        if(user4LoggedInId == -1)
            setUpUser4();

        addItemsToUserForTests(user4LoggedInId, store2Id, item2Id);
        buyCart(user4LoggedInId, "paypal");
    }


}
