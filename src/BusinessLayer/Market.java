package BusinessLayer;

import BusinessLayer.Stores.Store;
import BusinessLayer.Stores.StoreFacade;
import BusinessLayer.Users.SystemManager;
import BusinessLayer.Users.UserFacade;

import java.util.HashMap;
import java.util.Map;

public class Market {
    private static Market instance;
    private UserFacade userFacade;
    private StoreFacade storeFacade;
    private Map<String, SystemManager> systemManagerMap;
    private Market() {
        systemManagerMap = new HashMap<>();
        userFacade = new UserFacade();
        storeFacade = new StoreFacade();
    }

    public static Market getInstance() {
        if (instance == null) {
            instance = new Market();
            instance.createFirstAdmin();
        }
        return instance;
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public StoreFacade getStoreFacade() {
        return storeFacade;
    }

    public Map<String, SystemManager> getSystemManagerMap() {
        return systemManagerMap;
    }

    private void createFirstAdmin() {
        userFacade.createAdmin();
    }

    public void addAdmin(String adminName, SystemManager systemManager) {
        systemManagerMap.put(adminName, systemManager);
    }

    public void register(String username, String pass) throws Exception {
        userFacade.registerUser(username, pass);
    }

    public void login(String username, String pass) throws Exception {
        userFacade.logIn(username, pass);
    }

    public void systemStart() {
        userFacade.systemStart();
    }

    public void logout(String userName, String pass) {
        userFacade.logout(userName, pass);
    }

    public void addOwner(int userID, int userToAddID, int storeID) {
        userFacade.addOwner(userID, userToAddID, storeID);
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

    public void closeStorePermanently(String username, int storeID) throws Exception
     {
        if (isAdmin(username)) {
            SystemManager systemManager = systemManagerMap.get(username);
            systemManager.closeStorePermanently(storeFacade.getStore(storeID));
        }
        throw new RuntimeException("Only admin can close stores permanently");
    }

    private boolean isAdmin(String username) {
        return systemManagerMap.get(username) != null;
    }

    public void removeUser(String username, String userToRemove) throws Exception {
        if (isAdmin(username)) {
            SystemManager systemManager = systemManagerMap.get(username);
            systemManager.removeUser(userFacade.getUser(userToRemove));
        }
        else
            throw new RuntimeException("Only System admin can remove a user");
    }

    public int addStore(int founderID, String name)
    {
        Store store = storeFacade.addStore(founderID, name);
        userFacade.addStore(founderID, store);
        return store.getStoreID();
    }
}
