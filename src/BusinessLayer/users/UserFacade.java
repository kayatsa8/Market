package BusinessLayer.users;

import java.util.HashMap;
import java.util.Map;
import BusinessLayer.users.User;

public class UserFacade {
    private Map<Integer, User> users;

    public UserFacade() {
        users = new HashMap<>();
    }

    public int registerUser(String username, String password) {
        return -1;
    }

    public int logIn(String username, String password) {
        return -1;
    }

    public int logOut(String username) {
        return -1;
    }


}
