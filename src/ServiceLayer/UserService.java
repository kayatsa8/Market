package ServiceLayer;

import BusinessLayer.Log;
import BusinessLayer.Market;

import java.util.logging.Logger;

public class UserService {
    private static final Logger log = Log.log;
    private final Market market;
    public UserService(){
        market=Market.getInstance();
    }

    public void start() {
        log.info("Starting System");
        market.SystemStart();

    }
    public Result<Boolean> Login(String userName,String pass){
        try {
            market.logIn(userName,pass);
            log.info("logIn succeeded");
            return new Result<>(false,true);//login == true
        } catch (Exception e) {
            log.info("logIn failed");
            return new Result<>(true,e.getMessage());//login==false
        }
    }

    public Result<Boolean> Register(String userName,String pass){
        try {
            market.register(userName,pass);
            log.info("logIn succeeded");
            return new Result<>(false,true);//login == true,isErr==false
        } catch (Exception e) {
            log.info("logIn failed");
            return new Result<>(true,e.getMessage());//login==false
        }
    }

}
