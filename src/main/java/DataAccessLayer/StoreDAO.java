package DataAccessLayer;

import BusinessLayer.Market;
import BusinessLayer.Stores.Store;
import BusinessLayer.Users.RegisteredUser;
import BusinessLayer.Users.UserFacade;
import DataAccessLayer.Hibernate.DBConnector;

import java.util.HashMap;

//DB mock
public class StoreDAO {
    DBConnector<Store> connector;
    public StoreDAO() {
        connector = new DBConnector<>(Store.class, Market.getConfigurations());
    }
    public void addStore(Store store) {
        connector.insert(store);
    }

    public void removeUser(Store store) throws Exception {
        connector.delete(store.getStoreID());
    }

}
