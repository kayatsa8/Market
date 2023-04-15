package BusinessLayer.stores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreFacade {
    private Map<Integer, Store> stores;
    private int storesIDs;
    private int itemsIDs;

    public StoreFacade()
    {
        this.stores = new HashMap<>();
        this.storesIDs = 0;
        this.itemsIDs = 0;
    }
    public void addStore(int founderID)
    {
        Store newStore = new Store(storesIDs, founderID);
        stores.put(storesIDs++, newStore);
    }
    public void addCatalogItem(int storeID, String itemName, double itemPrice, Category itemCategory)
    {
        stores.get(storeID).addCatalogItem(itemsIDs++, itemName, itemPrice, itemCategory);
    }
    public void addItemAmount(int storeID, int itemID, int amountToAdd)
    {
        stores.get(storeID).addItemAmount(itemID, amountToAdd);
    }
    public void addBid(int storeID, int itemID, int userID, double offeredPrice)
    {
        stores.get(storeID).addBid(itemID, userID, offeredPrice);
    }
    public void addLottery(int storeID, int itemID, double price, int lotteryPeriodInDays)
    {
        stores.get(storeID).addLottery(itemID, price, lotteryPeriodInDays);
    }
    public void addAuction(int storeID, int itemID, double initialPrice, int auctionPeriodInDays)
    {
        stores.get(storeID).addAuction(itemID, initialPrice, auctionPeriodInDays);
    }
    public boolean participateInLottery(int storeID, int lotteryID, int userID, double offerPrice)
    {
        return stores.get(storeID).participateInLottery(lotteryID, userID, offerPrice);
    }
    public boolean offer(int storeID, int auctionID, int userID, double offerPrice)
    {
        return stores.get(storeID).offer(auctionID, userID, offerPrice);
    }
    public boolean approve(int storeID, int bidID, int replierUserID)
    {
        return stores.get(storeID).approve(bidID, replierUserID);
    }
    public boolean reject(int storeID, int bidID, int replierUserID)
    {
        return stores.get(storeID).reject(bidID, replierUserID);
    }
    public boolean counterOffer(int storeID, int bidID, int replierUserID, double counterOffer)
    {
        return stores.get(storeID).counterOffer(bidID, replierUserID, counterOffer);
    }
    public boolean openStore(int storeID)
    {
        return stores.get(storeID).openStore();
    }
    public boolean closeStore(int storeID)
    {
        return stores.get(storeID).closeStore();
    }
    public boolean closeStorePermanently(int storeID)
    {
        return stores.get(storeID).closeStorePermanently();
    }
}