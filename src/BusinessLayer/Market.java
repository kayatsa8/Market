package BusinessLayer;

import BusinessLayer.Users.UserFacade;

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

    public void login(String username, String pass) throws Exception {
        userFacade.logIn(username, pass);
    }
    public void systemStart(){
        userFacade.SystemStart();

    }

    public void logout(String userName, String pass) {
        userFacade.logout(userName, pass);
    }

    public void addOwner(String userName, String userToAdd, int storeID) {
        userFacade.addOwner(userName, userToAdd, storeID);
    }

    public void addManager(String userName, String userToAdd, int storeID) {
        userFacade.addManager(userName, userToAdd, storeID);
    }

    public void removeOwner(String userName, String userToRemove, int storeID) {
        userFacade.removeOwner(userName, userToRemove, storeID);
    }

    public void removeManager(String userName, String userToRemove, int storeID) {
        userFacade.removeManager(userName, userToRemove, storeID);
    }
}
