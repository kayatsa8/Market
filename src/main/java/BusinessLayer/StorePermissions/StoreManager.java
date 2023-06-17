package BusinessLayer.StorePermissions;

import java.util.HashSet;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "storeManagers")
public class StoreManager extends StoreEmployees {

    @Transient
    private ConcurrentHashMap<StoreActionPermissions, Integer> map;

    public StoreManager() {
        super();

    }
    @Transient
    private Set<StoreActionPermissions> storeActionPermissions;

    public Set<StoreActionPermissions> getStoreActionPermissions() {
        return storeActionPermissions;
    }

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
