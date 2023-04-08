package Tests.Acceptance;

import Tests.Bridge.Bridge;
import Tests.Bridge.Driver;
import Tests.Objects.*;

import java.util.List;

public abstract class ProjectTest {

    private Bridge bridge;
    //All the 1'st items in the 1'set store
    protected int user1GuestId;         //guest - active
    protected int user2LoggedInId;      //registered, logged in, Store Owner and Manager
    protected int user3NotLoggedInId;   // registered, not logged in
    protected int userNotExistId;
    protected int store1Id;             //open store
    protected int store2Id;
    protected int storeClosedId;
    protected int item1Id;              //item1 in user1 basket
    protected int item11Id;             //item11 in store1 but not in basket
    protected int item2Id;              //item2 in store2
    protected int item12ToBeRemovedId;


    public void setUp() {
        this.bridge = Driver.getBridge();
        setUpUsers();
        setUpStores();
        setUpExternalSystems();
        setUpCarts();
    }

    private void setUpExternalSystems() {
        //add supplier and payment services to store
    }

    private void setUpCarts() {
        //add carts to users and add items to carts
    }


    private void setUpStores() {
        //add stores to the system, at least 2
        //TODO
        //store1Id = addStore();
        //store2id = addStore();
        //storeClosedId = addStore(); //close this store!
    }


    private void setUpUsers() {
        user1GuestId = registerUser("YonatanUser123", "YonatanPass123!");
        loginUser(user1GuestId, "YonatanPass123!");

        user2LoggedInId = registerUser("YonatanUser12345", "YonatanPass123!");
        loginUser(user2LoggedInId, "YonatanPass123!");

        //add user guest
        //user3Id = loginAsGuest()
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

    protected Boolean addItemToBasket(int userId, int storeId, int itemId, int amount) {
        return this.bridge.addItemToBasket(userId, storeId, itemId, amount);
    }

    protected TestCartInfo showCart(int userId) {
        return this.bridge.showCart(userId);
    }

    protected Boolean buyCart(int userId, String paymentDetails) {
        return this.bridge.buyCart(userId, paymentDetails);
    }

    protected int addItemToStore(int storeId, String itemName, int price) {
        return this.bridge.addItemToStore(storeId, itemName, price);
    }

    protected Boolean removeItemFromStore(int storeId, int itemId) {
        return this.bridge.removeItemFromStore(storeId, itemId);
    }


    protected Boolean changeItemName(int storeId, int itemId, String newName) {
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
}
