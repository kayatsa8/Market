package BusinessLayer.Stores;

import java.util.HashMap;
import java.util.Map;

public class StoreFacade {
    private Map<Integer, Store> stores;
    private int storesIDs;
    private int itemsIDs;

    public StoreFacade() {
        this.stores = new HashMap<>();
        this.storesIDs = 0;
        this.itemsIDs = 0;
    }

    public Store getStore(int storeID) {
        return stores.get(storeID);
    }

    public Store addStore(int founderID, String name)//TODO sync with users
    {
        Store newStore = new Store(storesIDs, founderID, name);
        stores.put(storesIDs++, newStore);
        return newStore;
    }

    public void addCatalogItem(int storeID, String itemName, double itemPrice, Category itemCategory) {
        getStore(storeID).addCatalogItem(itemsIDs++, itemName, itemPrice, itemCategory);
    }

    public void addItemAmount(int storeID, int itemID, int amountToAdd) {
        getStore(storeID).addItemAmount(itemID, amountToAdd);
    }

    public void addBid(int storeID, int itemID, int userID, double offeredPrice) {
        getStore(storeID).addBid(itemID, userID, offeredPrice);
    }

    public void addLottery(int storeID, int itemID, double price, int lotteryPeriodInDays) {
        getStore(storeID).addLottery(itemID, price, lotteryPeriodInDays);
    }

    public void addAuction(int storeID, int itemID, double initialPrice, int auctionPeriodInDays) {
        getStore(storeID).addAuction(itemID, initialPrice, auctionPeriodInDays);
    }

    public boolean participateInLottery(int storeID, int lotteryID, int userID, double offerPrice) {
        return getStore(storeID).participateInLottery(lotteryID, userID, offerPrice);
    }

    public boolean offer(int storeID, int auctionID, int userID, double offerPrice) {
        return getStore(storeID).offer(auctionID, userID, offerPrice);
    }

    public boolean approve(int storeID, int bidID, int replierUserID) {
        return getStore(storeID).approve(bidID, replierUserID);
    }

    public boolean reject(int storeID, int bidID, int replierUserID) {
        return getStore(storeID).reject(bidID, replierUserID);
    }

    public boolean counterOffer(int storeID, int bidID, int replierUserID, double counterOffer) {
        return getStore(storeID).counterOffer(bidID, replierUserID, counterOffer);
    }

    public boolean openStore(int storeID) {
        return getStore(storeID).openStore();
    }

    public boolean closeStore(int storeID) {
        return getStore(storeID).closeStore();
    }

    public boolean closeStorePermanently(int storeID) {
        return getStore(storeID).closeStorePermanently();
    }
}