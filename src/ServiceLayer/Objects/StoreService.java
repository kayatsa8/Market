package ServiceLayer.Objects;

import BusinessLayer.Stores.Store;

public class StoreService {

    public final int founderID;
    public final String storeName;
    public final int storeID;
    public StoreService(Store store)
    {
        storeID = store.getStoreID();
        storeName = store.getStoreName();
        founderID = store.getFounderID();
    }

    public boolean hasItem(int itemId) {
        return true;
    }
}
