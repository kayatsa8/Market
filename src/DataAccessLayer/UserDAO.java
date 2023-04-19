package DataAccessLayer;

import BusinessLayer.Users.RegisteredUser;

import java.util.HashMap;

//DB mock
public class UserDAO {
    private static HashMap<Integer, RegisteredUser> userMap = new HashMap<>();

    public UserDAO() {
    }

    public static HashMap<Integer, RegisteredUser> getAllUsers() {
        return userMap;
    }

    public void addUser(RegisteredUser user) {
        userMap.put(user.getId(), user);
//        if(userMap.put(user.getUsername(),user)==null)
//            throw new Exception("Fail to add user in UserDAO");
    }

    public void removeUser(RegisteredUser user) throws Exception {
        if (userMap.remove(user.getId()) == null)
            throw new Exception("Fail to remove user in UserDAO");
    }

    public int getMaxID() {
        return 0;
    }

    public void removeManagership(int id, int storeID) {
    }

    public void removeOwnership(int id, int storeID) {
    }
}
