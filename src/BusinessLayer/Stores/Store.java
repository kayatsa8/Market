package BusinessLayer.Stores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static BusinessLayer.Stores.StoreStatus.*;

public class Store {
    private final int founderID;
    private int storeID;
    private String storeName;
    private Map<CatalogItem, Integer> itemsAmounts;
    private Map<Integer, Bid> bids;
    private Map<Integer, Auction> auctions;
    private Map<Integer, Lottery> lotteries;
    private StoreStatus storeStatus;
    private List<Integer> storeOwners;
    private List<Integer> storeManagers;
    private int bidsIDs;
    private int lotteriesIDs;
    private int auctionsIDs;

    public Store(int storeID, int founderID, String name) {
        this.storeID = storeID;
        this.storeName = name;
        this.itemsAmounts = new HashMap<>();
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
        storeOwners.add(founderID);
    }

    public int getFounderID() {
        return founderID;
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

    public StoreStatus getStoreStatus() {
        return storeStatus;
    }

    public List<Integer> getStoreOwners() {
        return storeOwners;
    }

    public List<Integer> getStoreManagers() {
        return storeManagers;
    }

    public void addCatalogItem(int itemID, String itemName, double itemPrice, Category itemCategory) {
        CatalogItem newItem = new CatalogItem(itemID, itemName, itemPrice, itemCategory);
        itemsAmounts.put(newItem, 0);
    }

    public void addItemAmount(int itemID, int amountToAdd) {
        for (CatalogItem item : itemsAmounts.keySet()) {
            if (item.getItemID() == itemID) {
                itemsAmounts.put(item, amountToAdd + itemsAmounts.get(item));
            }
        }
    }

    public void addBid(int itemID, int userID, double offeredPrice) {
        Bid newBid = new Bid(itemID, userID, offeredPrice);
        List<Integer> storeOwnersAndManagers = new ArrayList<>();
        storeOwnersAndManagers.addAll(storeOwners);
        storeOwnersAndManagers.addAll(storeManagers);
        newBid.setRepliers(storeOwnersAndManagers);
        bids.put(bidsIDs++, newBid);

    }

    public void addLottery(int itemID, double price, int lotteryPeriodInDays) {
        lotteries.put(lotteriesIDs++, new Lottery(itemID, price, lotteryPeriodInDays));
    }

    public void addAuction(int itemID, double initialPrice, int auctionPeriodInDays) {
        auctions.put(auctionsIDs++, new Auction(itemID, initialPrice, auctionPeriodInDays));
    }

    public boolean participateInLottery(int lotteryID, int userID, double offerPrice) {
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
