package BusinessLayer.users;

import java.util.HashMap;
import java.util.Map;
import BusinessLayer.users.User;

public class UserFacade {
    private Map<String, User> users;

    public UserFacade() {
        users = new HashMap<>();
    }

    public int registerUser(String username, String password) {
        if (users.get(username)!=null) {
            throw new RuntimeException(String.format("Username %s is already in use. Please use another user name", username));
        }
        else {
//            checkPassword(password);
//            users.put(username, new R)
            return -1;
        }
    }

    public int logIn(String username, String password) {
        return -1;
    }

    public int logOut(String username) {
        return -1;
    }


}
