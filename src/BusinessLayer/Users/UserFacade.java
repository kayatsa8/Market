package BusinessLayer.Users;

import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.Stores.Store;
import DataAccessLayer.UserDAO;

import java.util.HashMap;
import java.util.Map;

public class UserFacade {
    private final static int MIN_PASS_LENGTH = 6;
    private Map<Integer, RegisteredUser> userIDs;
    private UserDAO userDAO;
    private static int userID;
    private final static String adminName = "admin";

    public UserFacade() {
        userIDs = new HashMap<>();
        userDAO = new UserDAO();
        userID = userDAO.getMaxID() + 1;
    }
    private int getNewId(){
        int id=userID;
        userID++;
        return id;
    }

    public void createAdmin() throws Exception {
        RegisteredUser admin = new RegisteredUser(adminName, adminName, 0, true);
//        userDAO.addUser(admin);
        userIDs.put(0, admin);
    }

    /**
     *
     * @param username the username
     * @return <b>RegisteredUser</b> with the username or <b>null</b> if not found
     */
    public RegisteredUser getUser(String username) {
        RegisteredUser tempReg=null;
        for (Map.Entry<Integer, RegisteredUser> entry : userIDs.entrySet()) {
            Integer id = entry.getKey();
            RegisteredUser user = entry.getValue();
            if (user.getUsername().equals(username)){
                tempReg=user;
                break;
            }
        }
        return tempReg;
    }

    public RegisteredUser getUserByID(int userID) {
        return userIDs.get(userID);
    }

    public int registerUser(String username, String password) throws Exception {
        checkUserName(username);
        if (getUser(username) != null) {
            throw new Exception(String.format("Username %s is already in use. Please use another user name", username));
        } else {
            checkPassword(password);
            RegisteredUser tempUser = new RegisteredUser(username, password, getNewId());
            // add to DB
            userDAO.addUser(tempUser);
            //add to cash
            userIDs.put(tempUser.getId(), tempUser);
            return tempUser.getId();
        }
    }


    private void checkPassword(String password) throws Exception {
        if (password == null)
            throw new Exception("Password cant be null");
        if (password.length() < MIN_PASS_LENGTH)
            throw new Exception("Password too short! Must be at least 6 chars");
    }

    private void checkUserName(String userName) throws Exception {
        if (userName == null) {
            throw new Exception("Password cant be null");
        }
    }

    public int logIn(String username, String password) throws Exception {
        //List<RegisteredUser> list = new ArrayList<RegisteredUser>(userIDs.values()).stream().filter((user)->user.getUsername().equals(username)).toList();
        if (username==null||password==null)
            throw new Exception("username or password is Null");
        RegisteredUser user=getUser(username);
        if (user==null)
            throw new Exception("incorrect user name");
        int userId=user.getId();
        if (getUserByID(userId)!=null&&!(getUserByID(userId).getPassword().equals(password)))
            throw new Exception("incorrect password");
        return getUser(username).getId();
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
            userIDs.put(id, user);
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
        if (currUser == null ) {
            throw new RuntimeException("User does not exist");
        }
        currUser.addStore(store);
    }

    public void addManager(int userID, int userToAdd, int storeID) {
        RegisteredUser currUser = getUserByID(userID);
        RegisteredUser newManager = getUserByID(userToAdd);
        if (currUser == null || newManager == null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.addManager(newManager, storeID);
    }

    public void removeOwner(int userID, int idToRemove, int storeID) {
        RegisteredUser currUser = getUserByID(userID);
        RegisteredUser ownerToRemove = getUserByID(idToRemove);
        if (currUser == null || ownerToRemove == null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.removeOwner(ownerToRemove, storeID);
    }

    public void removeManager(int userID, int idToRemove, int storeID) {
        RegisteredUser currUser = getUserByID(userID);
        RegisteredUser managerToRemove = getUserByID(idToRemove);
        if (currUser == null || managerToRemove == null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.removeManager(managerToRemove, storeID);
    }

    //only called from system manager after other user associations removed
    public void removeUser(RegisteredUser userToRemove) throws Exception {
        userIDs.remove(userToRemove.getId());
        NotificationHub.getInstance().removeFromService(userToRemove.getId());
        userDAO.removeUser(userToRemove);
    }
}
