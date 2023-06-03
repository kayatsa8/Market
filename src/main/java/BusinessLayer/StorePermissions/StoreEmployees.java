package BusinessLayer.StorePermissions;

import BusinessLayer.Stores.Store;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class StoreEmployees {
    @Id
    private int userID;
    private int parentID;
    @Transient
    private Store store;

    public StoreEmployees(int userID, int parentID, Store store) {
        this.userID = userID;
        this.parentID = parentID;
        this.store = store;
    }

    public StoreEmployees() {

    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public int getStoreID() {
        return store.getStoreID();
    }

    public abstract boolean hasPermission(StoreActionPermissions permission);

}
