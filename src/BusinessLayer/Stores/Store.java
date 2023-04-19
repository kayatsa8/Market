package BusinessLayer.Stores;

import java.util.*;

import BusinessLayer.Log;
import BusinessLayer.NotificationSystem.Mailbox;
import BusinessLayer.NotificationSystem.NotificationHub;
import BusinessLayer.NotificationSystem.StoreMailbox;
import BusinessLayer.Receipts.Receipt.Receipt;
import BusinessLayer.Receipts.ReceiptHandler;
import BusinessLayer.Stores.Policies.Discounts.Conditional;
import BusinessLayer.Stores.Policies.Discounts.Discount;
import BusinessLayer.Stores.Policies.Discounts.Hidden;
import BusinessLayer.Stores.Policies.Discounts.Visible;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static BusinessLayer.Stores.StoreStatus.*;

public class Store {
    private static final Logger log = Log.log;
    private final int founderID;
    private String storeName;
    private int storeID;
    private int bidsIDs;
    private int lotteriesIDs;
    private int auctionsIDs;
    private int discountsIDs;
    private StoreMailbox storeMailBox;
    private StoreStatus storeStatus;
    private Map<Integer, Discount> discounts;
    private Map<Integer, CatalogItem> items;
    private Map<Integer, Integer> itemsAmounts;
    private Map<Integer, Integer> savedItemsAmounts;
    private Map<Integer, Bid> bids;
    private Map<Integer, Auction> auctions;
    private Map<Integer, Lottery> lotteries;
    private ReceiptHandler receiptHandler;
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
    public Discount getDiscount(int discountID)
    {
        return discounts.get(discountID);
    }
    public ReceiptHandler getReceiptHandler() { return receiptHandler; }

    public Store(int storeID, int founderID, String name)
    {
        this.storeID = storeID;
        this.storeName = name;
        this.discounts = new HashMap<>();
        this.itemsAmounts = new HashMap<>();
        this.items = new HashMap<>();
        this.savedItemsAmounts = new HashMap<>();
        this.auctions = new HashMap<>();
        this.lotteries = new HashMap<>();
        this.bids = new HashMap<>();
        this.receiptHandler = new ReceiptHandler();
        this.bidsIDs = 0;
        this.lotteriesIDs = 0;
        this.auctionsIDs = 0;
        this.discountsIDs = 0;
        this.storeStatus = OPEN;
        this.storeManagers = new ArrayList<>();
        this.founderID = founderID;
        this.storeOwners = new ArrayList<>();
        try {
            this.storeMailBox = NotificationHub.getInstance().registerToMailService(this);
        } catch (Exception e) {}
        storeOwners.add(founderID);
        log.info("Store " + storeID + " created with name: " + storeName);
    }

    public int getFounderID() {
        return founderID;
    }

