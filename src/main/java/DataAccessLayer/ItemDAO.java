package DataAccessLayer;

import BusinessLayer.Market;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;
import DataAccessLayer.Hibernate.DBConnector;

import java.util.HashMap;
import java.util.Map;

//DB mock
public class ItemDAO {
    DBConnector<CatalogItem> itemDBConnector;
    public ItemDAO() {
        itemDBConnector = new DBConnector<>(CatalogItem.class, Market.getConfigurations_static());
    }


    public void addItem(CatalogItem newItem) {
        itemDBConnector.insert(newItem);
    }
    public void removeItem(CatalogItem item) {
        itemDBConnector.delete(item.getItemID());
    }
    public void save(CatalogItem item) {
        itemDBConnector.saveState(item);
    }
}
