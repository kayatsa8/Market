package Acceptance;

import Bridge.Bridge;
import Bridge.Driver;
import Objects.*;

import java.util.List;



public abstract class ProjectTest {

    final int GUEST = 1;
    final int MEMBER = 2;
    final int LOGGED = 1;
    final int NOT_LOGGED = 2;

    private Bridge bridge;
    protected int user1GuestId = -1;         //guest - active
    protected int user2LoggedInId = -1;
    protected int user3NotLoggedInId = -1;   // registered, not logged in
    protected int user4LoggedInId = -1;      //logged in, have items in carts
    protected int user5ManagerOwnerOfStore2ToBeRemoved = -1; //Owner/Manager of store2, to be removed positioned  by user2
    protected int user6ManagerOwnerOfStore2 = -1;            //Owner/Manager of store2, positioned by user2
    protected int userNotExistId = -1;
    protected int store2Id = -1;             //store is open
    protected int store2ClosedId = -1;
    protected int store4Id = -1;
    protected int item1Id = -1;              //item1 in user1 basket
    protected int item11Id = -1;             //item11 in store1 but not in basket
    protected int item2Id = -1;              //item2 in store2
    protected int item2ToBeRemovedId = -1;


    public void setUp() {
        this.bridge = Driver.getBridge();
        //setUpExternalSystems();
    }


    /**
     * User1: Guest, Not logged In
     */
    protected void setUpUser1(){
        user1GuestId = setUser("User1","User1!", GUEST, NOT_LOGGED);
    }

    /**
     * User2: Member, logged in, Store Owner and Manager of store2
     */
    protected void setUpUser2(){
        user2LoggedInId = setUser("User2","User2!", MEMBER, LOGGED);
        user5ManagerOwnerOfStore2ToBeRemoved = setUser("User5", "User5!", MEMBER, NOT_LOGGED);
        user6ManagerOwnerOfStore2 = setUser("User6", "User6!", MEMBER, LOGGED);
        //Make user6 and user5 manager Owner
        store2Id = createStore(user2LoggedInId); //store is open
        store2ClosedId = createStore(user2LoggedInId); //store is close
        //Make user5 as manager and owner of store2
        //add items
        item2Id = addItemToStore(store2Id, "Name1", 10);
        item2ToBeRemovedId = addItemToStore(store2Id, "Name2", 10);
    }

    /**
     * User3: Member, Not logged in, Has a cart with items
     */
    protected void setUpUser3() {
        user3NotLoggedInId = setUser("User3","User3!", MEMBER, NOT_LOGGED);
    }

    /**
     * User4: Member, logged in, Store Owner and founder of store4
     */
    protected void setUpUser4(){
        user4LoggedInId = setUser("User4","User4!", MEMBER, LOGGED);
        if(user2LoggedInId == -1)
            user2LoggedInId = setUser("User2","User2!", MEMBER, LOGGED);   //created for the ownership of the store
        store4Id = createStore(user4LoggedInId);  //user4 is founder, user2 is owner
        //add items
    }


    protected int setUser(String userName, String password, int GuestOrMember, int logged) {
        //if its a guest don't register
        //register user to system and return the ID
        return -1;
    }


    /**
     * Set up all Users and Stores. user1 and user2 have carts with items in them
     */
    protected void setUpAllMarket() {
        setUpUser1();
        setUpUser2();
        setUpUser3();
        setUpUser4();
        addItemsToUser(user1GuestId, store2Id, item1Id);
    }

    /**
     * add items to cart of user from store
     * @param userId
     * @param storeId
     */
    private void addItemsToUser(int userId, int storeId, int itemId) {
    }


    /**
     * userName : YonatanUser
     * password : YonatanPass123
     * @return
     */
    protected int registerYonatan() {
        return registerUser("YonatanUser", "YonatanPass123!");
    }


    protected String getTestItemName(int storeId, int itemId){
        //get item name from store, hold here lists of items?
        return "getTestItemName";
    }


    public int registerUser(String userName, String password) {
        return this.bridge.registerUser(userName, password);
    }

    protected boolean loginUser(int id, String password) {
        return this.bridge.loginUser(id, password);
    }

    protected void exitSystem(int id) {
        this.bridge.exitSystem(id);
    }

    protected void loadSystem() {
        this.bridge.loadSystem();
    }

    protected String getStoreInfo(int storeId) {
        return this.bridge.getStoreInfo(storeId);
    }

    protected List<TestItemInfo> searchItems(String itemName, List<String> filters) {
        return this.bridge.searchItems(itemName, filters);
    }

    protected boolean addItemToBasket(int userId, int storeId, int itemId, int amount) {
        return this.bridge.addItemToBasket(userId, storeId, itemId, amount);
    }

    protected TestCartInfo showCart(int userId) {
        return this.bridge.showCart(userId);
    }

    protected boolean buyCart(int userId, String paymentDetails) {
        return this.bridge.buyCart(userId, paymentDetails);
    }

    protected int addItemToStore(int storeId, String itemName, int price) {
        return this.bridge.addItemToStore(storeId, itemName, price);
    }

    protected boolean removeItemFromStore(int storeId, int itemId) {
        return this.bridge.removeItemFromStore(storeId, itemId);
    }

    protected boolean changeItemName(int storeId, int itemId, String newName) {
        return this.bridge.changeItemName(storeId, itemId, newName);
    }

    protected List<TestStaffInfo> showStaffInfo(int storeId, int userId) {
        return this.bridge.showStaffInfo(storeId, userId);
    }

    protected List<TestReceipt> getSellingHistory(int storeId, int userId) {
        return this.bridge.getSellingHistory(storeId, userId);
    }

    protected TestStoreInfo getStoreInformationAsStoreManager(int storeId, int userId) {
        return this.bridge.getStoreInfoAsStoreManager(storeId, userId);
    }

    protected boolean logOut(int userId) {
        return this.bridge.logOut(userId);
    }

    protected int createStore(int userId) {
        return this.bridge.createStore(userId);
    }

    protected boolean closeStore(int userId, int storeId) {
        return bridge.closeStore(userId, storeId);
    }

    protected boolean defineStoreManager(int storeId, int storeManager, int newStoreManager) {
        return this.bridge.defineStoreManager(storeId, storeManager, newStoreManager);
    }

    protected boolean removeStoreManager(int storeId, int storeManagerId, int removeUserId) {
        return this.bridge.removeStoreManager(storeId, storeManagerId, removeUserId);
    }

    protected boolean defineStoreOwner(int storeId, int ownerId, int newCoOwnerId) {
        return this.bridge.defineStoreOwner(storeId, ownerId, newCoOwnerId);
    }

    protected boolean removeStoreOwner(int storeId, int storeOwnerId, int newStoreOwnerId) {
        return this.bridge.removeStoreOwner(storeId, storeOwnerId, newStoreOwnerId);
    }

    protected boolean payCart(int userId, String paymentDetails, String paymentService) {
        return this.bridge.payCart(userId, paymentDetails, paymentService);
    }

    protected boolean askForSupply(int userId, List<TestItemInfo> items, String supplyService) {
        return this.bridge.askForSupply(userId, items, supplyService);
    }
}