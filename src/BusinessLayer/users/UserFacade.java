package BusinessLayer.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import BusinessLayer.users.User;
import DataAccessLayer.UserDAO;

public class UserFacade {
    private Map<String, RegisteredUser> users;
    private UserDAO userDAO;


    public UserFacade() {
        users = new HashMap<>();
        userDAO=new UserDAO();
    }

    public void registerUser(String username, String password) throws Exception {
        checkUserName(username);
        if (users.get(username)!=null) {
            throw new Exception(String.format("Username %s is already in use. Please use another user name", username));
        }
        else {
            if (checkPassword(password)){
                RegisteredUser tempUser=new RegisteredUser(username,password);
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


}