    public Map<CatalogItem, Boolean> getCatalog() {
        Map<CatalogItem, Boolean> res = new HashMap<>();
        CatalogItem valueFromA;
        Boolean valueFromB;
        for (Map.Entry<Integer, CatalogItem> entry : items.entrySet()) {
            valueFromA = entry.getValue();
            valueFromB = itemsAmounts.get(entry.getKey())>0;

            // Put the value from map a as the key and the value from map b as the value in map c
            res.put(valueFromA, valueFromB);
        }
        return res;
    }
    public void addVisibleDiscount(int itemID, double percent, Calendar endOfSale)
    {
        Discount visibleDiscount = new Visible(itemID, percent, endOfSale);
        discounts.put(discountsIDs++, visibleDiscount);
        log.info("Added new visible discount to item " + itemID + " at store " + storeID);
    }
    public void addConditionalDiscount(Map<Integer, Integer> itemsIDsToAmounts, double percent, Calendar endOfSale)
    {
        Discount conditionalDiscount = new Conditional(itemsIDsToAmounts, percent, endOfSale);
        discounts.put(discountsIDs++, conditionalDiscount);
        log.info("Added new conditional discount at store " + storeID);
    }
    public void addHiddenDiscount(int itemID, double percent, String coupon, Calendar endOfSale)
    {
        Discount hiddenDiscount = new Hidden(itemID, percent, endOfSale, coupon);
        discounts.put(discountsIDs++, hiddenDiscount);
        log.info("Added new hidden discount to item " + itemID + " at store " + storeID);
    }
    public StoreStatus getStoreStatus() {
        return storeStatus;
    }
    public void addCatalogItem(int itemID, String itemName, double itemPrice, Category itemCategory) {
        CatalogItem newItem = new CatalogItem(itemID, itemName, itemPrice, itemCategory);
        itemsAmounts.put(itemID, 0);
        items.put(itemID, newItem);
        savedItemsAmounts.put(itemID, 0);
        log.info("Added new item: " + itemName + ", at store " + storeID);
    }
    public void buyBasket(List<CartItemInfo> basketItems, int userID)
    {
        ReceiptHandler RH = new ReceiptHandler();
        Map<CatalogItem, CartItemInfo> receiptItems = new HashMap<>();
        for (CartItemInfo cartItemInfo : basketItems)
        {
            int itemID = cartItemInfo.getItemID();
            receiptItems.put(getItem(itemID), cartItemInfo);
            savedItemsAmounts.put(itemID, savedItemsAmounts.get(itemID) - cartItemInfo.getAmount());
        }
        Map<Integer, Map<CatalogItem, CartItemInfo>> receiptInfo = new HashMap<>();
        receiptInfo.put(userID, receiptItems);
        RH.addReceipt(storeID, receiptInfo);
        storeMailBox.sendMessageToList(storeOwners, "New purchase", "User " + userID + " made a purchase in store " + storeName + " where you are one of the owners");
        log.info("A basket was bought at store " + storeID);
    }
    public boolean saveItemsForUpcomingPurchase(List<CartItemInfo> basketItems)
    {
        boolean success = true;
        int itemAmountToSave;
        int itemCurrentAmount;
        for (CartItemInfo cartItemInfo : basketItems)
        {
            int itemID = cartItemInfo.getItemID();
            itemAmountToSave = cartItemInfo.getAmount();
            itemCurrentAmount = itemsAmounts.get(itemID);
            if (itemCurrentAmount < itemAmountToSave)
            {
                success = false;
                break;
            }
        }
        if (success)
        {
            for (CartItemInfo cartItemInfo : basketItems)
            {
                int itemID = cartItemInfo.getItemID();
                itemAmountToSave = cartItemInfo.getAmount();
                saveItemAmount(itemID, itemAmountToSave);
                double itemDiscountPercent = getItemDiscountsPercent(itemID);
                cartItemInfo.setPercent(itemDiscountPercent);
            }
            log.info("Items was saved for upcoming purchase at store " + storeID);
        }
        else
            log.warning("Items wasn't saved for upcoming purchase at store " + storeID + " due to lack of items");
        return success;
    }
    private double getItemDiscountsPercent(int itemID) //return: [0,1]
    {
        double pricePercent = 1;
        for (Discount discount : discounts.values())
        {
            if (discount.getItemsIDs().contains(itemID))
            {
                pricePercent = pricePercent * (1 - discount.getDiscountToItem());
            }
        }
        return 1-pricePercent;
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
        storeMailBox.sendMessageToList(storeOwnersAndManagers, "New bid", "User " + userID + " offered new bid for item " + items.get(itemID).getItemName() + " at store " + storeName + " with price of " + offeredPrice + " while the original price is " + items.get(itemID).getPrice());
        log.info("Added new bid for item " + itemID + " at store " + storeID);
    }
    public void addLottery(int itemID, double price, int lotteryPeriodInDays)
    {
        saveItemAmount(itemID, 1);
        lotteries.put(lotteriesIDs, new Lottery(this, lotteriesIDs++,itemID, price, lotteryPeriodInDays));
        log.info("Added new lottery for item " + itemID + " at store " + storeID);
    }
    public void addAuction(int itemID, double initialPrice, int auctionPeriodInDays)
    {
        saveItemAmount(itemID, 1);
        auctions.put(auctionsIDs, new Auction(this, auctionsIDs++, itemID, initialPrice, auctionPeriodInDays));
        log.info("Added new auction for item " + itemID + " at store " + storeID);
    }
    public void addItemAmount(int itemID, int amountToAdd)
    {
        int currentAmount = getItemAmount(itemID);
        itemsAmounts.put(itemID, currentAmount+amountToAdd);
        log.info("Added amount by " + amountToAdd +  " for item " + itemID + " at store " + storeID);
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
        Bid bid = bids.get(bidID);
        int itemID = bid.getItemID();
        int userID = bid.getUserID();
        addSavedItemAmount(itemID, -1);
        removeBid(bidID);
        if (bid.getHighestCounterOffer() == -1)
        {
            storeMailBox.sendMessage(userID, "Bid approved", "Hi, your bid for the item: " + items.get(itemID).getItemName() + ", was approved by the store, and the item will be sent to you soon");
            log.info("Bid " + bidID + " was fully approved");
        }
        else
        {
            storeMailBox.sendMessage(userID, "Bid countered", "Hi, your bid for the item: " + items.get(itemID).getItemName() + ", was counterd by the store with counter-offer of: " + bid.getHighestCounterOffer() + " while the original price is: " + items.get(itemID).getPrice());
            log.info("Bid " + bidID + " was counter-offered with price of " + bid.getHighestCounterOffer());
        }
    }
    public void finishBidUnsuccessfully(int bidID)
    {
        Bid bid = bids.get(bidID);
        int itemID = bid.getItemID();
        int userID = bid.getUserID();
        addItemAmount(itemID, 1);
        addSavedItemAmount(itemID, -1);
        removeBid(bidID);
        storeMailBox.sendMessage(userID, "Bid rejected", "Hi, we apologize for the inconvenience, but your bid for the item: " + items.get(itemID).getItemName() + ", was rejected by the store");
        log.info("Bid " + bidID + " was rejected");
    }
    public void finishAuctionSuccessfully(int auctionID)
    {
        System.out.println("The item is sold to user");
        Auction myAuction = auctions.get(auctionID);
        int winnerID = myAuction.getCurrentWinningUserID();
        int itemID = myAuction.getItemID();
        addSavedItemAmount(myAuction.getItemID(), -1);
        myAuction.getAuctionTimer().cancel();
        myAuction.getAuctionTimer().purge();
        removeAuction(auctionID);
        storeMailBox.sendMessage(winnerID, "Won the auction", "Congratulations, you are the winner in our auction in store " + storeName + " of item " + items.get(itemID).getItemName() + " with an offer of " + myAuction.getCurrentPrice() + " while the original price is " + items.get(itemID).getPrice());
        log.info("Auction " + auctionID + " finished successfully and item was sold");
    }
    public void finishAuctionUnsuccessfully(int auctionID)
    {
        Auction myAuction = auctions.get(auctionID);
        int itemID = myAuction.getItemID();
        addItemAmount(itemID, 1);
        addSavedItemAmount(itemID, -1);
        myAuction.getAuctionTimer().cancel();
        myAuction.getAuctionTimer().purge();
        removeAuction(auctionID);
        log.info("Auction " + auctionID + " finished unsuccessfully and item was not sold");
    }
    public void finishLotterySuccessfully(int lotteryID)
    {
        Lottery myLottery = lotteries.get(lotteryID);
        int winnerID = myLottery.getWinnerID();
        int itemID = myLottery.getItemID();
        addSavedItemAmount(itemID, -1);
        myLottery.getLotteryTimer().cancel();
        myLottery.getLotteryTimer().purge();
        removeLottery(lotteryID);
        storeMailBox.sendMessage(winnerID, "Won the lottery", "Congratulations, you are the winner in our lottery in store " + storeName + " of item " + items.get(itemID).getItemName());
        List<Integer> losers = myLottery.getParticipants();
        losers.remove(winnerID);
        storeMailBox.sendMessageToList(losers, "Lost the lottery", "We are sorry, but you lost the lottery in store " + storeName + " of item " + items.get(itemID).getItemName());
        log.info("Lottery " + lotteryID + " finished successfully and item was sold to user " + winnerID);
    }
    public void finishLotteryUnsuccessfully(int lotteryID)
    {
        Lottery myLottery = lotteries.get(lotteryID);
        int itemID = myLottery.getItemID();
        addItemAmount(itemID, 1);
        addSavedItemAmount(itemID, -1);
        myLottery.getLotteryTimer().cancel();
        myLottery.getLotteryTimer().purge();
        removeLottery(lotteryID);
        List<Integer> participants = myLottery.getParticipants();
        if (participants.size()>0)
            storeMailBox.sendMessageToList(participants, "Lottery has canceled", "We are sorry, but the lottery in store " + storeName + " of item " + items.get(itemID).getItemName() + " has canceled due to lack of demand. Your money will be returned.");
        log.info("Lottery " + lotteryID + " finished unsuccessfully and item was not sold");
    }
    public boolean participateInLottery(int lotteryID, int userID, double offerPrice)
    {
        Lottery myLottery = lotteries.get(lotteryID);
        boolean participateSuccessfully = myLottery.participateInLottery(userID, offerPrice);
        if (participateSuccessfully)
        {
            if (myLottery.isLotteryFinished())
            {
                finishLotterySuccessfully(lotteryID);
            }
            log.info("User " + userID + " is participating in lottery " + lotteryID);
            return true;
        }
        log.warning("User " + userID + " failed to participate in lottery " + lotteryID);
        return false;
    }

