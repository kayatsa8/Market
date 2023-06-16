package DataAccessLayer;

import BusinessLayer.Market;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;
import DataAccessLayer.Hibernate.DBConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//DB mock
public class StoreDAO {
    DBConnector<Store> connector;
    DBConnector<CatalogItem> itemDBConnector;
    public StoreDAO() throws Exception {
        connector = new DBConnector<>(Store.class, Market.getInstance().getConfigurations());
        itemDBConnector = new DBConnector<>(CatalogItem.class, Market.getInstance().getConfigurations());
    }
    public void addStore(Store store) {
        connector.insert(store);
    }

    public void removeUser(Store store) throws Exception {
        connector.delete(store.getStoreID());
    }

    public Map<Integer, Store> getStores() {
        Map<Integer, Store> storeMap = new HashMap<>();
        List<Store> stores = connector.getAll();
        for (Store store : stores) {
            storeMap.put(store.getStoreID(), store);
        }
        return storeMap;
    }

    public void addItem(CatalogItem newItem) {
        itemDBConnector.insert(newItem);
    }
    public void removeItem(CatalogItem item) {
        itemDBConnector.delete(item.getItemID());
    }

    public List<CatalogItem> getItems() {
        return itemDBConnector.getAll();
    }
}
