package ServiceLayer;

import BusinessLayer.Log;

import java.util.logging.Logger;

public class UserService {
    private static final Logger log = Log.log;
    public static void start() {
        log.info("Starting System");
        log.warning("warning");
        log.severe("ERROR");
    }
}
