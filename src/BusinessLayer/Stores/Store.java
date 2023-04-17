package BusinessLayer.Stores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import BusinessLayer.NotificationSystem.Mailbox;
import BusinessLayer.NotificationSystem.NotificationHub;
import static BusinessLayer.Stores.StoreStatus.*;

public class Store {
    private final int founderID;
    private String storeName;
    private int storeID;
    private int bidsIDs;
    private int lotteriesIDs;
    private int auctionsIDs;
    private Mailbox storeMailBox;
    private StoreStatus storeStatus;
    private Map<Integer, CatalogItem> items;
    private Map<Integer, Integer> itemsAmounts;
    private Map<Integer, Integer> savedItemsAmounts;
    private Map<Integer, Bid> bids;
    private Map<Integer, Auction> auctions;
    private Map<Integer, Lottery> lotteries;
    private List<Integer> storeOwners;
    private List<Integer> storeManagers;
    public List<Integer> getStoreOwners() {
        return storeOwners;
    }
    public List<Integer> getStoreManagers() {
        return storeManagers;
    }
    public CatalogItem getItem(int itemID)
    {
        return items.get(itemID);
    }
    public int getItemAmount(int itemID)
    {
        return itemsAmounts.get(itemID);
    }
    public int getStoreID() {
        return storeID;
    }
    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Store(int storeID, int founderID, String name)
    {
        this.storeID = storeID;
        this.storeName = name;
        this.itemsAmounts = new HashMap<>();
        this.items = new HashMap<>();
        this.savedItemsAmounts = new HashMap<>();
        this.auctions = new HashMap<>();
        this.lotteries = new HashMap<>();
        this.bids = new HashMap<>();
        this.bidsIDs = 0;
        this.lotteriesIDs = 0;
        this.auctionsIDs = 0;
        this.storeStatus = OPEN;
        this.storeManagers = new ArrayList<>();
        this.founderID = founderID;
        this.storeOwners = new ArrayList<>();
        try {
            this.storeMailBox = NotificationHub.getInstance().registerToMailService(this);
        } catch (Exception e) {}
        storeOwners.add(founderID);
    }

