package BusinessLayer.Stores;

import BusinessLayer.NotificationSystem.Message;
import BusinessLayer.Stores.Policies.Conditions.LogicalCompositions.LogicalComposites;
import BusinessLayer.Stores.Policies.Conditions.NumericCompositions.NumericComposites;
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

    public Store addStore(int founderID, String name) {
        Store newStore = new Store(storesIDs, founderID, name);
        stores.put(storesIDs++, newStore);
        return newStore;
    }

    public void setStoreName(int storeID, String storeName) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.setStoreName(storeName);
    }


    public Store getStore(int storeID) {
        return stores.get(storeID);
    }

    public CatalogItem getItem(int storeID, int itemID) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.getItem(itemID);
    }
    public CatalogItem addCatalogItem(int storeID, String itemName, double itemPrice, String itemCategory) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        if (itemPrice <= 0)
            throw new Exception("Item price has to be positive but is " + itemPrice);
        categoryPool.add(itemCategory);
        return store.addCatalogItem(itemsIDs++, itemName, itemPrice, itemCategory);
    }
    public int getItemAmount(int storeID, int itemID) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.getItemAmount(itemID);
    }
    public void addItemAmount(int storeID, int itemID, int amountToAdd) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.addItemAmount(itemID, amountToAdd);
    }
    public void addBid(int storeID, int itemID, int userID, double offeredPrice) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.addBid(itemID, userID, offeredPrice);
    }
    public void addLottery(int storeID, int itemID, double price, int lotteryPeriodInDays) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.addLottery(itemID, price, lotteryPeriodInDays);
    }
    public void addAuction(int storeID, int itemID, double initialPrice, int auctionPeriodInDays) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.addAuction(itemID, initialPrice, auctionPeriodInDays);
    }
    public boolean participateInLottery(int storeID, int lotteryID, int userID, double offerPrice) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.participateInLottery(lotteryID, userID, offerPrice);
    }
    public boolean offerToAuction(int storeID, int auctionID, int userID, double offerPrice) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.offerToAuction(auctionID, userID, offerPrice);
    }
    public boolean approve(int storeID, int bidID, int replierUserID) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.approve(bidID, replierUserID);
    }
    public boolean reject(int storeID, int bidID, int replierUserID) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.reject(bidID, replierUserID);
    }
    public boolean counterOffer(int storeID, int bidID, int replierUserID, double counterOffer) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.counterOffer(bidID, replierUserID, counterOffer);
    }

    public boolean reopenStore(int userID, int storeID) throws Exception {
        if (isStoreExists(storeID))
            return getStore(storeID).reopenStore(userID);
        throw new Exception("Store ID " + storeID + " does not exist");
    }

    public boolean closeStore(int userID, int storeID) throws Exception {
        if (isStoreExists(storeID))
            return getStore(storeID).closeStore(userID);
        throw new Exception("Store ID " + storeID + " does not exist");
    }

    public boolean closeStorePermanently(int storeID) throws Exception {
        if (isStoreExists(storeID))
            return getStore(storeID).closeStorePermanently();
        throw new Exception("Store ID " + storeID + " does not exist");
    }

    public void addVisibleItemsDiscount(int storeID, List<Integer> itemsIDs, double percent, Calendar endOfSale) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.addVisibleItemsDiscount(itemsIDs, percent, endOfSale);
    }
    public void addVisibleCategoryDiscount(int storeID, String category, double percent, Calendar endOfSale) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.addVisibleCategoryDiscount(category, percent, endOfSale);
    }
    public void addVisibleStoreDiscount(int storeID, double percent, Calendar endOfSale) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.addVisibleStoreDiscount(percent, endOfSale);
    }
    public void addConditionalItemsDiscount(int storeID, double percent, Calendar endOfSale, List<Integer> itemsIDs) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.addConditionalItemsDiscount(percent, endOfSale, itemsIDs);
    }
    public void addConditionalCategoryDiscount(int storeID, double percent, Calendar endOfSale, String category) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.addConditionalCategoryDiscount(percent, endOfSale, category);
    }
    public void addConditionalStoreDiscount(int storeID, double percent, Calendar endOfSale) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.addConditionalStoreDiscount(percent, endOfSale);
    }
    public void addHiddenItemsDiscount(int storeID, List<Integer> itemsIDs, double percent, String coupon, Calendar endOfSale) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.addHiddenItemsDiscount(itemsIDs, percent, coupon, endOfSale);
    }
    public void addHiddenCategoryDiscount(int storeID, String category, double percent, String coupon, Calendar endOfSale) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.addHiddenCategoryDiscount(category, percent, coupon, endOfSale);
    }
    public void addHiddenStoreDiscount(int storeID, double percent, String coupon, Calendar endOfSale) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.addHiddenStoreDiscount(percent, coupon, endOfSale);
    }


    public String addPriceRule(int storeID, int discountID, double minimumPrice) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.addPriceRule(discountID, minimumPrice);
    }
    public String addQuantityRule(int storeID, int discountID, Map<Integer, Integer> itemsAmounts) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.addQuantityRule(discountID, itemsAmounts);
    }
    public String addComposite(int storeID, int discountID, LogicalComposites logicalComposite, List<Integer> logicalComponentsIDs) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.addComposite(discountID, logicalComposite, logicalComponentsIDs);
    }
    public String finishConditionalDiscountBuilding(int storeID, int discountID) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.finishConditionalDiscountBuilding(discountID);
    }
    public void wrapDiscounts(int storeID, List<Integer> discountsIDsToWrap, NumericComposites numericCompositeEnum) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.wrapDiscounts(discountsIDsToWrap, numericCompositeEnum);
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

    public CatalogItem removeItemFromStore(int storeID, int itemID) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.removeItemFromStore(itemID);
    }

    public String updateItemName(int storeID, int itemID, String newName) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.updateItemName(itemID, newName);
    }

    public Boolean checkIfStoreOwner(int userID, int storeID) throws Exception {
        if (isStoreExists(storeID)) {
            return getStore(storeID).checkIfStoreOwner(userID);
        }
        throw new Exception("Store ID " + storeID + " does not exist");
    }

    public boolean isStoreExists(int storeID) {
        return stores.containsKey(storeID);
    }

    public Boolean checkIfStoreManager(int userID, int storeID) throws Exception {
        if (isStoreExists(storeID)) {
            return getStore(storeID).checkIfStoreManager(userID);
        }
        throw new Exception("Store ID " + storeID + " does not exist");
    }

    public void sendMessage(int storeID, int receiverID, String title, String content) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.sendMessage(receiverID, title, content);
    }

    public void markMessageAsRead(int storeID, Message message) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.markMessageAsRead(message);
    }

    public void markMessageAsNotRead(int storeID, Message message) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.markMessageAsNotRead(message);
    }

    public List<Message> watchNotReadMessages(int storeID) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.watchNotReadMessages();
    }

    public List<Message> watchReadMessages(int storeID) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.watchReadMessages();
    }

    public List<Message> watchSentMessages(int storeID) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        return store.watchSentMessages();
    }

    public void setMailboxAsUnavailable(int storeID) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.setMailboxAsUnavailable();
    }

    public void setMailboxAsAvailable(int storeID) throws Exception
    {
        Store store = getStore(storeID);
        if (store == null)
            throw new Exception("No store with ID: " + storeID);
        store.setMailboxAsAvailable();
    }
}