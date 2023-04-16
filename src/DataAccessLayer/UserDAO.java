package DataAccessLayer;

import BusinessLayer.Users.RegisteredUser;

import java.util.HashMap;

//DB mock
public class UserDAO {
    private static HashMap<String, RegisteredUser> userMap;
    public UserDAO(){
        userMap=new HashMap<>();
    }

    public static HashMap<String, RegisteredUser> GetAllUsers() {
        return userMap;
    }

    public void AddUser(RegisteredUser user) throws Exception {
        if(userMap.put(user.getUsername(),user)==null)
            throw new Exception("aa");
    }

    public int getMaxID() {
        return 0;
    }
}
