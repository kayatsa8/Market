package BusinessLayer.Users;

import BusinessLayer.StorePermissions.StoreManager;
import BusinessLayer.StorePermissions.StoreOwner;

import java.util.*;

public class RegisteredUser extends User{
    private String username;
    private String password;
    private int id;

    public int getId() {
        return id;
    }

    public Map<Integer, StoreOwner> getStoresIOwn() {
        return storesIOwn;
    }

    public Map<Integer, StoreManager> getStoresIManage() {
        return storesIManage;
    }

    private Map<Integer, StoreOwner> storesIOwn;
    private Map<Integer, StoreManager> storesIManage;

    public RegisteredUser(String username, String pass, int id) {
        this.username = username;
        this.password = pass;
        this.id = id;
        this.storesIOwn = new HashMap<>();
        this.storesIManage = new HashMap<>();
    }

    private int getID() {
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

    private boolean ownsStore(int storeID) {
        return (storesIOwn.get(storeID)!=null);
    }

    public StoreManager getStoreIManage(int storeID) {
        return managesStore(storeID) ? storesIManage.get(storeID) : null;
    }

    private boolean managesStore(int storeID) {
        return (storesIManage.get(storeID)!=null);
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

    public void addStoreOwnership(StoreOwner storeOwnership) {
        int storeID = storeOwnership.getStoreID();
        storesIOwn.put(storeID, new StoreOwner(this.getID(), storeOwnership));
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
        storesIManage.put(storeID, new StoreManager(this.getID(), storeOwnerShip));
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

    public void removeManager(RegisteredUser managerToRemove, int storeID) {
        StoreOwner storeOwnership = getStoreIOwn(storeID);
        if (storeOwnership==null) {
            throw new RuntimeException("User is not a store owner");
        }
        if (!managerToRemove.managesStore(storeID)) {
            throw new RuntimeException("Owner to remove doesn't own store");
        }
        storeOwnership.removeManager(managerToRemove);
    }

    public void removeManagership(int storeID) {
        storesIManage.remove(storeID);
    }

    public void removeOwnership(int storeID) {
        storesIOwn.remove(storeID);
    }
}