    public int getFounderID() {
        return founderID;
    }
    public StoreStatus getStoreStatus() {
        return storeStatus;
    }
    public void addCatalogItem(int itemID, String itemName, double itemPrice, Category itemCategory) {
        CatalogItem newItem = new CatalogItem(itemID, itemName, itemPrice, itemCategory);
        itemsAmounts.put(itemID, 0);
        items.put(itemID, newItem);
        savedItemsAmounts.put(itemID, 0);
    }
    public void buyBasket(Map<Integer, Integer> itemsAmountsToBuy)
    {
        int currentAmountToSave;
        int amountToBuy;
        for (Integer itemID : itemsAmountsToBuy.keySet())
        {
            currentAmountToSave = savedItemsAmounts.get(itemID);
            amountToBuy = itemsAmountsToBuy.get(itemID);
            savedItemsAmounts.put(itemID, currentAmountToSave - amountToBuy);
        }
    }
    public boolean saveItemsForUpcomingPurchase(Map<Integer, Integer> itemsAmountsToSave)
    {
        boolean success = true;
        int itemAmountToSave;
        int itemCurrentAmount;
        for (Integer itemID : itemsAmountsToSave.keySet())
        {
            itemAmountToSave = itemsAmountsToSave.get(itemID);
            itemCurrentAmount = itemsAmounts.get(itemID);
            if (itemCurrentAmount < itemAmountToSave)
            {
                success = false;
                break;
            }
        }
        if (success)
        {
            for (Integer itemID : itemsAmountsToSave.keySet())
            {
                saveItemAmount(itemID, itemsAmountsToSave.get(itemID));
            }
        }
        return success;
    }
    private void saveItemAmount(int itemID, int amountToSave)
    {
        int itemAmountToSave = amountToSave;
        int itemCurrentAmount = itemsAmounts.get(itemID);
        if (itemCurrentAmount >= itemAmountToSave)
        {
            itemsAmounts.put(itemID, itemCurrentAmount - itemAmountToSave);
            savedItemsAmounts.put(itemID, itemAmountToSave);
        }
    }
    public void addBid(int itemID, int userID, double offeredPrice)
    {
        saveItemAmount(itemID, 1);
        Bid newBid = new Bid(itemID, userID, offeredPrice);
        List<Integer> storeOwnersAndManagers = new ArrayList<>();
        storeOwnersAndManagers.addAll(storeOwners);
        storeOwnersAndManagers.addAll(storeManagers);
        newBid.setRepliers(storeOwnersAndManagers);
        bids.put(bidsIDs++, newBid);
    }
    public void addLottery(int itemID, double price, int lotteryPeriodInDays)
    {
        saveItemAmount(itemID, 1);
        lotteries.put(lotteriesIDs++, new Lottery(itemID, price, lotteryPeriodInDays));
    }
    public void addAuction(int itemID, double initialPrice, int auctionPeriodInDays)
    {
        saveItemAmount(itemID, 1);
        auctions.put(auctionsIDs++, new Auction(itemID, initialPrice, auctionPeriodInDays));
    }
    public void addItemAmount(int itemID, int amountToAdd)
    {
        int currentAmount = getItemAmount(itemID);
        itemsAmounts.put(itemID, currentAmount+amountToAdd);
    }
    private void addSavedItemAmount(int itemID, int amountToRemove)
    {
        int currentAmountSaved = savedItemsAmounts.get(itemID);
        savedItemsAmounts.put(itemID, currentAmountSaved+amountToRemove);
    }
    private void removeBid(int bidID)
    {
        bids.remove(bidID);
    }
    private void removeAuction(int auctionID)
    {
        auctions.remove(auctionID);
    }
    private void removeLottery(int lotteryID)
    {
        lotteries.remove(lotteryID);
    }
    public void finishBidSuccessfully(int bidID)
    {
        removeBid(bidID);
        addSavedItemAmount(bids.get(bidID).getItemID(), -1);
    }
    public void finishBidUnsuccessfully(int bidID)
    {
        removeBid(bidID);
        int itemID = bids.get(bidID).getItemID();
        addItemAmount(itemID, 1);
        addSavedItemAmount(itemID, -1);
    }
    public void finishAuctionSuccessfully(int auctionID)
    {
        removeAuction(auctionID);
        addSavedItemAmount(auctions.get(auctionID).getItemID(), -1);
    }
    public void finishAuctionUnsuccessfully(int auctionID)
    {
        removeAuction(auctionID);
        int itemID = auctions.get(auctionID).getItemID();
        addItemAmount(itemID, 1);
        addSavedItemAmount(itemID, -1);
    }
    public void finishLotterySuccessfully(int lotteryID)
    {
        removeLottery(lotteryID);
        addSavedItemAmount(lotteries.get(lotteryID).getItemID(), -1);
    }
    public void finishLotteryUnsuccessfully(int lotteryID)
    {
        removeLottery(lotteryID);
        int itemID = lotteries.get(lotteryID).getItemID();
        addItemAmount(itemID, 1);
        addSavedItemAmount(itemID, -1);
    }
    public boolean participateInLottery(int lotteryID, int userID, double offerPrice)
    {
        return lotteries.get(lotteryID).participateInLottery(userID, offerPrice);
    }

    public boolean offer(int auctionID, int userID, double offerPrice) {
        return auctions.get(auctionID).offer(userID, offerPrice);
    }

    public boolean approve(int bidID, int replierUserID) {
        return bids.get(bidID).approve(replierUserID);
    }

    public boolean reject(int bidID, int replierUserID) {
        return bids.get(bidID).reject(replierUserID);
    }

    public boolean counterOffer(int bidID, int replierUserID, double counterOffer) {
        return bids.get(bidID).counterOffer(replierUserID, counterOffer);
    }

    public boolean openStore() {
        if (storeStatus == OPEN) {
            System.out.println("Store is already open");
            return false;
        } else if (storeStatus == PERMANENTLY_CLOSE) {
            System.out.println("Store is permanently closed and cannot be opened");
            return false;
        } else {
            storeStatus = OPEN;
            return true;
        }
    }

    public boolean closeStore() {
        if (storeStatus == CLOSE) {
            System.out.println("Store is already close");
            return false;
        } else if (storeStatus == PERMANENTLY_CLOSE) {
            System.out.println("Store is permanently close and cannot change its status to close");
            return false;
        } else {
            storeStatus = CLOSE;
            return true;
        }
    }

    public boolean closeStorePermanently() {
        if (storeStatus == PERMANENTLY_CLOSE) {
            System.out.println("Store is already permanently closed");
            return false;
        } else {
            storeStatus = PERMANENTLY_CLOSE;
            return true;
        }
    }

    public void addManager(int userID) {
        this.storeManagers.add(userID);
    }

    public void addOwner(int userID) {
        this.storeOwners.add(userID);
    }

    //Integer instead of int so that it removes by object not index
    public void removeManager(Integer id) {
        this.storeManagers.remove(id);
    }

    //Integer instead of int so that it removes by object not index
    public void removeOwner(Integer id) {
        this.storeOwners.remove(id);
    }
}
