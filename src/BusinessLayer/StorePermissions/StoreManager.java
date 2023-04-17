package BusinessLayer.StorePermissions;

import BusinessLayer.Stores.Store;

public class StoreManager extends StorePermissions{
    public StoreManager(int id, StoreOwner storeOwnerShip) {
        super(id, storeOwnerShip.getUserID(),storeOwnerShip.getStore());
    }
}
