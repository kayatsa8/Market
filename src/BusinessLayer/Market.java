package BusinessLayer;

import BusinessLayer.users.UserFacade;

public class Market {
    private UserFacade userFacade;
    private static Market instance;

    private Market(){userFacade = new UserFacade();}

    public static Market getInstance() {
        if (instance == null) {
            instance = new Market();
        }
        return instance;
    }


    public void register(String username, String pass) throws Exception {
        userFacade.registerUser(username, pass);
    }

    public void logIn(String username, String pass) throws Exception {
        userFacade.logIn(username, pass);
    }
    public void SystemStart(){
        userFacade.SystemStart();

    }
}
