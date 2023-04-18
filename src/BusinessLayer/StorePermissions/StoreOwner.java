package BusinessLayer.StorePermissions;

import BusinessLayer.Stores.Store;
import BusinessLayer.Users.RegisteredUser;

import java.util.HashSet;
import java.util.Set;

public class StoreOwner extends StorePermissions {
    private Set<RegisteredUser> ownersIDefined;
    private Set<RegisteredUser> managersIDefined;

    /*
    founder calls this constructor
     */
    public StoreOwner(int userID, Store store) {
        super(userID, userID, store);
        this.ownersIDefined = new HashSet<>();
        this.managersIDefined = new HashSet<>();
    }

    public StoreOwner(int userID, StoreOwner parentStoreOwnership) {
        super(userID, parentStoreOwnership.getUserID(), parentStoreOwnership.getStore());
        this.ownersIDefined = new HashSet<>();
        this.managersIDefined = new HashSet<>();
    }

    public Set<RegisteredUser> getOwnersIDefined() {
        return ownersIDefined;
    }

    public Set<RegisteredUser> getManagersIDefined() {
        return managersIDefined;
    }

    public boolean isFounder() {
        return this.getUserID() == this.getParentID();
    }

    public void addOwner(RegisteredUser newOwner) {
        ownersIDefined.add(newOwner);
        newOwner.addStoreOwnership(this);
        this.getStore().addOwner(newOwner.getId());
    }

    public void addManager(RegisteredUser newManager) {
        managersIDefined.add(newManager);
        newManager.addStoreManagership(this);
        this.getStore().addManager(newManager.getId());
    }

    public void removeOwner(RegisteredUser ownerToRemove) {
        if (!ownersIDefined.contains(ownerToRemove)) {
            throw new RuntimeException("This user is not the one who defined this owner");
        }
        if (ownerToRemove.getId() == this.getUserID() && this.isFounder()) {
            throw new RuntimeException("This user is Founder of the store and cannot remove himself");
        }
        StoreOwner ownership = ownerToRemove.getStoreIOwn(this.getStoreID());
        ownership.destruct();
        this.ownersIDefined.remove(ownerToRemove);
        this.getStore().removeOwner(ownerToRemove.getId());
        ownerToRemove.removeOwnership(this.getStoreID());
    }

    private void destruct() {
        StoreOwner ownership;
        for (RegisteredUser manager : managersIDefined) {
            manager.removeManagership(this.getStoreID());
            this.getStore().removeManager(manager.getId());
        }
        for (RegisteredUser owner : ownersIDefined) {
            ownership = owner.getStoreIOwn(this.getStoreID());
            ownership.destruct();
            owner.removeOwnership(this.getStoreID());
            this.getStore().removeOwner(owner.getId());
        }

    }

    public void removeManager(RegisteredUser managerToRemove) {
        if (!managersIDefined.contains(managerToRemove)) {
            throw new RuntimeException("This user is not the one who defined this owner");
        }
        managerToRemove.removeManagership(this.getStoreID());
        managersIDefined.remove(managerToRemove);
        this.getStore().removeManager(managerToRemove.getId());
    }

    public void closeStore() {
        if (!this.isFounder()) {
            throw new RuntimeException("process to initiate store closing must be through founder");
        }
        destruct();
    }

    public int findChild(RegisteredUser child) {
        Integer res = null;
        if (this.getUserID() == child.getId()) {
            return child.getId();
        }
        //can be same bc managers dont define other managers
        if (this.ownersIDefined.contains(child) || this.managersIDefined.contains(child)) {
            return this.getUserID();
        }
        //DFS on owners
        for (RegisteredUser owner : ownersIDefined) {
            res = owner.getStoreIOwn(this.getStoreID()).findChild(child);
            if (res != null) {
                break;
            }
        }
        return res;
    }
}
