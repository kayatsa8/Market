package BusinessLayer.stores;

import BusinessLayer.Tree;
import java.util.*;
import static BusinessLayer.stores.StoreStatus.*;

public class Store {
    private int storeID;
    private Map<CatalogItem, Integer> itemsAmounts;
    private Map<Integer, Bid> bids;
    private Map<Integer, Auction> auctions;
    private Map<Integer, Lottery> lotteries;
    private Tree storeOwnersAndManagers;
    private StoreStatus storeStatus;
    private int bidsIDs;
    private int lotteriesIDs;
    private int auctionsIDs;


    public Store(int storeID, int founderID)
    {
        this.storeID = storeID;
        this.itemsAmounts = new HashMap<>();
        this.auctions = new HashMap<>();
        this.lotteries = new HashMap<>();
        this.bids = new HashMap<>();
        this.bidsIDs = 0;
        this.lotteriesIDs = 0;
        this.auctionsIDs = 0;
        this.storeStatus = OPEN;
        this.storeOwnersAndManagers = new Tree(founderID);

    }

    public int getFounderID()
    {
        return storeOwnersAndManagers.getRoot().getData();
    }
    public void addCatalogItem(int itemID, String itemName, double itemPrice, Category itemCategory)
    {
        CatalogItem newItem = new CatalogItem(itemID, itemName, itemPrice, itemCategory);
        itemsAmounts.put(newItem, 0);
    }

    public void addItemAmount(int itemID, int amountToAdd)
    {
        for (CatalogItem item: itemsAmounts.keySet())
        {
            if (item.getItemID() == itemID)
            {
                itemsAmounts.put(item, amountToAdd+itemsAmounts.get(item));
            }
        }
    }

    public void addBid(int itemID, int userID, double offeredPrice)
    {
        Bid newBid = new Bid(itemID, userID, offeredPrice);
        newBid.setRepliers(storeOwnersAndManagers.getTreeDataAsList());
        bids.put(bidsIDs++, newBid);

    }
    public void addLottery(int itemID, double price, int lotteryPeriodInDays)
    {
        lotteries.put(lotteriesIDs++, new Lottery(itemID, price, lotteryPeriodInDays));
    }
    public void addAuction(int itemID, double initialPrice, int auctionPeriodInDays)
    {
        auctions.put(auctionsIDs++, new Auction(itemID, initialPrice, auctionPeriodInDays));
    }
    public boolean participateInLottery(int lotteryID, int userID, double offerPrice)
    {
        return lotteries.get(lotteryID).participateInLottery(userID, offerPrice);
    }
    public boolean offer(int auctionID, int userID, double offerPrice)
    {
        return auctions.get(auctionID).offer(userID, offerPrice);
    }
    public boolean approve(int bidID, int replierUserID)
    {
        return bids.get(bidID).approve(replierUserID);
    }
    public boolean reject(int bidID, int replierUserID)
    {
        return bids.get(bidID).reject(replierUserID);
    }
    public boolean counterOffer(int bidID, int replierUserID, double counterOffer)
    {
        return bids.get(bidID).counterOffer(replierUserID, counterOffer);
    }
    public boolean openStore()
    {
        if (storeStatus == OPEN)
        {
            System.out.println("Store is already open");
            return false;
        }
        else if (storeStatus == PERMANENTLY_CLOSE)
        {
            System.out.println("Store is permanently closed and cannot be opened");
            return false;
        }
        else
        {
            storeStatus = OPEN;
            return true;
        }
    }
    public boolean closeStore()
    {
        if (storeStatus == CLOSE)
        {
            System.out.println("Store is already close");
            return false;
        }
        else if (storeStatus == PERMANENTLY_CLOSE)
        {
            System.out.println("Store is permanently close and cannot change its status to close");
            return false;
        }
        else
        {
            storeStatus = CLOSE;
            return true;
        }
    }
    public boolean closeStorePermanently()
    {
        if (storeStatus == PERMANENTLY_CLOSE)
        {
            System.out.println("Store is already permanently closed");
            return false;
        }
        else
        {
            storeStatus = PERMANENTLY_CLOSE;
            return true;
        }
    }
}
