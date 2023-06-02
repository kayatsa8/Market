package Acceptance;

import ServiceLayer.Objects.BidService;
import ServiceLayer.Objects.ChatService;
import ServiceLayer.Objects.MessageService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class BidTests extends ProjectTest{


    public static boolean doneSetUp = false;


    protected static int user2LoggedInId = -1;
    protected static int user3LoggedInId = -1;   // registered, not logged in
    protected static int user5ManagerOfStore2ToBeRemoved = -1; //Owner/Manager of store2, to be removed positioned  by user2
    protected static int user6OwnerOfStore2 = -1;            //Owner/Manager of store2, positioned by user2
    protected static int store2Id = -1;             //store is open
    protected static int store21IdOneOwner = -1;
    protected static int item1Id = -1;
    protected static int item11Id = -1;             //item11 in store2 but not in basket
    protected static int item21Id = -1;
    protected static int item211Id = -1;


    /**
     * User2: Member, logged in, Store Owner and Manager of store2
     */
    protected void setUpUser2(){
        if(user2LoggedInId != -1){
            return;
        }
        user2LoggedInId = setUser("User2StoreOwnerManagerTests","User2!", MEMBER, LOGGED);
        user5ManagerOfStore2ToBeRemoved = setUser("User5StoreOwnerManagerTests", "User5!", MEMBER, NOT_LOGGED);
        user6OwnerOfStore2 = setUser("User6StoreOwnerManagerTests", "User6!", MEMBER, LOGGED);
        store2Id = createStore(user2LoggedInId, "Store2"); //store is open

        //Make user6 and user5 manager Owner
        defineStoreOwner(store2Id, user2LoggedInId, user6OwnerOfStore2);
        defineStoreManager(store2Id , user2LoggedInId, user5ManagerOfStore2ToBeRemoved);


        store21IdOneOwner = createStore(user2LoggedInId, "Store21"); //store is open

        //add items
        item1Id = addItemToStoreForTests(store2Id, "item1", 10, "Books", 10);
        item11Id = addItemToStoreForTests(store2Id, "item11", 10, "Books", 10);
        item21Id = addItemToStoreForTests(store21IdOneOwner, "item21", 10, "Books", 10);
        item211Id = addItemToStoreForTests(store21IdOneOwner, "item211", 10, "Books", 10);
    }

    /**
     * User3: Member, logged in, Has a cart with items
     */
    protected void setUpUser3() {
        if(user3LoggedInId != -1)
            return;
        user3LoggedInId = setUser("User3StoreOwnerManagerTests","User3!", MEMBER, LOGGED);
    }




    @Before
    public void setUp() {
        super.setUp();
        if(!doneSetUp) {
            setUpUser2();
            setUpUser3();
            doneSetUp = true;
        }
    }


    @After
    public void tearDown() {
        //delete stores and delete users from DB
    }



    @Test
    public void addBid_Valid(){
        boolean added = this.getBridge().addBid(store2Id, item1Id, user3LoggedInId, 5);

        assertTrue(added);
        List<BidService> bids = this.getBridge().getUserBids(user2LoggedInId); //need to get all the storeBids

        boolean myBid = false;
        for(BidService bidService: bids){
            if(bidService.getUserId() == user3LoggedInId){
                myBid = true;
                break;
            }
        }
        assertTrue(myBid);
    }

    @Test
    public void addBid_itemNotExists(){
        boolean added = this.getBridge().addBid(store2Id, -1, user3LoggedInId, 5);

        assertFalse(added);
    }

    @Test
    public void addBid_UserNotValid(){
        boolean added = this.getBridge().addBid(store2Id, item1Id, -1, 5);

        assertFalse(added);
    }

    @Test
    public void approveBidOneOwner_Valid(){
        boolean added = this.getBridge().addBid(store21IdOneOwner, item21Id, user3LoggedInId, 5);
        assertTrue(added);

        int bidId = getBidId(store21IdOneOwner, item21Id, user3LoggedInId);
        boolean user2Accepts = this.getBridge().approve(store21IdOneOwner, bidId, user2LoggedInId);
        assertTrue(user2Accepts);

        //check if user3 got msg that the bid was approved
        HashMap<Integer, ChatService> notifications = getChats(user3LoggedInId);

        boolean foundMsg = false;
        for(ChatService chatService: notifications.values()){
            for(MessageService messageService: chatService.getMessages()){
                if(messageService.getContent().contains("your bid for the item: item21, was approved by the store")){
                    foundMsg = true;
                    break;
                }
            }
        }
        assertTrue(foundMsg);
    }

    private int getBidId(int storeId, int itemId, int userId) {
        List<BidService> bids = this.getBridge().getUserBids(user2LoggedInId); //need to get all the storeBids
        for(BidService bid: bids){
            if(bid.getStoreId() == storeId & bid.getUserId() == userId && bid.getItemId() == itemId)
                return bid.getId();
        }
        return -1;
    }

}
