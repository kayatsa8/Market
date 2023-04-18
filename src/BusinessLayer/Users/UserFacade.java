package BusinessLayer.Users;

import BusinessLayer.Stores.Store;
import DataAccessLayer.UserDAO;

import java.util.HashMap;
import java.util.Map;

public class UserFacade {
    private final static int MIN_PASS_LENGTH = 6;
    private Map<String, RegisteredUser> users;
    private Map<Integer, RegisteredUser> userIDs;
    private UserDAO userDAO;
    private static int userID;
    private final static String adminName = "admin";

    public UserFacade() {
        users = new HashMap<>();
        userIDs = new HashMap<>();
        userDAO = new UserDAO();
        userID = userDAO.getMaxID() + 1;
    }

    public void createAdmin() {
        RegisteredUser admin = new RegisteredUser(adminName, adminName, 0, true);
//        userDAO.addUser(admin);
        users.put(adminName, admin);
    }

    public RegisteredUser getUser(String userName) {
        return users.get(userName);
    }

    public RegisteredUser getUserByID(int userID) {
        return userIDs.get(userID);
    }

    public void registerUser(String username, String password) throws Exception {
        checkUserName(username);
        if (getUser(username) != null) {
            throw new Exception(String.format("Username %s is already in use. Please use another user name", username));
        } else {
            if (checkPassword(password)) {
                RegisteredUser tempUser = new RegisteredUser(username, password, userID++);
                // add to DB
                userDAO.addUser(tempUser);
                //add to cash
                users.put(username, tempUser);
                userIDs.put(tempUser.getId(), tempUser);
            }
        }
    }


    private boolean checkPassword(String password) throws Exception {
        if (password == null)
            throw new Exception("Password cant be null");
        if (password.length() < MIN_PASS_LENGTH)
            throw new Exception("Password too short! Must be at least 6 chars");
        return true;
    }

    private void checkUserName(String userName) throws Exception {
        if (userName == null) {
            throw new Exception("Password cant be null");
        }
    }

    public void logIn(String username, String password) throws Exception {
        if (!(users.containsKey(username)
                && getUser(username).getPassword().equals(password)))
            throw new Exception("incorrect user name or password");
    }

    public boolean logOut(String username) {
        return true;
    }

    public void systemStart() {
        LoadUsers();//Registered only
    }

    public void LoadUsers() {
        HashMap<String, RegisteredUser> dbUsersMap = UserDAO.getAllUsers();
        for (Map.Entry<String, RegisteredUser> entry : dbUsersMap.entrySet()) {
            String name = entry.getKey();
            RegisteredUser user = entry.getValue();
            //TODO load User's Cart
            //user.setCart(cartDBO.getCart(name));
            users.put(name, user);
        }

    }

    public void logout(String userName, String pass) {
        //TODO sessions and all
    }

    public void addOwner(int userID, int userToAddID, int storeID) {
        RegisteredUser currUser = getUserByID(userID);
        RegisteredUser newOwner = getUserByID(userToAddID);
        if (currUser == null || newOwner == null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.addOwner(newOwner, storeID);
    }

    public void addStore(int founderID, Store store) {
        RegisteredUser currUser = getUserByID(founderID);
        currUser.addStore(store);
    }

    public void addManager(String userName, String userToAdd, int storeID) {
        RegisteredUser currUser = getUser(userName);
        RegisteredUser newManager = getUser(userToAdd);
        if (currUser == null || newManager == null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.addManager(newManager, storeID);
    }

    public void removeOwner(String userName, String usernameToRemove, int storeID) {
        RegisteredUser currUser = getUser(userName);
        RegisteredUser ownerToRemove = getUser(usernameToRemove);
        if (currUser == null || ownerToRemove == null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.removeOwner(ownerToRemove, storeID);
    }

    public void removeManager(String userName, String usernameToRemove, int storeID) {
        RegisteredUser currUser = getUser(userName);
        RegisteredUser managerToRemove = getUser(usernameToRemove);
        if (currUser == null || managerToRemove == null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.removeManager(managerToRemove, storeID);
    }

    //only called from system manager after other user associations removed
    public void removeUser(RegisteredUser userToRemove) throws Exception {
        users.remove(userToRemove.getUsername());
        userIDs.remove(userToRemove.getId());
        userDAO.removeUser(userToRemove);
    }
}
