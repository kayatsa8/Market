package BusinessLayer.Users;

import BusinessLayer.Cart;
import BusinessLayer.StorePermissions.StoreActionPermissions;
import BusinessLayer.StorePermissions.StoreManager;
import BusinessLayer.StorePermissions.StoreOwner;
import BusinessLayer.Stores.Store;
import DataAccessLayer.UserDAO;

import java.util.*;

public class RegisteredUser extends User{
    private String username;
    private String password;
    private int id;
    private UserDAO userDAO;

    public Map<Integer, StoreOwner> getStoresIOwn() {
        return storesIOwn;
    }

    public Map<Integer, StoreManager> getStoresIManage() {
        return storesIManage;
    }

    private Map<Integer, StoreOwner> storesIOwn;
    private Map<Integer, StoreManager> storesIManage;
    private SystemManager systemManager;

    public RegisteredUser(String username, String pass, int id) {
        this.username = username;
        this.password = pass;
        this.id = id;
        this.storesIOwn = new HashMap<>();
        this.storesIManage = new HashMap<>();
        this.userDAO = new UserDAO();
        this.cart = new Cart(id);
    }

    public RegisteredUser(String username, String pass, int id, boolean isAdmin) {
        this.username = username;
        this.password = pass;
        this.id = id;
        this.storesIOwn = new HashMap<>();
        this.storesIManage = new HashMap<>();
        this.userDAO = new UserDAO();
        this.cart = new Cart(id);
        if (isAdmin) {
            systemManager = new SystemManager(this);
        }
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public StoreOwner getStoreIOwn(int storeID) {
        return ownsStore(storeID) ? storesIOwn.get(storeID) : null;
    }

    private boolean isAdmin() {
        return systemManager!=null;
    }

    private boolean ownsStore(int storeID) {
        return (storesIOwn.get(storeID)!=null);
    }

    public StoreManager getStoreIManage(int storeID) {
        return managesStore(storeID) ? storesIManage.get(storeID) : null;
    }

    private boolean managesStore(int storeID) {
        return (storesIManage.get(storeID)!=null);
    }

    public void addStore(Store store) {
        storesIOwn.put(store.getStoreID(), new StoreOwner(this.getId(), store));
    }

    public void addOwner(RegisteredUser newOwner, int storeID) throws RuntimeException{
        //ensure I am an owner
        StoreOwner storeOwnership = getStoreIOwn(storeID);
        if (storeOwnership==null) {
            throw new RuntimeException("User is not a store owner");
        }
        //check if newOwner is already an owner or manager//TODO can user be both?
        if (newOwner.ownsStore(storeID)) {
            throw new RuntimeException("User already owns store");
        }
        if (newOwner.managesStore(storeID)) {
            throw new RuntimeException("User already manages store");
        }
        storeOwnership.addOwner(newOwner);
    }

    public StoreOwner addStoreOwnership(StoreOwner storeOwnership) {
        int storeID = storeOwnership.getStoreID();
        StoreOwner ownership = new StoreOwner(this.getId(), storeOwnership);
        storesIOwn.put(storeID, ownership);
        return ownership;
    }

    public void addManager(RegisteredUser newManager, int storeID) {
        //ensure I am an owner
        StoreOwner storeOwnership = getStoreIOwn(storeID);
        if (storeOwnership==null) {
            throw new RuntimeException("User is not a store owner");
        }
        if (newManager.ownsStore(storeID)) {
            throw new RuntimeException("User already owns store");
        }
        if (newManager.managesStore(storeID)) {
            throw new RuntimeException("User already manages store");
        }

        storeOwnership.addManager(newManager);
    }

    public void addStoreManagership(StoreOwner storeOwnerShip) {
        int storeID = storeOwnerShip.getStoreID();
        storesIManage.put(storeID, new StoreManager(this.getId(), storeOwnerShip));
    }

    public void removeOwner(RegisteredUser ownerToRemove, int storeID) {
        StoreOwner storeOwnership = getStoreIOwn(storeID);
        if (storeOwnership==null) {
            throw new RuntimeException("User is not a store owner");
        }
        if (!ownerToRemove.ownsStore(storeID)) {
            throw new RuntimeException("Owner to remove doesn't own store");
        }
        storeOwnership.removeOwner(ownerToRemove);
    }

    public void closeStore(RegisteredUser founder, int storeID) {
        StoreOwner storeOwnership = founder.getStoreIOwn(storeID);
        storeOwnership.closeStore();
        founder.removeOwnership(storeID);
    }

    public void removeManager(RegisteredUser managerToRemove, int storeID) {
        StoreOwner storeOwnership = getStoreIOwn(storeID);
        if (storeOwnership==null) {
            throw new RuntimeException("User is not a store owner");
        }
        if (!managerToRemove.managesStore(storeID)) {
            throw new RuntimeException("Manager to remove doesn't manage store");
        }
        storeOwnership.removeManager(managerToRemove);
    }

    public void removeManagership(int storeID) {
        storesIManage.remove(storeID);
        userDAO.removeManagership(this.getId(), storeID);
    }

    public void removeOwnership(int storeID) {
        storesIOwn.remove(storeID);
        userDAO.removeOwnership(this.getId(), storeID);
    }

    public void addManagerPermission(int storeID, RegisteredUser manager, StoreActionPermissions permission) {
        StoreOwner storeOwnership = getStoreIOwn(storeID);
        if (storeOwnership==null) {
            throw new RuntimeException("User is not a store owner");
        }
        if (!manager.managesStore(storeID)) {
            throw new RuntimeException("Manager doesn't manage store");
        }
        storeOwnership.addManagerPermission(manager, permission);
    }

    public void removeManagerPermission(int storeID, RegisteredUser manager, StoreActionPermissions permission) {
        StoreOwner storeOwnership = getStoreIOwn(storeID);
        if (storeOwnership==null) {
            throw new RuntimeException("User is not a store owner");
        }
        if (!manager.managesStore(storeID)) {
            throw new RuntimeException("Manager doesn't manage store");
        }
        storeOwnership.removeManagerPermission(manager, permission);
    }

}
