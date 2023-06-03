package BusinessLayer.Users;

import BusinessLayer.Market;
import BusinessLayer.MarketMock;
import BusinessLayer.NotificationSystem.Chat;
import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.NotificationSystem.Observer.NotificationObserver;
import BusinessLayer.NotificationSystem.UserMailbox;
import BusinessLayer.StorePermissions.StoreActionPermissions;
import BusinessLayer.StorePermissions.StoreManager;
import BusinessLayer.StorePermissions.StoreOwner;
import BusinessLayer.Stores.Store;
import DataAccessLayer.UserDAO;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.*;

public class RegisteredUser extends User {
    private String username;
    private String password;
    private UserDAO userDAO;
    private Map<Integer, StoreOwner> storesIOwn;
    private Map<Integer, StoreManager> storesIManage;
    private SystemManager systemManager;
    private boolean isLoggedIn;
    public RegisteredUser(String username, String pass, int id, String address, LocalDate bDay) throws Exception {
        super(id);

        this.username = username;
        this.password = Password.hashPassword(pass);
        this.id = id;
        this.bDay = bDay;
        this.address=address;
        this.storesIOwn = new HashMap<>();
        this.storesIManage = new HashMap<>();
        this.userDAO = new UserDAO();
        this.isLoggedIn = false;
        this.mailbox = Market.getInstance().getNotificationHub().registerToMailService(id, this);
    }

    public RegisteredUser(String username, String pass, int id, boolean isAdmin) throws Exception {
        super(id);
        this.username = username;
        this.password = Password.hashPassword(pass);
        this.storesIOwn = new HashMap<>();
        this.storesIManage = new HashMap<>();
        this.userDAO = new UserDAO();
        this.isLoggedIn = false;
        if (isAdmin) {
            systemManager = new SystemManager(this);
        }
        this.mailbox = Market.getInstance().getNotificationHub().registerToMailService(id, this);
    }
    public RegisteredUser(String username, String pass, int id, boolean isAdmin, MarketMock marketMock) throws Exception {
        super(id);
        this.username = username;
        this.password = Password.hashPassword(pass);
        this.storesIOwn = new HashMap<>();
        this.storesIManage = new HashMap<>();
        this.userDAO = new UserDAO();
        this.isLoggedIn = false;
        if (isAdmin) {
            systemManager = new SystemManager(this);
        }
        this.mailbox = marketMock.getNotificationHub().registerToMailService(id,this);
    }
    public RegisteredUser(String username, String pass, int id, MarketMock marketMock) throws Exception {
        super(id);
        this.username = username;
        this.password = Password.hashPassword(pass);
        this.storesIOwn = new HashMap<>();
        this.storesIManage = new HashMap<>();
        this.userDAO = new UserDAO();
        this.isLoggedIn = false;
        this.mailbox = marketMock.getNotificationHub().registerToMailService(id,this);
    }
    public SystemManager makeAdmin() throws Exception {
        systemManager = new SystemManager(this);
        return systemManager;
    }
    public Map<Integer, StoreOwner> getStoresIOwn() {
        return storesIOwn;
    }

    public Map<Integer, StoreManager> getStoresIManage() {
        return storesIManage;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
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
        return systemManager != null;
    }

    private boolean ownsStore(int storeID) {
        return (storesIOwn.get(storeID) != null);
    }

    public StoreManager getStoreIManage(int storeID) {
        return managesStore(storeID) ? storesIManage.get(storeID) : null;
    }

    private boolean managesStore(int storeID) {
        return (storesIManage.get(storeID) != null);
    }

    public void addStore(Store store) {
        storesIOwn.put(store.getStoreID(), new StoreOwner(this.getId(), store));
    }

    public void addOwner(RegisteredUser newOwner, int storeID) throws RuntimeException {
        //ensure I am an owner
        StoreOwner storeOwnership = getStoreIOwn(storeID);
        if (storeOwnership == null) {
            throw new RuntimeException("User is not a store owner");
        }
        //check if newOwner is already an owner or manager//TODO can user be both?
        if (newOwner.ownsStore(storeID)) {
            throw new RuntimeException("User " + newOwner.username + " already owns store " + storeID);
        }
        if (newOwner.managesStore(storeID)) {
            throw new RuntimeException("User already manages store");
        }
        storeOwnership.addOwner(newOwner);
        mailbox.sendMessage(newOwner.getId(), "You have been appointed owner of Store: " + storeOwnership.getStore().getStoreName());
    }

