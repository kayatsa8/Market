package BusinessLayer.Stores;

import BusinessLayer.Stores.Policies.Discounts.Conditional;
import BusinessLayer.Stores.Policies.Discounts.Discount;
import BusinessLayer.Stores.Policies.Discounts.Hidden;
import BusinessLayer.Stores.Policies.Discounts.Visible;

import java.util.Calendar;
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
    public void addCatalogItem(int storeID, String itemName, double itemPrice, Category itemCategory)
    {
        getStore(storeID).addCatalogItem(itemsIDs++, itemName, itemPrice, itemCategory);
    }
    public int getItemAmount(int storeID, int itemID)
    {
        return getStore(storeID).getItemAmount(itemID);
    }
    public void buyCart(Map<Integer, Map<Integer, Integer>> storesToItemsToAmount)
    {
        for (Map.Entry<Integer, Map<Integer, Integer>> storeToItemsToAmount: storesToItemsToAmount.entrySet())
        {
            buyBasket(storeToItemsToAmount.getKey(), storeToItemsToAmount.getValue());
        }
    }
    public void buyBasket(int storeID, Map<Integer, Integer> itemsAmountsToBuy)
    {
        getStore(storeID).buyBasket(itemsAmountsToBuy);
    }
    public boolean saveBasketItemsForUpcomingPurchase(int storeID, Map<Integer, Integer> itemsAmountsToSave)
    {
        return getStore(storeID).saveItemsForUpcomingPurchase(itemsAmountsToSave);
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
    public boolean openStore(int storeID) throws Exception
    {
        return getStore(storeID).openStore();
    }
    public boolean closeStore(int storeID) throws Exception
    {
        return getStore(storeID).closeStore();
    }
    public boolean closeStorePermanently(int storeID) throws Exception
    {
        return getStore(storeID).closeStorePermanently();
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
}