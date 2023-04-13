package Bridge;

import Objects.*;

import java.util.HashMap;
import java.util.List;

public interface Bridge {


    /**
     * Register User
     * @param userName
     * @param password
     * @return id of the registered user, -1 if fails
     */
    int registerUser(String userName, String password);

    /**
     * Login User
     * @param id
     * @param password
     * @return true if log in successful
     */
    boolean loginUser(int id, String password);


    /**
     * this user id exit the system
     * @param id
     */
    void exitSystem(int id);


    /**
     * load system, at least 2 stores exists
     */
    void loadSystem();


    /**
     * gets the store information
     * @param storeId
     * @return
     */
    String getStoreInfo(int storeId);

    /**
     * searches for items in the store with itemName and with filters to use
     * @param itemName
     * @param filters
     * @return
     */
    List<TestItemInfo> searchItems(String itemName, List<String> filters);

    /**
     * adds item to basket if store open and item in store
     * @param userId
     * @param storeId
     * @param itemId
     * @param amount
     * @return
     */
    boolean addItemToBasket(int userId, int storeId, int itemId, int amount);

    /**
     * show cart of user with userId
     * @param userId
     * @return
     */
    TestCartInfo showCart(int userId);

    /**
     * buy the cart and
     * @param userId
     * @param paymentDetails
     * @return
     */
    boolean buyCart(int userId, String paymentDetails);

    /**
     * adds item to and existing store
     * @param storeId
     * @param itemName
     * @param price
     * @return itemId in store, -1 if failed
     */
    int addItemToStore(int storeId, String itemName, int price);


    /**
     * removes item from store if item and store exists
     * @param storeId
     * @param itemId
     * @return true if successful
     */
    boolean removeItemFromStore(int storeId, int itemId);

    /**
     *
     * @param storeId
     * @param itemId
     * @param newName
     * @return true if successful
     */
    boolean changeItemName(int storeId, int itemId, String newName);

    /**
     * return the staff info if the user is manager/owner
     * @param storeId
     * @param userId
     * @return Information about the staff
     */
    List<TestStaffInfo> showStaffInfo(int storeId, int userId);

    /**
     * returns all the receipts for this store
     *
     * @param storeId
     * @param userId
     * @return list of receipts if user is store manager/owner
     */
    HashMap<Integer, List<TestReceipt>> getSellingHistoryOfStoreForManager(int storeId, int userId);

    /**
     * gets the store information the store manager asked for and checks if her has access
     * @param storeId
     * @param userId
     * @return
     */
    TestStoreInfo getStoreInfoAsStoreManager(int storeId, int userId);

    /**
     * logout from system, save cart for this user
     * @param userId
     * @return true if logged out successful
     */
    boolean logOut(int userId);

    /**
     * creates a store and puts userId as store founder
     * @param userId
     * @return id of the new store
     */
    int createStore(int userId);

    /**
     * closes store only if user is the founder of the store
     * @param userId
     * @param storeId
     * @return true if successful
     */
    boolean closeStore(int userId, int storeId);

    /**
     * The storeOwner defines the newStoreManager as the new Store Manager
     * @param storeId
     * @param storeOwner
     * @param newStoreManager
     * @return true if successful
     */
    boolean defineStoreManager(int storeId, int storeOwner, int newStoreManager);

    /**
     * Store Owner removes the removeUserId from the management job
     * @param storeId
     * @param storeOwnerId
     * @param removeUserId
     * @return true if successful
     */
    boolean removeStoreManager(int storeId, int storeOwnerId, int removeUserId);

    /**
     * defines a store owner for the store
     * @param storeId
     * @param ownerId
     * @param newCoOwnerId
     * @return true if successful
     */
    boolean defineStoreOwner(int storeId, int ownerId, int newCoOwnerId);

    /**
     * Removes a co-StoreOwner
     * @param storeId
     * @param storeOwnerId
     * @param newStoreOwnerId
     * @return true if successful
     */
    boolean removeStoreOwner(int storeId, int storeOwnerId, int newStoreOwnerId);

    /**
     * this function goes to the external system to pay, after the cart has been checked
     * @param userId
     * @param paymentDetails
     * @param paymentService
     * @return true if external service accepted the payment
     */
    boolean payCart(int userId, String paymentDetails, String paymentService);

    /**
     * this function sets a delivery for user with items he bought.
     * @param userId
     * @param items
     * @param supplyService
     */
    boolean askForSupply(int userId, List<TestItemInfo> items, String supplyService);

    /**
     * closes store permanently only if user is store manager
     * @param storeManagerId
     * @param storeId
     * @return true if successful
     */
    boolean closeStorePermanently(int storeManagerId, int storeId);

    /**
     * checks if user is owner of store
     * @param userId
     * @param storeId
     * @return
     */
    boolean checkIfStoreOwner(int userId, int storeId);

    /**
     * removes user from system only by system manager
     * @param systemManagerId
     * @param userToRemoveId
     * @return
     */
    boolean removeRegisterdUser(int systemManagerId, int userToRemoveId);

    /**
     * System Manager gets complaints
     * @param managerId
     * @return list of complaints
     */
    HashMap<Integer, String> getComplaints(int managerId);

    /**
     * @param userId    systemManagerId
     * @param complaintsAnswers Map of userId and answer
     * @return true if successful
     */
    boolean answerComplaint(int userId, HashMap<Integer, String> complaintsAnswers);

    /**
     * User can post a complaint to system manager
     * @param userId the sender
     * @param msg
     */
    void postComplaint(int userId, String msg);

    /**
     * send msg from senderId to receiverId
     * @param senderId
     * @param receiverId
     * @param msg
     * @return
     */
    boolean sendMsg(int senderId, int receiverId, String msg);

    /**
     * get messages sent to user
     * @param userId the user who receives the msgs
     * @return Map of senderId : messages sent by him to
     */
    HashMap<Integer, List<String>> getMsgs(int userId);

    /**
     * get Receipts of his user
     * @param managerId the one who want the receipts
     * @param userId  the receipts of this user
     * @return the receipts of user with userId
     */
    HashMap<Integer, List<TestReceipt>> getSellingHistoryOfUserForManager(int managerId, int userId);

    /**
     * get information on traffic of shoppers
     * @param managerId
     * @return Map of userId to his traffic log
     */
    HashMap<Integer, String> getUsersTraffic(int managerId);

    /**
     * gets purchase rate of items
     * @param managerId
     * @return Map of id of items : how many bought it today
     */
    HashMap<Integer, Integer> getPurchaseTraffic(int managerId);

    /**
     * get how many users registered today
     * @param managerId
     * @return number of users registered today
     */
    int getNumberOfRegistrationForToady(int managerId);

    /**
     * reopens a closed store, only by store owner
     * @param userId
     * @param storeId
     * @return true if successful
     */
    boolean reopenStore(int userId, int storeId);

    /**
     * gets the list of notifications for his user
     * @param userId
     * @return
     */
    List<String> getNotifications(int userId);
}