    public boolean offerToAuction(int auctionID, int userID, double offerPrice) {
        Auction myAuction = auctions.get(auctionID);
        double bestOfferBefore = myAuction.getCurrentPrice();
        int winnerBefore = myAuction.getCurrentWinningUserID();
        String itemName = items.get(myAuction.getItemID()).getItemName();
        boolean result = myAuction.offerToAuction(userID, offerPrice);
        double bestOfferNow = myAuction.getCurrentPrice();
        if (result)
            storeMailBox.sendMessage(winnerBefore, "Someone beat your auction offer", "Hi, we want to inform you that other user passed your offer of " + bestOfferBefore + " with an offer of " + bestOfferNow + " at the auction of item " + itemName + " at store " + storeName);
        log.info("User " + userID + " offered to auction " + auctionID + " with price of " + offerPrice);
        return result;
    }

    public boolean approve(int bidID, int replierUserID) throws Exception
    {
        boolean finishedBid = bids.get(bidID).approve(replierUserID);
        log.info("User " + replierUserID + " approved bid " + bidID);
        if (finishedBid) {
            finishBidSuccessfully(bidID);
            return true;
        }
        return false;
    }

    public boolean reject(int bidID, int replierUserID) throws Exception
    {
        boolean finishedBid = bids.get(bidID).reject(replierUserID);
        log.info("User " + replierUserID + " rejected bid " + bidID);
        if (finishedBid) {
            finishBidUnsuccessfully(bidID);
            return true;
        }
        return false;
    }

