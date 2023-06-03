package DataAccessLayer;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Market;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.UserFacade;
import DataAccessLayer.Hibernate.DBConnector;

import java.util.HashMap;

//DB mock
public class UserDAO {
    private static HashMap<Integer, RegisteredUser> userMap = new HashMap<>();
    DBConnector<RegisteredUser> connector;
    public UserDAO() throws Exception {
        connector = new DBConnector<>(RegisteredUser.class, Market.getConfigurations());
    }

    public static HashMap<Integer, RegisteredUser> getAllUsers() {
        return userMap;
    }

    public void addUser(RegisteredUser user) {
        userMap.put(user.getId(), user);
        connector.insert(user);
//        if(userMap.put(user.getUsername(),user)==null)
//            throw new Exception("Fail to add user in UserDAO");
    }

    public void removeUser(RegisteredUser user) throws Exception {
        connector.delete(user.getId());
        if (userMap.remove(user.getId()) == null)
            throw new Exception("Fail to remove user in UserDAO");
    }

    public int getMaxID() {
        //temp like this, in future change using db and change var in UF to private
        return UserFacade.userID;
    }

    public void removeManagership(RegisteredUser user) {
        connector.saveState(user);
    }

    public void removeOwnership(RegisteredUser user) {
        connector.saveState(user);
    }
}
