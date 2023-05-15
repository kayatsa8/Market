package ServiceLayer.Objects;

import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;
import BusinessLayer.Stores.StoreStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreService {

    public final int founderID;
    public final String storeName;
    public final int storeID;
    private Store store;
    private Map<CatalogItemService, Integer>  items = new HashMap<>();
    public StoreService(Store store)
    {
        storeID = store.getStoreID();
        storeName = store.getStoreName();
        founderID = store.getFounderID();
        this.store = store;
        loadItemsAndAmountsFromStore(store);

    }


    private Map<CatalogItemService, Integer> loadItemsAndAmountsFromStore(Store store) {
        Map<CatalogItem, Boolean> itemsInStore = store.getCatalog();
        for(Map.Entry<CatalogItem,Boolean> itemEntry: itemsInStore.entrySet()){
            CatalogItemService item = new CatalogItemService(itemEntry.getKey(), itemEntry.getValue());
            int amount = 0;
            if(itemEntry.getValue()){
                amount = store.getItemAmount(item.getItemID());
                item.setAmount(amount);
            }
            items.put(item, amount);
        }
        return items;
    }

    public boolean hasItem(int itemId) {
        return false;
    }

    public int getStoreId() {
        return storeID;
    }

    public CatalogItemService getItem(int id){
        for(CatalogItemService catalogItemService: items.keySet()){
            if (catalogItemService.getItemID() == id)
                return catalogItemService;
        }
        return null;
//        CatalogItem item = store.getItem(id);
//        if(item == null)
//            return null;
//        return new CatalogItemService(item, store.getItemAmount(id)>0);
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
                return "Permanently closed";
            }
        }
        return "";
    }

    public List<CatalogItemService> getItems(){
        return new ArrayList<>(items.keySet());
    }


}
