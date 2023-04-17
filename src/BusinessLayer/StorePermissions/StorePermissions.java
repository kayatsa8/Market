package BusinessLayer.StorePermissions;

import BusinessLayer.Stores.Store;

public abstract class StorePermissions {
    private int userID;
    private int parentID;
    private Store store;

    public StorePermissions(int userID, int parentID, Store store) {
        this.userID = userID;
        this.parentID = parentID;
        this.store = store;
    }

    public int getUserID() {
        return userID;
    }

    public int getParentID() {
        return parentID;
    }

    public Store getStore() {
        return store;
    }

    public int getStoreID() {
        return store.getStoreID();
    }

}
