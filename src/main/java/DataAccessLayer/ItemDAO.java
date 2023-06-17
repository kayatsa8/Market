package DataAccessLayer;

import BusinessLayer.Market;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;
import DataAccessLayer.Hibernate.DBConnector;

import java.util.HashMap;
import java.util.Map;

//DB mock
public class ItemDAO {
    public ItemDAO() {
    }

    public DBConnector<CatalogItem> getItemDBConnector() {
        try {
            return new DBConnector<>(CatalogItem.class, Market.getInstance().getConfigurations());
        }
        catch (Exception e) {
        }
        return null;
    }
    public void addItem(CatalogItem newItem) {
        getItemDBConnector().insert(newItem);
    }
    public void removeItem(CatalogItem item) {
        getItemDBConnector().delete(item.getItemID());
    }
    public void save(CatalogItem item) {
        getItemDBConnector().saveState(item);
    }
}