    public StoreOwner addStoreOwnership(StoreOwner storeOwnership) {
        int storeID = storeOwnership.getStoreID();
        StoreOwner ownership = new StoreOwner(this.getId(), storeOwnership);
        storesIOwn.put(storeID, ownership);
        mailbox.getChats().putIfAbsent(storeID, new Chat(id, storeID));
        return ownership;
    }

    public void addManager(RegisteredUser newManager, int storeID) {
        //ensure I am an owner
        StoreOwner storeOwnership = getStoreIOwn(storeID);
        if (storeOwnership == null) {
            throw new RuntimeException("User is not a store owner");
        }
        if (newManager.ownsStore(storeID)) {
            throw new RuntimeException("User already owns store");
        }
        if (newManager.managesStore(storeID)) {
            throw new RuntimeException("User already manages store");
        }
        storeOwnership.addManager(newManager);
        mailbox.sendMessage(newManager.getId(), "You have been appointed owner of Store: " + storeOwnership.getStore().getStoreName());
    }

    public void addStoreManagership(StoreOwner storeOwnerShip) {
        int storeID = storeOwnerShip.getStoreID();
        storesIManage.put(storeID, new StoreManager(this.getId(), storeOwnerShip));
    }

    public void removeOwner(RegisteredUser ownerToRemove, int storeID) {
        StoreOwner storeOwnership = getStoreIOwn(storeID);
        if (storeOwnership == null) {
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
        if (storeOwnership == null) {
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
        if (storeOwnership == null) {
            throw new RuntimeException("User is not a store owner");
        }
        if (!manager.managesStore(storeID)) {
            throw new RuntimeException("Manager doesn't manage store");
        }
        storeOwnership.addManagerPermission(manager, permission);
    }

    public void removeManagerPermission(int storeID, RegisteredUser manager, StoreActionPermissions permission) {
        StoreOwner storeOwnership = getStoreIOwn(storeID);
        if (storeOwnership == null) {
            throw new RuntimeException("User is not a store owner");
        }
        if (!manager.managesStore(storeID)) {
            throw new RuntimeException("Manager doesn't manage store");
        }
        storeOwnership.removeManagerPermission(manager, permission);
    }

    public void logIn() {
        this.isLoggedIn = true;
    }

    public void logout() {
        this.isLoggedIn = false;
    }


    public Map<RegisteredUser, Set<Integer>> getAllOwnersIDefined() {
        Map<Integer, StoreOwner> storeOwnership = getStoresIOwn();
        if (storeOwnership == null) {
            throw new RuntimeException("User is not a store owner");
        }
        Map<RegisteredUser, Set<Integer>> owners = new HashMap<>();
        for(StoreOwner storeOwner: storeOwnership.values()){
            Set<RegisteredUser> currUsers = storeOwner.getOwnersIDefined();
            for (RegisteredUser user: currUsers){
                if(!owners.containsKey(user)){
                    Set<Integer> set = new HashSet<>();
                    set.add(storeOwner.getStoreID());
                    owners.put(user, set);
                }
                else{
                    owners.get(user).add(storeOwner.getStoreID());
                }
            }
        }
        return owners;
    }

    public Map<RegisteredUser, Set<Integer>> getAllManagersIDefined() {
        Map<Integer, StoreOwner> storeOwnership = getStoresIOwn();
        if (storeOwnership == null) {
            throw new RuntimeException("User is not a store owner");
        }
        Map<RegisteredUser, Set<Integer>> managers = new HashMap<>();
        for(StoreOwner owner: storeOwnership.values()){
            Set<RegisteredUser> currUsers = owner.getManagersIDefined();
            for (RegisteredUser user: currUsers){
                if(!managers.containsKey(user)){
                    Set<Integer> set = new HashSet<>();
                    set.add(owner.getStoreID());
                    managers.put(user, set);
                }
                else{
                    managers.get(user).add(owner.getStoreID());
                }
            }
        }

        return managers;
    }

    public LocalDate getbDay() {
        return bDay;
    }

    public void setbDay(LocalDate bDay) {
        this.bDay = bDay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
