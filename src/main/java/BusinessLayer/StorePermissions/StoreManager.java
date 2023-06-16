package BusinessLayer.StorePermissions;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StoreManager extends StoreEmployees {


    public Set<StoreActionPermissions> getStoreActionPermissions() {
        return storeActionPermissions;
    }

    private Set<StoreActionPermissions> storeActionPermissions;

    public StoreManager(int id, StoreOwner storeOwnerShip) {
        super(id, storeOwnerShip.getUserID(),storeOwnerShip.getStore());
        storeActionPermissions = new HashSet<>();
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
