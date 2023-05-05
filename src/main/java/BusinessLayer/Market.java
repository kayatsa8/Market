package BusinessLayer;

import BusinessLayer.CartAndBasket.Cart;
import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.NotificationSystem.Message;
import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.Receipts.Receipt.Receipt;
import BusinessLayer.StorePermissions.StoreActionPermissions;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;
import BusinessLayer.Stores.StoreFacade;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.SystemManager;
import BusinessLayer.Users.UserFacade;
import Globals.FilterValue;
import Globals.SearchBy;
import Globals.SearchFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Market {
    private static Market instance;
    private UserFacade userFacade;
    private StoreFacade storeFacade;
    private Map<Integer, SystemManager> systemManagerMap;
    private static final Object instanceLock = new Object();
    private Market() {
        systemManagerMap = new HashMap<>();
        userFacade = new UserFacade();
        storeFacade = new StoreFacade();
    }

    public static Market getInstance() throws Exception {
        synchronized (instanceLock) {
            if (instance == null) {
                instance = new Market();
                instance.createFirstAdmin();
            }
            instance.userFacade.setGuest();
            return instance;
        }
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public StoreFacade getStoreFacade() {
        return storeFacade;
    }

    public Map<Integer, SystemManager> getSystemManagerMap() {
        return systemManagerMap;
    }

    private void createFirstAdmin() throws Exception {
        userFacade.createAdmin();
    }

    public void addAdmin(int newAdmin, SystemManager systemManager) {
        systemManagerMap.put(newAdmin, systemManager);
    }

    public int register(String username, String pass) throws Exception {
        return userFacade.registerUser(username, pass);
    }

    public int login(String username, String pass) throws Exception {
       return userFacade.logIn(username, pass);
    }

    public boolean logout(int userID) throws Exception {
        return userFacade.logout(userID);
    }

    public void systemStart() {
        userFacade.systemStart();
    }

    public void addOwner(int userID, int userToAddID, int storeID) throws Exception {
        userFacade.addOwner(userID, userToAddID, storeID);
    }

    public void addManager(int userID, int userToAdd, int storeID) throws Exception {
        userFacade.addManager(userID, userToAdd, storeID);
    }

    public void removeOwner(int userID, int userToRemove, int storeID) throws Exception {
        userFacade.removeOwner(userID, userToRemove, storeID);
    }

    public void removeManager(int userID, int userToRemove, int storeID) throws Exception {
        userFacade.removeManager(userID, userToRemove, storeID);
    }

    public boolean closeStorePermanently(int userID, int storeID) throws Exception
     {
        if (isAdmin(userID)) {
            SystemManager systemManager = systemManagerMap.get(userID);
            systemManager.closeStorePermanently(storeFacade.getStore(storeID));
            return true;
        }
        else {
            throw new RuntimeException("Only admin can close stores permanently");
        }
    }

    private boolean isAdmin(int userID) {
        return systemManagerMap.get(userID) != null;
    }

    public void removeUser(int userID, int userToRemove) throws Exception {
        if (isAdmin(userID)) {
            SystemManager systemManager = systemManagerMap.get(userID);
            systemManager.removeUser(userFacade.getRegisteredUser(userToRemove));
        }
        else
            throw new RuntimeException("Only System admin can remove a user");
    }

    public int addStore(int founderID, String name) throws Exception {
        //bc of two-way dependency: store is created with only founder ID then when founder receives store pointer he adds himself to owner list
        Store store = storeFacade.addStore(founderID, name);
        userFacade.addStore(founderID, store);
        return store.getStoreID();
    }

    public Map<CatalogItem, Boolean> getCatalog() {
        return storeFacade.getCatalog();
    }

    public Map<CatalogItem, Boolean> searchCatalog(String keywords, SearchBy searchBy, Map<SearchFilter, FilterValue> filters) throws Exception {
        return storeFacade.getCatalog(keywords, searchBy, filters);
    }

    public Cart getCart(int userID) {
        return userFacade.getCart(userID);
    }

    public Cart addItemToCart(int userID, int storeID, int itemID, int quantity) throws Exception {
        Store store = storeFacade.getStore(storeID);
        CatalogItem item = store.getItem(itemID);
        return userFacade.addItemToCart(userID, store, item, quantity);
    }

    public Cart removeItemFromCart(int userID, int storeID, int itemID) throws Exception {
        return userFacade.removeItemFromCart(userID, storeID, itemID);
    }

    public Cart changeItemQuantityInCart(int userID, int storeID, int itemID, int quantity) throws Exception {
        return userFacade.changeItemQuantityInCart(userID, storeID, itemID, quantity);
    }

    /**
     * this method is used to show the costumer all the stores he added,
     * he can choose one of them and see what is inside with getItemsInBasket
     *
     * @return List<String> @TODO maybe should be of some kind of object?
     */
    public List<String> getStoresOfBaskets(int userID) throws Exception {
        return userFacade.getStoresOfBaskets(userID);
    }

    public HashMap<CatalogItem, CartItemInfo> getItemsInBasket(int userID, String storeName) throws Exception {
        return userFacade.getItemsInBasket(userID, storeName);
    }

    public Cart buyCart(int userID, String address) throws Exception {
        return userFacade.buyCart(userID, address);
    }

    /**
     * empties the cart
     */
    public Cart emptyCart(int userID) throws Exception {
        return userFacade.emptyCart(userID);
    }

    public Store getStoreInfo(int storeID)
    {
        return storeFacade.getStore(storeID);
    }

    public CatalogItem addItemToStore(int storeID, String itemName, double itemPrice, String itemCategory) throws Exception
    {
        return storeFacade.addCatalogItem(storeID, itemName, itemPrice, itemCategory);
    }

    public CatalogItem removeItemFromStore(int storeID, int itemID) throws Exception
    {
        return storeFacade.removeItemFromStore(storeID, itemID);
    }

    public String updateItemName(int storeID, int itemID, String newName) throws Exception
    {
        return storeFacade.updateItemName(storeID, itemID, newName);
    }

    public Boolean checkIfStoreOwner(int userId, int storeID) throws Exception
    {
        return storeFacade.checkIfStoreOwner(userId, storeID);
    }

    public Boolean checkIfStoreManager(int userID, int storeID) throws Exception
    {
        return storeFacade.checkIfStoreManager(userID, storeID);
    }

    public Boolean reopenStore(int userID, int storeID) throws Exception
    {
        return storeFacade.reopenStore(userID, storeID);
    }

    public Boolean closeStore(int userID, int storeID) throws Exception
    {
        return storeFacade.closeStore(userID, storeID);
    }

    public void addManagerPermission(int userID, int storeID, RegisteredUser manager, StoreActionPermissions permission) throws Exception {
        userFacade.addManagerPermission(userID, storeID, manager, permission);
    }

    public void removeManagerPermission(int userID, int storeID, RegisteredUser manager, StoreActionPermissions permission) throws Exception {
        userFacade.removeManagerPermission(userID, storeID, manager, permission);
    }

    public boolean sendMessage(int senderID, int receiverID, String title, String content) throws Exception{
        if(storeFacade.isStoreExists(senderID)){
            storeFacade.sendMessage(senderID, receiverID, title, content);
            return true;
        }
        if(userFacade.userExists(senderID)){
            userFacade.sendMessage(senderID, receiverID, title, content);
            return true;
        }

        return false;
    }

    public void markMessageAsRead(int ID, Message message) throws Exception {
        if(storeFacade.isStoreExists(ID)){
            storeFacade.markMessageAsRead(ID, message);
        }
        if(userFacade.userExists(ID)){
            userFacade.markMessageAsRead(ID, message);
        }
    }

    public void markMessageAsNotRead(int ID, Message message) throws Exception {
        if(storeFacade.isStoreExists(ID)){
            storeFacade.markMessageAsNotRead(ID, message);
        }
        if(userFacade.userExists(ID)){
            userFacade.markMessageAsNotRead(ID, message);
        }
    }

    public List<Message> watchNotReadMessages(int ID) throws Exception
    {
        if(storeFacade.isStoreExists(ID)){
            return storeFacade.watchNotReadMessages(ID);
        }
        if(userFacade.userExists(ID)){
            return userFacade.watchNotReadMessages(ID);
        }

        return null;
    }

    public List<Message> watchReadMessages(int ID) throws Exception
    {
        if(storeFacade.isStoreExists(ID)){
            return storeFacade.watchNotReadMessages(ID);
        }
        if(userFacade.userExists(ID)){
            return userFacade.watchReadMessages(ID);
        }

        return null;
    }

    public List<Message> watchSentMessages(int ID) throws Exception
    {
        if(storeFacade.isStoreExists(ID)){
            return storeFacade.watchSentMessages(ID);
        }
        if(userFacade.userExists(ID)){
            return userFacade.watchSentMessages(ID);
        }

        return null;
    }

    public boolean setMailboxAsUnavailable(int storeID) throws Exception
    {
        if(storeFacade.isStoreExists(storeID)){
            storeFacade.setMailboxAsUnavailable(storeID);
            return true;
        }

        return false;
    }

    public boolean setMailboxAsAvailable(int storeID) throws Exception
    {
        if(storeFacade.isStoreExists(storeID)){
            storeFacade.setMailboxAsAvailable(storeID);
            return true;
        }

        return false;
    }

    public void addItemAmount(int storeID, int itemID, int amountToAdd) throws Exception
    {
        storeFacade.addItemAmount(storeID, itemID, amountToAdd);
    }

    public List<Receipt> getSellingHistoryOfStoreForManager(int storeId, int userId) throws Exception {
        if(storeFacade.checkIfStoreManager(userId, storeId) || isAdmin(userId))
            return storeFacade.getStore(storeId).getReceiptHandler().getAllReceipts();
        return null;
    }

    public ArrayList<Store> getAllStores() {
        return storeFacade.getAllStores();
    }
}
