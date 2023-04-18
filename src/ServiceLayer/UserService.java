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

    public Result<Boolean> login(String userName, String pass) {
        try {
            market.login(userName,pass);
            log.info("logIn succeeded");
            return new Result<>(false, null);//login == true
        } catch (Exception e) {
            log.info("logIn failed");
            return new Result<>(true, e.getMessage());//login==false
        }
    }

    public Result<Boolean> register(String userName, String pass) {
        try {
            market.register(userName, pass);
            log.info("logIn succeeded");
            return new Result<>(false, null);//login == true,isErr==false
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

    public Result<Boolean> addManager(String userName, String userToAdd, int storeID) {
        try {
            market.addManager(userName, userToAdd, storeID);
            log.info("Added user to list of store managers");
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("failed to add user to store managers");
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> removeOwner(String userName, String userToRemove, int storeID) {
        try {
            market.removeOwner(userName, userToRemove, storeID);
            log.info("removed owner and subsequent owners/managers");
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("failed to remove owner or subsequent owners/managers");
            return new Result<>(true, e.getMessage());
        }
    }

    public Result<Boolean> removeManager(String userName, String userToRemove, int storeID) {
        try {
            market.removeManager(userName, userToRemove, storeID);
            log.info("removed manager");
            return new Result<>(false, true);
        } catch (Exception e) {
            log.info("failed to remove store manager");
            return new Result<>(true, e.getMessage());
        }
    }
}
