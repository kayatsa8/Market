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
    static DBConnector<Store> connector = new DBConnector<>(Store.class, Market.getConfigurations_static());
    static DBConnector<CatalogItem> itemDBConnector = new DBConnector<>(CatalogItem.class, Market.getConfigurations_static());
    public StoreDAO() {
        connector = new DBConnector<>(Store.class, Market.getConfigurations_static());
        itemDBConnector = new DBConnector<>(CatalogItem.class, Market.getConfigurations_static());
    }
    public static void addStore(Store store) {
        connector.insert(store);
    }

    public static void removeUser(Store store) throws Exception {
        connector.delete(store.getStoreID());
    }

    public static Map<Integer, Store> getStores() {
        Map<Integer, Store> storeMap = new HashMap<>();
        List<Store> stores = connector.getAll();
        for (Store store : stores) {
            storeMap.put(store.getStoreID(), store);
        }
        return storeMap;
    }

    public static void addItem(CatalogItem newItem) {
        itemDBConnector.insert(newItem);
    }
    public static void removeItem(CatalogItem item) {
        itemDBConnector.delete(item.getItemID());
    }

    public static List<CatalogItem> getItems() {
        return itemDBConnector.getAll();
    }
}
