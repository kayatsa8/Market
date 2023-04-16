package BusinessLayer.Users;

import java.util.HashMap;
import java.util.Map;

import BusinessLayer.StorePermissions.StoreOwner;
import DataAccessLayer.UserDAO;

public class UserFacade {
    private Map<String, RegisteredUser> users;
    private UserDAO userDAO;
    private static int userID;

    public UserFacade() {
        users = new HashMap<>();
        userDAO=new UserDAO();
        userID = userDAO.getMaxID()+1;
    }

    public void registerUser(String username, String password) throws Exception {
        checkUserName(username);
        if (users.get(username)!=null) {
            throw new Exception(String.format("Username %s is already in use. Please use another user name", username));
        }
        else {
            if (checkPassword(password)){
                RegisteredUser tempUser=new RegisteredUser(username,password, userID++);
                // add to DB
                userDAO.AddUser(tempUser);
                //add to cash
                users.put(username, tempUser);
            }

        }
    }

    private boolean checkPassword(String password) throws Exception {
        if (password==null)
            throw new Exception("Password cant be null");
        return password.length()>=6;

    }
    private void checkUserName(String userName) throws Exception {
        if (userName==null) {
            throw new Exception("Password cant be null");
        }
    }

    public void logIn(String username, String password) throws Exception {
        if (!(users.containsKey(username)
                && users.get(username).getPassword().equals(password)))
            throw new Exception("incorrect user name or password");
    }

    public boolean logOut(String username) {
        return true;
    }
    public void SystemStart(){
        LoadUsers();//Registered only
    }
    public void LoadUsers(){
        HashMap<String, RegisteredUser> dbUsersMap=UserDAO.GetAllUsers();
        for(Map.Entry<String, RegisteredUser> entry : dbUsersMap.entrySet()) {
            String name = entry.getKey();
            RegisteredUser user = entry.getValue();
            //TODO load User's Cart
            //user.setCart(cartDBO.getCart(name));
            users.put(name,user);
        }

    }


    public void addOwner(String userName, String userToAdd, int storeID) {
        RegisteredUser currUser = users.get(userName);
        RegisteredUser newOwner = users.get(userToAdd);
        if (currUser==null || newOwner==null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.addOwner(newOwner, storeID);
    }

    public void addManager(String userName, String userToAdd, int storeID) {
        RegisteredUser currUser = users.get(userName);
        RegisteredUser newManager = users.get(userToAdd);
        if (currUser==null || newManager==null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.addManager(newManager, storeID);
    }

    public void removeOwner(String userName, String usernameToRemove, int storeID) {
        RegisteredUser currUser = users.get(userName);
        RegisteredUser ownerToRemove = users.get(usernameToRemove);
        if (currUser==null || ownerToRemove==null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.removeOwner(ownerToRemove, storeID);
    }

    public void removeManager(String userName, String usernameToRemove, int storeID) {
        RegisteredUser currUser = users.get(userName);
        RegisteredUser managerToRemove = users.get(usernameToRemove);
        if (currUser==null || managerToRemove==null) {
            throw new RuntimeException("User does not exist");
        }
        currUser.removeManager(managerToRemove, storeID);
    }
}
