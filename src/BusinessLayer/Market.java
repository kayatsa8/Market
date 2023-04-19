package BusinessLayer;

import BusinessLayer.Stores.CatalogItem;
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
    private Map<Integer, SystemManager> systemManagerMap;
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

    public Map<Integer, SystemManager> getSystemManagerMap() {
        return systemManagerMap;
    }

    private void createFirstAdmin() {
        userFacade.createAdmin();
    }

    public void addAdmin(int newAdmin, SystemManager systemManager) {
        systemManagerMap.put(newAdmin, systemManager);
    }

    public int register(String username, String pass) throws Exception {
        return userFacade.registerUser(username, pass);
    }

    public int login(String username, String pass) throws Exception {
       return userFacade.logIn(username, pass);
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

    public void addManager(int userID, int userToAdd, int storeID) {
        userFacade.addManager(userID, userToAdd, storeID);
    }

    public void removeOwner(int userID, int userToRemove, int storeID) {
        userFacade.removeOwner(userID, userToRemove, storeID);
    }

    public void removeManager(int userID, int userToRemove, int storeID) {
        userFacade.removeManager(userID, userToRemove, storeID);
    }

    public void closeStorePermanently(int userID, int storeID) throws Exception
     {
        if (isAdmin(userID)) {
            SystemManager systemManager = systemManagerMap.get(userID);
            systemManager.closeStorePermanently(storeFacade.getStore(storeID));
        }
        else {
            throw new RuntimeException("Only admin can close stores permanently");
        }
    }

    private boolean isAdmin(int userID) {
        return systemManagerMap.get(userID) != null;
    }

    public void removeUser(int userID, int userToRemove) throws Exception {
        if (isAdmin(userID)) {
            SystemManager systemManager = systemManagerMap.get(userID);
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

    public Map<CatalogItem, Boolean> getCatalog() {
        return storeFacade.getCatalog();
    }
}
