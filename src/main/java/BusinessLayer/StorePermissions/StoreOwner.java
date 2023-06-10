package BusinessLayer.StorePermissions;

import BusinessLayer.Stores.Store;
import BusinessLayer.Users.RegisteredUser;
import DataAccessLayer.StoreEmployeesDAO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "storeOwners")
public class StoreOwner extends StoreEmployees {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "storeowners_ownersDefined")//,
    private Set<RegisteredUser> ownersIDefined;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "storeowners_managersDefined")
    private Set<RegisteredUser> managersIDefined;
    @Transient
    private StoreEmployeesDAO employeesDAO;
    /*
    founder calls this constructor
     */
    public StoreOwner(int userID, Store store) {
        super(userID, userID, store);
        store.addOwner(this);
        this.ownersIDefined = new HashSet<>();
        this.managersIDefined = new HashSet<>();
        this.employeesDAO = new StoreEmployeesDAO();
    }

    public StoreOwner(int userID, StoreOwner parentStoreOwnership) {
        super(userID, parentStoreOwnership.getUserID(), parentStoreOwnership.getStore());
        this.ownersIDefined = new HashSet<>();
        this.managersIDefined = new HashSet<>();
        this.employeesDAO = new StoreEmployeesDAO();
    }

    public StoreOwner() {
        super();
    }

    public Set<RegisteredUser> getOwnersIDefined() {
        return ownersIDefined;
    }

    public void setOwnersIDefined(Set<RegisteredUser> ownersIDefined) {
        this.ownersIDefined = ownersIDefined;
    }

    public Set<RegisteredUser> getManagersIDefined() {
        return managersIDefined;
    }

    public void setManagersIDefined(Set<RegisteredUser> managersIDefined) {
        this.managersIDefined = managersIDefined;
    }

    public boolean isFounder() {
        return this.getUserID() == this.getParentID();
    }

    public void addOwner(RegisteredUser newOwner) {
        ownersIDefined.add(newOwner);
        employeesDAO.save(this);
        StoreOwner owner = newOwner.addStoreOwnership(this);
        this.getStore().addOwner(owner);
    }

    public void addManager(RegisteredUser newManager) {
        managersIDefined.add(newManager);
        employeesDAO.save(this);
        newManager.addStoreManagership(this);
        this.getStore().addManager(newManager.getStoreIManage(getStoreID()));
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
        this.getStore().removeOwner(ownership);
        ownerToRemove.removeOwnership(this.getStoreID());
        employeesDAO.save(this);
    }

    private void destruct() {
        StoreOwner ownership;
        for (RegisteredUser manager : managersIDefined) {
            this.getStore().removeManager(manager.getStoreIManage(getStoreID()));
            manager.removeManagership(this.getStoreID());
        }
        for (RegisteredUser owner : ownersIDefined) {
            ownership = owner.getStoreIOwn(this.getStoreID());
            ownership.destruct();
            this.getStore().removeOwner(owner.getStoreIOwn(getStoreID()));
            owner.removeOwnership(this.getStoreID());
        }

    }

    public void removeManager(RegisteredUser managerToRemove) {
        if (!managersIDefined.contains(managerToRemove)) {
            throw new RuntimeException("This user is not the one who defined this owner");
        }
        this.getStore().removeManager(managerToRemove.getStoreIManage(getStoreID()));
        managerToRemove.removeManagership(this.getStoreID());
        managersIDefined.remove(managerToRemove);
        employeesDAO.save(this);
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

    public void addManagerPermission(RegisteredUser manager, StoreActionPermissions permission) {
        if (!managersIDefined.contains(manager)) {
            throw new RuntimeException("This user is not the one who defined this owner");
        }
        manager.getStoreIManage(this.getStoreID()).addPermission(permission);
    }

    public void removeManagerPermission(RegisteredUser manager, StoreActionPermissions permission) {
        if (!managersIDefined.contains(manager)) {
            throw new RuntimeException("This user is not the one who defined this owner");
        }
        manager.getStoreIManage(this.getStoreID()).removePermission(permission);
    }

    public boolean hasPermission(StoreActionPermissions permission) {
        return true;
    }
}
