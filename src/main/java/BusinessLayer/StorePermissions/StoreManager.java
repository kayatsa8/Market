package BusinessLayer.StorePermissions;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StoreManager extends StoreEmployees {

    private ConcurrentHashMap<StoreActionPermissions, Integer> map;
    private Set<StoreActionPermissions> storeActionPermissions;

    public StoreManager(int id, StoreOwner storeOwnerShip) {
        super(id, storeOwnerShip.getUserID(),storeOwnerShip.getStore());
        map = new ConcurrentHashMap<>();
        storeActionPermissions = map.newKeySet();
    }

    public void addPermission(StoreActionPermissions permission) {
        this.storeActionPermissions.add(permission);
    }

    public void removePermission(StoreActionPermissions permission) {
        this.storeActionPermissions.remove(permission);
    }

    public boolean hasPermission(StoreActionPermissions permission) {
        return this.storeActionPermissions.contains(permission);
    }
}
