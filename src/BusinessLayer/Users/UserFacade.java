package BusinessLayer.Users;

import BusinessLayer.Log;
import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.Stores.Store;
import DataAccessLayer.UserDAO;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class UserFacade {
    private static final Logger log = Log.log;
    private final static int MIN_PASS_LENGTH = 6;
    private final static String adminName = "admin";
    private static int userID;
    //    private Map<String, RegisteredUser> users;
    private Map<Integer, RegisteredUser> users;
    private UserDAO userDAO;

    public UserFacade() {
//        users = new HashMap<>();
        users = new HashMap<>();
        userDAO = new UserDAO();
        userID = userDAO.getMaxID() + 1;
    }

    private int getNewId() {
        return userID++;
    }

    public void createAdmin() {
        RegisteredUser admin = new RegisteredUser(adminName, adminName, 1000000, true);
//        userDAO.addUser(admin);
        users.put(0, admin);
    }

    public RegisteredUser getUserByName(String userName) throws Exception {
        for (RegisteredUser user : users.values()) {
            if (user.getUsername().equals(userName)) {
                return user;
            }
        }
        throw new Exception("No user exists with name " + userName);
    }

    public RegisteredUser getUser(int userID) {
        return users.get(userID);
    }

    public int registerUser(String username, String password) throws Exception {
        if (checkUserName(username) && checkPassword(password)) {
            RegisteredUser tempUser = new RegisteredUser(username, password, getNewId());
            // add to DB
            userDAO.addUser(tempUser);
            //add to cash
            users.put(tempUser.getId(), tempUser);
            NotificationHub.getInstance().registerToMailService(tempUser);
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
        return user.getId();
    }

    public boolean logOut(String username) {
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

    public void logout(String userName, String pass) {
        //TODO sessions and all
    }

    public void addOwner(int userID, int userToAddID, int storeID) {
        RegisteredUser currUser = getUser(userID);
        RegisteredUser newOwner = getUser(userToAddID);
        if (currUser == null || newOwner == null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.addOwner(newOwner, storeID);
    }

    public void addStore(int founderID, Store store) {
        RegisteredUser currUser = getUser(founderID);
        currUser.addStore(store);
    }

    public void addManager(int userID, int userToAdd, int storeID) {
        RegisteredUser currUser = getUser(userID);
        RegisteredUser newManager = getUser(userToAdd);
        if (currUser == null || newManager == null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.addManager(newManager, storeID);
    }

    public void removeOwner(int userID, int idToRemove, int storeID) {
        RegisteredUser currUser = getUser(userID);
        RegisteredUser ownerToRemove = getUser(idToRemove);
        if (currUser == null || ownerToRemove == null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.removeOwner(ownerToRemove, storeID);
    }

    public void removeManager(int userID, int idToRemove, int storeID) {
        RegisteredUser currUser = getUser(userID);
        RegisteredUser managerToRemove = getUser(idToRemove);
        if (currUser == null || managerToRemove == null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.removeManager(managerToRemove, storeID);
    }

    //only called from system manager after other user associations removed
    public void removeUser(RegisteredUser userToRemove) throws Exception {
        users.remove(userToRemove.getUsername());
        users.remove(userToRemove.getId());
        NotificationHub.getInstance().removeFromService(userToRemove.getId());
        userDAO.removeUser(userToRemove);
    }
}
