package ServiceLayer;

import BusinessLayer.Log;
import BusinessLayer.Market;

import java.util.logging.Logger;

public class UserService {
    private static final Logger log = Log.log;
    private final Market market;

    public UserService() {
        market = Market.getInstance();
    }

    public void start() {
        log.info("Starting System");
        market.systemStart();
    }

    public Result<Integer> login(String userName, String pass) {
        try {
            int id=market.login(userName,pass);
            log.info("logIn succeeded");
            return new Result<>(false, id);//login == true
        } catch (Exception e) {
            log.info("logIn failed");
            return new Result<>(true, e.getMessage());//login==false
        }
    }

    public Result<Integer> register(String userName, String pass) {
        try {
            int id=market.register(userName, pass);
            log.info("logIn succeeded");
            return new Result<>(false, id);//login == true,isErr==false
        } catch (Exception e) {
            log.info("logIn failed");
            return new Result<>(true, e.getMessage());//login==false
        }
    }

    public Result<Boolean> logout(String userName, String pass) {
        try {
            market.logout(userName,pass);
            log.info("Logout succeeded");
            return new Result<>(false, null);//login == true,isErr==false
        } catch (Exception e) {
            log.info("Logout failed");
            return new Result<>(true, e.getMessage());//login==false
        }
    }

    public Result<Boolean> addOwner(int userID, int userToAddID, int storeID) {
        try {
            market.addOwner(userID, userToAddID, storeID);
            log.info("Added user to list of store owners");
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("failed to add user to store owners");
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> addManager(int userID, int userToAdd, int storeID) {
        try {
            market.addManager(userID, userToAdd, storeID);
            log.info("Added user to list of store managers");
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("failed to add user to store managers");
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> removeOwner(int userID, int userToRemove, int storeID) {
        try {
            market.removeOwner(userID, userToRemove, storeID);
            log.info("removed owner and subsequent owners/managers");
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("failed to remove owner or subsequent owners/managers");
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> removeManager(int userID, int userToRemove, int storeID) {
        try {
            market.removeManager(userID, userToRemove, storeID);
            log.info("removed manager");
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("failed to remove store manager");
            return new Result<>(true, e.getMessage());
        }
    }

    /*
    here instead of StoreService bc only system admin can do this?
     */
    public Result<Boolean> closeStorePermanently(int userID, int storeID) throws Exception
    {
        try {
            market.closeStorePermanently(userID, storeID);
            log.info("closed store");
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("failed to close store");
            return new Result<>(true, e.getMessage());
        }
    }

}
