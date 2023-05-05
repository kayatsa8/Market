package ServiceLayer.Objects;

import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;
import BusinessLayer.Stores.StoreStatus;

public class StoreService {

    public final int founderID;
    public final String storeName;
    public final int storeID;
    private Store store;
    public StoreService(Store store)
    {
        storeID = store.getStoreID();
        storeName = store.getStoreName();
        founderID = store.getFounderID();
        this.store = store;

    }

    public boolean hasItem(int itemId) {
        return false;
    }

    public int getStoreId() {
        return storeID;
    }

    public CatalogItemService getItem(int id){
        CatalogItem item = store.getItem(id);
        if(item == null)
            return null;
        return new CatalogItemService(item, store.getItemAmount(id)>0);
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreStatus(){
        StoreStatus status = store.getStoreStatus();
        switch (status){
            case OPEN -> {
                return "Open";
            }
            case CLOSE -> {
                return "Close";
            }
            case PERMANENTLY_CLOSE -> {
                return "Permanently close";
            }
        }
        return "";
    }
}
