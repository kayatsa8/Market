package BusinessLayer;

import BusinessLayer.users.UserFacade;

public class Market {
    private UserFacade userFacade;
    public Market() {
        userFacade = new UserFacade();
    }

    public int register(String username, String pass) {
        return userFacade.registerUser(username, pass);
    }

    public int logIn(String username, String pass) {
        return userFacade.logIn(username, pass);
    }
}
