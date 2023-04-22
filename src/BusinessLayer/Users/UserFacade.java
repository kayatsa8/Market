package BusinessLayer.Users;

import BusinessLayer.CartAndBasket.Cart;
import BusinessLayer.Log;
import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.NotificationSystem.Message;
import BusinessLayer.StorePermissions.StoreActionPermissions;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;
import DataAccessLayer.UserDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UserFacade {
    private static final Logger log = Log.log;
    private final static int MIN_PASS_LENGTH = 6;
    private final static String adminName = "admin";
    public static int userID;
    //    private Map<String, RegisteredUser> users;
    private Map<Integer, RegisteredUser> users;
    private UserDAO userDAO;
    private Guest guest;

    public UserFacade() {
//        users = new HashMap<>();
        users = new HashMap<>();
        userDAO = new UserDAO();
        userID = userDAO.getMaxID() + 1;
    }

    private synchronized int getNewId() {
        return userID++;
    }

    public void setGuest() {
        this.guest = new Guest();
    }

    public void createAdmin() throws Exception {
        RegisteredUser admin = new RegisteredUser(adminName, adminName, getNewId(), true);
//        userDAO.addUser(admin);
        users.put(admin.getId(), admin);
    }

    public RegisteredUser getUserByName(String userName) throws Exception {
        for (RegisteredUser user : users.values()) {
            if (user.getUsername().equals(userName)) {
                return user;
            }
        }
        throw new Exception("No user exists with name " + userName);
    }

    public boolean userExists(int ID){
        return users.containsKey(ID);
    }

    public User getUser(int userID) {
        if (userID != Guest.GUEST_USER_ID) {
            return users.get(userID);
        }
        return this.guest;
    }

    public RegisteredUser getRegisteredUser(int userID) throws Exception {
        if (userID== Guest.GUEST_USER_ID) {
            throw new Exception("This is the guest user ID, not registered user");
        }
        return users.get(userID);
    }

    public RegisteredUser getLoggedInUser(int userID) throws Exception {
        RegisteredUser user = users.get(userID);
        if (user.isLoggedIn()) {
            return user;
        }
        throw new Exception("User " + user.getUsername() + "is not logged in");
    }

    public int registerUser(String username, String password) throws Exception {
        if (checkUserName(username) && checkPassword(password)) {
            RegisteredUser tempUser = new RegisteredUser(username, password, getNewId());
            // add to DB
            userDAO.addUser(tempUser);
            //add to cash
            users.put(tempUser.getId(), tempUser);

            //There is one in the constructor, if you want to put it here, you need to do user.setMailBox(...)
            //NotificationHub.getInstance().registerToMailService(tempUser);

            return tempUser.getId();
        }
        else {
            log.severe("Problem logging in. username or password check returned false but not error");
            throw new Exception("Problem logging in. username or password check returned false but not error");
        }
    }


    private boolean checkPassword(String password) throws Exception {
        if (password == null)
            throw new Exception("Password cant be null");
        if (password.length() < MIN_PASS_LENGTH)
            throw new Exception("Password too short! Must be at least 6 chars");
        return true;
    }

    private boolean checkUserName(String userName) throws Exception {
        if (userName == null) {
            throw new Exception("Password cant be null");
        }
        for (RegisteredUser user : users.values()) {
            if (user.getUsername().equals(userName)) {
                throw new Exception("Username " + userName + " already taken");
            }
        }
        return true;
    }

    public int logIn(String username, String password) throws Exception {
        //List<RegisteredUser> list = new ArrayList<RegisteredUser>(userIDs.values()).stream().filter((user)->user.getUsername().equals(username)).toList();
        if (username == null || password == null)
            throw new Exception("username or password is Null");
        RegisteredUser user = getUserByName(username);
        if (user == null)
            throw new Exception("incorrect user name");
        if (!user.getPassword().equals(password))
            throw new Exception("incorrect password");
        if (user.isLoggedIn())
            throw new Exception("User is already logged in");
        user.logIn();
        return user.getId();
    }

    public boolean logout(int userID) throws Exception {
        RegisteredUser user = getLoggedInUser(userID);
        if (user == null)
            throw new Exception("incorrect user name");
        if (!user.isLoggedIn())
            throw new Exception("User is not logged in");
        user.logout();
        return true;
    }

    public void systemStart() {
        loadUsers();//Registered only
    }

    public void loadUsers() {
        HashMap<Integer, RegisteredUser> dbUsersMap = UserDAO.getAllUsers();
        for (Map.Entry<Integer, RegisteredUser> entry : dbUsersMap.entrySet()) {
            Integer id = entry.getKey();
            RegisteredUser user = entry.getValue();
            //TODO load User's Cart
            //user.setCart(cartDBO.getCart(name));
            users.put(user.getId(), user);
        }

    }

    public void addOwner(int userID, int userToAddID, int storeID) throws Exception {
        RegisteredUser currUser = getLoggedInUser(userID);
        RegisteredUser newOwner = getRegisteredUser(userToAddID);
        if (currUser == null || newOwner == null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.addOwner(newOwner, storeID);
    }

    public void addStore(int founderID, Store store) throws Exception {
        RegisteredUser currUser = getLoggedInUser(founderID);
        currUser.addStore(store);
    }

    public void addManager(int userID, int userToAdd, int storeID) throws Exception {
        RegisteredUser currUser = getLoggedInUser(userID);
        RegisteredUser newManager = getRegisteredUser(userToAdd);
        if (currUser == null || newManager == null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.addManager(newManager, storeID);
    }

    public void removeOwner(int userID, int userToRemove, int storeID) throws Exception {
        RegisteredUser currUser = getLoggedInUser(userID);
        RegisteredUser ownerToRemove = getRegisteredUser(userToRemove);
        if (currUser == null || ownerToRemove == null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.removeOwner(ownerToRemove, storeID);
    }

    public void removeManager(int userID, int userToRemove, int storeID) throws Exception {
        RegisteredUser currUser = getLoggedInUser(userID);
        RegisteredUser managerToRemove = getRegisteredUser(userToRemove);
        if (currUser == null || managerToRemove == null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.removeManager(managerToRemove, storeID);
    }

    //only called from system manager after other user associations removed
    public void removeUser(RegisteredUser userToRemove) throws Exception {
        users.remove(userToRemove.getId());
        userDAO.removeUser(userToRemove);
    }

    public Cart getCart(int userID) {
        return getUser(userID).getCart();
    }

    public Cart addItemToCart(int userID, Store store, CatalogItem item, int quantity) throws Exception {
        return getLoggedInUser(userID).addItemToCart(store, item, quantity);
    }

    public Cart removeItemFromCart(int userID, int storeID, int itemID) throws Exception {
        return getLoggedInUser(userID).removeItemFromCart(storeID, itemID);
    }

    public Cart changeItemQuantityInCart(int userID, int storeID, int itemID, int quantity) throws Exception {
        return getLoggedInUser(userID).changeItemQuantityInCart(storeID, itemID, quantity);
    }

    /**
     * this method is used to show the costumer all the stores he added,
     * he can choose one of them and see what is inside with getItemsInBasket
     *
     * @return List<String> @TODO maybe should be of some kind of object?
     */
    public List<String> getStoresOfBaskets(int userID) throws Exception {
        return getLoggedInUser(userID).getStoresOfBaskets();
    }

    public HashMap<CatalogItem, CartItemInfo> getItemsInBasket(int userID, String storeName) throws Exception {
        return getLoggedInUser(userID).getItemsInBasket(storeName);
    }

    public Cart buyCart(int userID, String address) throws Exception {
        return getLoggedInUser(userID).buyCart(address);
    }

    /**
     * empties the cart
     */
    public Cart emptyCart(int userID) throws Exception {
        return getLoggedInUser(userID).emptyCart();
    }

    public void addManagerPermission(int userID, int storeID, RegisteredUser manager, StoreActionPermissions permission) throws Exception {
        RegisteredUser user = getLoggedInUser(userID);
        user.addManagerPermission(storeID, manager, permission);
    }

    public void removeManagerPermission(int userID, int storeID, RegisteredUser manager, StoreActionPermissions permission) throws Exception {
        RegisteredUser user = getLoggedInUser(userID);
        user.removeManagerPermission(storeID, manager, permission);
    }

    public void sendMessage(int senderID, int receiverID, String title, String content){
        users.get(senderID).sendMessage(receiverID, title, content);
    }

    public void markMessageAsRead(int userID, Message message) throws Exception {
        users.get(userID).markMessageAsRead(message);
    }

    public void markMessageAsNotRead(int userID, Message message) throws Exception {
        users.get(userID).markMessageAsNotRead(message);
    }

    public List<Message> watchNotReadMessages(int userID){
        return users.get(userID).watchNotReadMessages();
    }

    public List<Message> watchReadMessages(int userID){
        return users.get(userID).watchReadMessages();
    }

    public List<Message> watchSentMessages(int userID){
        return users.get(userID).watchSentMessages();
    }
}
