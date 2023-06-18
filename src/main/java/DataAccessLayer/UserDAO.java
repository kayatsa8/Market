package DataAccessLayer;

import BusinessLayer.Market;
import BusinessLayer.Users.Guest;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.UserFacade;
import DataAccessLayer.Hibernate.DBConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//DB mock
public class UserDAO {
    private static HashMap<Integer, RegisteredUser> userMap = new HashMap<>();
    private static UserDAO instance;
    private UserDAO() {

    }
    private DBConnector<RegisteredUser> getConnector() throws Exception {
        return new DBConnector<>(RegisteredUser.class, Market.getInstance().getConfigurations());
    }

    private DBConnector<Guest> getGuestConnector() throws Exception {
        return new DBConnector<>(Guest.class, Market.getInstance().getConfigurations());
    }

    public static synchronized UserDAO getUserDao() throws Exception {
        if (instance==null) {
            instance = new UserDAO();
        }
        return instance;
    }

    public void removeUser(RegisteredUser user) throws Exception {
        getConnector().delete(user.getId());
        if (userMap.remove(user.getId()) == null)
            throw new Exception("Fail to remove user in UserDAO");
    }

    public int getMaxID() {
        //temp like this, in future change using db and change var in UF to private
        return UserFacade.userID;
    }

    public void removeManagership(RegisteredUser user) throws Exception {
        getConnector().saveState(user);
    }

    public void removeOwnership(RegisteredUser user) throws Exception {
        getConnector().saveState(user);
    }

    public Map<Integer, RegisteredUser> getUsers() throws Exception {
        List<RegisteredUser> users = getConnector().getAll();
        Map<Integer, RegisteredUser> res = new HashMap<>();
        for (RegisteredUser user : users) {
            res.put(user.getId(), user);
        }
        return res;
    }

    public void save(RegisteredUser user) throws Exception {
        getConnector().saveState(user);
    }

    public void save(Guest user) throws Exception {
        getGuestConnector().saveState(user);
    }
}
