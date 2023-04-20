package BusinessLayer.Stores;

import Globals.FilterValue;
import Globals.SearchBy;
import Globals.SearchFilter;

import java.util.*;

import static Globals.SearchFilter.STORE_RATING;

public class StoreFacade {
    private Map<Integer, Store> stores;
    private Set<String> categoryPool;
    private int storesIDs;
    private int itemsIDs;

    public StoreFacade() {
        this.stores = new HashMap<>();
        this.categoryPool = new HashSet<>();
        this.storesIDs = 0;
        this.itemsIDs = 0;
    }

    public Store addStore(int founderID, String name)//TODO sync with users
    {
        Store newStore = new Store(storesIDs, founderID, name);
        stores.put(storesIDs++, newStore);
        return newStore;
    }
    public void setStoreName(int storeID, String storeName)
    {
        getStore(storeID).setStoreName(storeName);
    }
    public Store getStore(int storeID)
    {
        return stores.get(storeID);
    }
    public CatalogItem getItem(int storeID, int itemID)
    {
        return getStore(storeID).getItem(itemID);
    }
    public CatalogItem addCatalogItem(int storeID, String itemName, double itemPrice, String itemCategory)
    {
        categoryPool.add(itemCategory);
        return getStore(storeID).addCatalogItem(itemsIDs++, itemName, itemPrice, itemCategory);
    }
    public int getItemAmount(int storeID, int itemID)
    {
        return getStore(storeID).getItemAmount(itemID);
    }
    public void addItemAmount(int storeID, int itemID, int amountToAdd)
    {
        getStore(storeID).addItemAmount(itemID, amountToAdd);
    }
    public void addBid(int storeID, int itemID, int userID, double offeredPrice)
    {
        getStore(storeID).addBid(itemID, userID, offeredPrice);
    }
    public void addLottery(int storeID, int itemID, double price, int lotteryPeriodInDays)
    {
        getStore(storeID).addLottery(itemID, price, lotteryPeriodInDays);
    }
    public void addAuction(int storeID, int itemID, double initialPrice, int auctionPeriodInDays)
    {
        getStore(storeID).addAuction(itemID, initialPrice, auctionPeriodInDays);
    }
    public boolean participateInLottery(int storeID, int lotteryID, int userID, double offerPrice)
    {
        return getStore(storeID).participateInLottery(lotteryID, userID, offerPrice);
    }
    public boolean offerToAuction(int storeID, int auctionID, int userID, double offerPrice)
    {
        return getStore(storeID).offerToAuction(auctionID, userID, offerPrice);
    }
    public boolean approve(int storeID, int bidID, int replierUserID) throws Exception
    {
        return getStore(storeID).approve(bidID, replierUserID);
    }
    public boolean reject(int storeID, int bidID, int replierUserID) throws Exception
    {
        return getStore(storeID).reject(bidID, replierUserID);
    }
    public boolean counterOffer(int storeID, int bidID, int replierUserID, double counterOffer) throws Exception
    {
        return getStore(storeID).counterOffer(bidID, replierUserID, counterOffer);
    }
    public boolean reopenStore(int userID, int storeID) throws Exception
    {
        if (isStoreExists(storeID))
            return getStore(storeID).reopenStore(userID);
        throw new Exception("Store ID " + storeID + " does not exist");
    }
    public boolean closeStore(int userID, int storeID) throws Exception
    {
        if (isStoreExists(storeID))
            return getStore(storeID).closeStore(userID);
        throw new Exception("Store ID " + storeID + " does not exist");
    }
    public boolean closeStorePermanently(int storeID) throws Exception
    {
        if (isStoreExists(storeID))
            return getStore(storeID).closeStorePermanently();
        throw new Exception("Store ID " + storeID + " does not exist");
    }
    public void addVisibleDiscount(int storeID, int itemID, double percent, Calendar endOfSale)
    {
        Store store = stores.get(storeID);
        store.addVisibleDiscount(itemID, percent, endOfSale);
    }
    public void addConditionalDiscount(int storeID, Map<Integer, Integer> itemsIDsToAmounts, double percent, Calendar endOfSale)
    {
        Store store = stores.get(storeID);
        store.addConditionalDiscount(itemsIDsToAmounts, percent, endOfSale);
    }
    public void addHiddenDiscount(int storeID, int itemID, double percent, String coupon, Calendar endOfSale)
    {
        Store store = stores.get(storeID);
        store.addHiddenDiscount(itemID, percent, coupon, endOfSale);
    }

    public Map<CatalogItem, Boolean> getCatalog() {
        Map<CatalogItem, Boolean> res = new HashMap<>();
        for (Store store : stores.values()) {
            res.putAll(store.getCatalog());
        }
        return res;
    }

    public Map<CatalogItem, Boolean> getCatalog(String keywords, SearchBy searchBy, Map<SearchFilter, FilterValue> filters) throws Exception {
        Map<CatalogItem, Boolean> res = new HashMap<>();
        Collection<Store> storesToSearch = stores.values();
        if (filters.containsKey(STORE_RATING)) {
            storesToSearch.removeIf(store -> filters.get(STORE_RATING).filter());
            filters.remove(STORE_RATING);
        }
        for (Store store : storesToSearch) {
            res.putAll(store.getCatalog(keywords, searchBy, filters));
        }
        return res;
    }

    public void removeItemFromStore(int storeID, int itemID) throws Exception
    {
        getStore(storeID).removeItemFromStore(itemID);
    }

    public String updateItemName(int storeID, int itemID, String newName) throws Exception
    {
        return getStore(storeID).updateItemName(itemID, newName);
    }

    public Boolean checkIfStoreOwner(int userID, int storeID) throws Exception
    {
        if (isStoreExists(storeID))
        {
            return getStore(storeID).checkIfStoreOwner(userID);
        }
        throw new Exception("Store ID " + storeID + " does not exist");
    }

    private boolean isStoreExists(int storeID)
    {
        return stores.containsKey(storeID);
    }

    public Boolean checkIfStoreManager(int userID, int storeID) throws Exception
    {
        if (isStoreExists(storeID))
        {
            return getStore(storeID).checkIfStoreManager(userID);
        }
        throw new Exception("Store ID " + storeID + " does not exist");
    }
}