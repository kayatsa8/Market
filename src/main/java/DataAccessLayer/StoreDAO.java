package DataAccessLayer;

import BusinessLayer.Market;
import BusinessLayer.Stores.Store;
import DataAccessLayer.Hibernate.DBConnector;

import java.util.HashMap;
import java.util.Map;

//DB mock
public class StoreDAO {
    DBConnector<Store> connector;
    public StoreDAO() {
        connector = new DBConnector<>(Store.class, Market.getConfigurations_static());
    }
    public void addStore(Store store) {
        connector.insert(store);
    }

    public void removeUser(Store store) throws Exception {
        connector.delete(store.getStoreID());
    }

    public Map<Integer, Store> getStores() {
        return new HashMap<>();
//        List<Store> stores = connector.getAll();
//        Map<Integer, Store> res = new HashMap<>();
//        for (Store store : stores) {
//            res.put(store.getStoreID(), store);
//        }
//        return res;
    }
}
