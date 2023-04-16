package Acceptance;

import Objects.TestReceipt;
import ServiceLayer.Objects.ReceiptService;
import org.junit.After;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class SystemManagerTests extends ProjectTest{


    @Override
    public void setUp() {
        super.setUp();
        setUpAllMarket();
        butCartUser2Store4();
    }


    private void butCartUser2Store4() {
        //user2 buy items from store4 so the selling history will be in store4
    }


    @After
    public void tearDown() {
        //delete stores and delete users from DB
    }

    /**
     * Close Store permanently #40
     */
    @Test
    public void closeStorePermanently_Valid(){
        boolean closed = this.closeStorePermanently(user7SystemManagerId, store2ClosedId);
        //check if user6ManagerOwnerOfStore2 is owner of store2
        assertTrue(closed);
        boolean stillOwner = this.checkIfStoreOwner(user6ManagerOwnerOfStore2, store2ClosedId);
        assertFalse(stillOwner);
    }

    @Test
    public void closeStorePermanently_NonValidStore(){
        boolean closed = this.closeStorePermanently(user7SystemManagerId, -1);
        assertFalse(closed);
    }

    @Test
    public void closeStorePermanently_NotSystemManager(){
        boolean closed = this.closeStorePermanently(user1GuestId, store2ClosedId);
        assertFalse(closed);
    }

    /**
     * remove registeredUser #41
     */
    @Test
    public void removeRegisteredUser_Valid(){
        boolean removed = this.removeRegisterdUser(user7SystemManagerId, user4LoggedInId);
        assertTrue(removed);
        boolean check = this.checkIfStoreOwner(user4LoggedInId, store4Id);
        assertTrue(check);
        boolean login = this.loginUser(user4LoggedInId, "User4!");
        assertFalse(login);
    }

    @Test
    public void removeRegisteredUser_NotValidUser(){
        boolean removed = this.removeRegisterdUser(user7SystemManagerId, -1);
        assertFalse(removed);
    }

    @Test
    public void removeRegisteredUser_NotValidManager(){
        boolean removed = this.removeRegisterdUser(user1GuestId, user4LoggedInId);
        assertFalse(removed);
    }

    /**
     * get information and answer request #42
     */
    @Test
    public void answerComplaints_Valid(){
        this.sendComplaint(user4LoggedInId, "Complaint! important Very");
        HashMap<Integer, String> complaint = new HashMap<>();
        complaint.put(user4LoggedInId, "Answer");
        boolean answered = this.answerComplaints(user7SystemManagerId, complaint);
        assertTrue(answered);

        HashMap<Integer, String> complaints = this.getComplaints(user7SystemManagerId);
        assertNull(complaints);
    }

    @Test
    public void answerComplaints_NotSystemManager(){
        HashMap<Integer, String> complaint = new HashMap<>();
        complaint.put(user4LoggedInId, "Answer");
        boolean answered = this.answerComplaints(user4LoggedInId, complaint);
        assertFalse(answered);
    }

    @Test
    public void sendMsg_Valid(){
        String msg = "Msg Important";
        boolean sent = this.sendMsg(user7SystemManagerId, user4LoggedInId, msg);
        assertTrue(sent);

        HashMap<Integer, List<String>> complaints = this.getMsgs(user4LoggedInId);
        assertEquals(complaints.get(user7SystemManagerId).get(0), msg);
    }

    @Test
    public void sendMsg_WrongUser(){
        String msg = "Msg Important";
        boolean sent = this.sendMsg(user7SystemManagerId, user1GuestId, msg);
        assertFalse(sent);

        HashMap<Integer, List<String>> complaints = this.getMsgs(user1GuestId);
        assertNull(complaints.get(user7SystemManagerId));
    }

    /**
     * Get Selling History #43
     */
    @Test
    public void getSellingHistoryOfStore_Valid(){
        //Change to receipt
        HashMap<Integer,List<ReceiptService>> receipts = this.getSellingHistoryOfStore(user7SystemManagerId, store2Id);
        assertNotNull(receipts);
        //assertEquals(receipts.get(0).getItems().get(0).getName(), "Tomato");
    }

    @Test
    public void getSellingHistoryOfStore_WrongStore(){
        HashMap<Integer,List<ReceiptService>> receipts = this.getSellingHistoryOfStore(user7SystemManagerId, -1);
        assertNull(receipts);
    }

    @Test
    public void getSellingHistoryOfUser_Valid(){
        //Change to receipt
        HashMap<Integer,List<TestReceipt>> receipts = this.getSellingHistoryOfUser(user7SystemManagerId, user2LoggedInId);
        assertNotNull(receipts);
        //assertEquals(receipts.get(0).getItems().get(0).getName(), "Tomato");
    }

    /**
     * get system activity #44
     */
    @Test
    public void getUsersTraffic_Valid(){
        HashMap<Integer, String> result = this.getUsersTraffic(user7SystemManagerId);
        assertNotNull(result);
    }

    @Test
    public void getUsersTraffic_NotSystemManager(){
        HashMap<Integer, String> result = this.getUsersTraffic(user2LoggedInId);
        assertNull(result);
    }

    @Test
    public void getPurchaseTraffic_Valid(){
        HashMap<Integer, Integer> result = this.getPurchaseTraffic(user7SystemManagerId);
        assertNotNull(result);
    }

    @Test
    public void getNumberOfRegistrationForToady_Valid(){
        int result = this.getNumberOfRegistrationForToady(user7SystemManagerId);
        assertTrue(result > 0);
    }

    @Test
    public void getNumberOfRegistrationForToady_NotSystemManager(){
        int result = this.getNumberOfRegistrationForToady(user2LoggedInId);
        assertTrue(result < 0);
    }



}
