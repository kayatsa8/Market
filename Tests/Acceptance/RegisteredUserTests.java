package Acceptance;

import Objects.TestReceipt;
import ServiceLayer.Objects.ReceiptService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.notification.RunListener;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class RegisteredUserTests extends ProjectTest{


    @Override
    public void setUp() {
        super.setUp();
        setUpUser3();
        setUpUser4();
    }


    @After
    public void tearDown() {
    }


    /**
     * Logout #16
     */
    @Test
    public void logout_Valid(){
        boolean loggedOut = this.logOut(user4LoggedInId);
        assertTrue(loggedOut);

        boolean check = this.checkIfLoggedIn(user4LoggedInId);
        assertFalse(check);
    }

    @Test
    public void logOut_UserNotLoggedIn(){
        boolean loggedOut = this.logOut(user3NotLoggedInId);
        assertFalse(loggedOut);
    }

    /**
     * Open Store #17
     */
    @Test
    public void openStore_Valid(){
        //add more arguments
        int storeId = this.createStore(user2LoggedInId);
        assertTrue(storeId > 0);
    }

    @Test
    public void createStore_MissingInformation(){
        //Missing info should be here
        int storeId = this.createStore(user2LoggedInId);
        assertTrue(storeId > 0);
    }

    @Test
    public void createStore_NotLoggedIn(){
        int storeId = this.createStore(user3NotLoggedInId);
        assertFalse(storeId > 0);
    }


    /**
     * Rate an Item or a Store #19
     */
    @Test
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
    }

    /**
     * send request to store #20
     */
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
    }

    /**
     * Make a complaint #21
     */
    @Test
    public void makeAComplaint_Valid(){
        boolean submitted = this.makeAComplaint(user4LoggedInId, "Complaint very very");
        assertTrue(submitted);

        HashMap<Integer, String> complaints = this.getComplaints(user7SystemManagerId);
        assertTrue(complaints.get(user4LoggedInId).contains("Complaint very very"));
    }

    @Test
    public void makeAComplaint_WrongId(){
        boolean submitted = this.makeAComplaint(-1, "Complaint very very");
        assertFalse(submitted);
    }

    @Test
    public void makeAComplaint_NoMsg(){
        boolean submitted = this.makeAComplaint(user4LoggedInId, "");
        assertFalse(submitted);
    }

    /**
     * Get Personal history purchase #22
     */
     @Test
    public void getPersonalHistory_Valid(){
         HashMap<Integer, List<ReceiptService>> history = this.getPersonalHistory(user2LoggedInId);
         assertNotNull(history);

         //assertTrue(history.get(0) );
    }

    @Test
    public void getPersonalHistory_NoHistory(){
        HashMap<Integer, List<ReceiptService>> history = this.getPersonalHistory(user1GuestId);
        assertNull(history);
    }

    @Test
    public void getPersonalHistory_NotLoggedIn(){
        HashMap<Integer, List<ReceiptService>> history = this.getPersonalHistory(user3NotLoggedInId);
        assertNull(history);
    }

    /**
     * show and edit personal information #23
     */
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
    }

}
