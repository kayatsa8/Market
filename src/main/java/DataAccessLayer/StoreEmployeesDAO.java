package DataAccessLayer;

import BusinessLayer.Market;
import BusinessLayer.StorePermissions.StoreManager;
import BusinessLayer.StorePermissions.StoreOwner;
import BusinessLayer.Users.RegisteredUser;
import DataAccessLayer.Hibernate.DBConnector;

import java.util.HashMap;

//DB mock
public class StoreEmployeesDAO {
    DBConnector<StoreOwner> soConnector;
    DBConnector<StoreManager> smConnector;
    public StoreEmployeesDAO() throws Exception {
        soConnector = new DBConnector<>(StoreOwner.class, Market.getInstance().getConfigurations());
        smConnector = new DBConnector<>(StoreManager.class, Market.getInstance().getConfigurations());
    }


    public void addOwner(StoreOwner owner) {
        soConnector.saveState(owner);
    }

    public void removeOwner(StoreOwner owner) {
//        soConnector.delete(owner.getUserID()); //TODO Change
    }

    public void removeOwnership(StoreOwner owner) {
        soConnector.saveState(owner);
    }

    public void addManager(StoreManager manager) {
        smConnector.saveState(manager);
    }

    public void removeManager(StoreManager manager) {
//        smConnector.delete(owner.getUserID()); //TODO Change
    }

    public void removeManagership(StoreManager manager) {
        smConnector.saveState(manager);
    }

    public void save(StoreOwner storeOwner) {
        soConnector.saveState(storeOwner);
    }
}
