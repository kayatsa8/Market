package BusinessLayer.stores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Store {
    private int storeID;
    private Map<CatalogItem, Integer> itemsAmounts;
    private List<Bid> bids;
    private List<Auction> auctions;
    private List<Lottery> lotteries;

    public Store(int storeID)
    {
        this.storeID = storeID;
        this.itemsAmounts = new HashMap<>();
        this.auctions = new ArrayList<>();
        this.lotteries = new ArrayList<>();
        this.bids = new ArrayList<>();
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
        bids.add(new Bid(itemID, userID, offeredPrice));
    }
    public void addLottery(int itemID, double price, int lotteryPeriodInDays)
    {
        lotteries.add(new Lottery(itemID, price, lotteryPeriodInDays));
    }
    public void addAuction(int itemID, double initialPrice, int auctionPeriodInDays)
    {
        auctions.add(new Auction(itemID, initialPrice, auctionPeriodInDays));
    }
}
