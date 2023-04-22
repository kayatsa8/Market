package BusinessLayer.StorePermissions;

import BusinessLayer.Stores.Store;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StoreManager extends StorePermissions{

    private ConcurrentHashMap<StoreActionPermissions, Integer> map;
    private Set<StoreActionPermissions> storeActionPermissions;

    public StoreManager(int id, StoreOwner storeOwnerShip) {
        super(id, storeOwnerShip.getUserID(),storeOwnerShip.getStore());
        map = new ConcurrentHashMap<>();
        storeActionPermissions = map.newKeySet();
    }

    public boolean addPermission(StoreActionPermissions permission) {
        return this.storeActionPermissions.add(permission);
    }

    public boolean removePermission(StoreActionPermissions permission) {
        return this.storeActionPermissions.remove(permission);
    }

    protected boolean hasPermission(StoreActionPermissions permission) {
        return this.storeActionPermissions.contains(permission);
    }
}