    public boolean counterOffer(int bidID, int replierUserID, double counterOffer) throws Exception
    {
        boolean finishedBid = bids.get(bidID).counterOffer(replierUserID, counterOffer);
        log.info("User " + replierUserID + " counter-offered bid " + bidID);
        if (finishedBid) {
            finishBidSuccessfully(bidID);
            return true;
        }
        return false;
    }

    public boolean openStore() throws Exception
    {
        if (storeStatus == OPEN) {
            throw new Exception("Store is already open");
        } else if (storeStatus == PERMANENTLY_CLOSE) {
            throw new Exception("Store is permanently closed and cannot be opened");
        } else {
            storeStatus = OPEN;
            List<Integer> storeOwnersAndManagers = new ArrayList<>();
            storeOwnersAndManagers.addAll(storeOwners);
            storeOwnersAndManagers.addAll(storeManagers);
            storeMailBox.sendMessageToList(storeOwnersAndManagers, "Store opened", "Store " + storeName + " has opened");
            log.info("Store " + storeID + " opened");
            return true;
        }
    }

    public boolean closeStore() throws Exception
    {
        if (storeStatus == CLOSE) {
            throw new Exception("Store is already close");
        } else if (storeStatus == PERMANENTLY_CLOSE) {
            throw new Exception("Store is permanently close and cannot change its status to close");
        } else {
            storeStatus = CLOSE;
            List<Integer> storeOwnersAndManagers = new ArrayList<>();
            storeOwnersAndManagers.addAll(storeOwners);
            storeOwnersAndManagers.addAll(storeManagers);
            storeMailBox.sendMessageToList(storeOwnersAndManagers, "Store closed", "Store " + storeName + " has closed");
            log.info("Store " + storeID + " closed");
            return true;
        }
    }

    public boolean closeStorePermanently() throws Exception
    {
        if (storeStatus == PERMANENTLY_CLOSE) {
            throw new Exception("Store is already permanently closed");
        } else {
            storeStatus = PERMANENTLY_CLOSE;
            List<Integer> storeOwnersAndManagers = new ArrayList<>();
            storeOwnersAndManagers.addAll(storeOwners);
            storeOwnersAndManagers.addAll(storeManagers);
            storeMailBox.sendMessageToList(storeOwnersAndManagers, "Store closed permanently", "Store " + storeName + " has closed permanently");
            log.info("Store " + storeID + " is permanently closed");
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
