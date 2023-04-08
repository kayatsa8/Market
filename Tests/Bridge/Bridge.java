package Tests.Bridge;

import Tests.Objects.*;

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
    Boolean addItemToBasket(int userId, int storeId, int itemId, int amount);

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
    Boolean buyCart(int userId, String paymentDetails);

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
    Boolean removeItemFromStore(int storeId, int itemId);

    /**
     *
     * @param storeId
     * @param itemId
     * @param newName
     * @return true if successful
     */
    Boolean changeItemName(int storeId, int itemId, String newName);

    /**
     * return the staff info if the user is manager/owner
     * @param storeId
     * @param userId
     * @return Information about the staff
     */
    List<TestStaffInfo> showStaffInfo(int storeId, int userId);

    /**
     * returns all the receipts for this store
     * @param storeId
     * @param userId
     * @return list of receipts if user is store manager/owner
     */
    List<TestReceipt> getSellingHistory(int storeId, int userId);

    /**
     * gets the store information the store manager asked for and checks if her has access
     * @param storeId
     * @param userId
     * @return
     */
    TestStoreInfo getStoreInfoAsStoreManager(int storeId, int userId);
}
