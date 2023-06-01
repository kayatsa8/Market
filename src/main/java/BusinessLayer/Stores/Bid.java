package BusinessLayer.Stores;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static BusinessLayer.Stores.BidReplies.*;

public class Bid {
    private int bidID;
    private int itemID;
    private double offeredPrice;
    private double originalPrice;
    private int userID;
    private boolean bidRejected;
    private double highestCounterOffer;
    private Map<Integer, BidReplies> repliers;
    public Bid(int bidID, int itemID, int userID, double offeredPrice, double originalPrice)
    {
        this.bidID = bidID;
        this.itemID = itemID;
        this.offeredPrice = offeredPrice;
        this.originalPrice = originalPrice;
        this.userID = userID;
        this.highestCounterOffer = -1;
        this.bidRejected = false;
        this.repliers = new HashMap<>();
    }
    public void setRepliers(List<Integer> repliersIDs)
    {
        for (Integer replierID : repliersIDs)
        {
            repliers.put(replierID, null);
        }
    }
    public boolean isUserNeedToReply(int userID)
    {
        return (repliers.containsKey(userID) && repliers.get(userID) == null);
    }

    public int getBidID() { return bidID; }
    public int getItemID()
    {
        return itemID;
    }
    public int getUserID() { return userID; }
    public double getHighestCounterOffer()
    {
        return highestCounterOffer;
    }
    public boolean approve(int replierUserID) throws Exception
    {
        if (!repliers.keySet().contains(replierUserID))
        {
            throw new Exception("The user " + replierUserID + " is not allowed to reply to bid in this store");
        }
        if (repliers.get(replierUserID) != null)
        {
            throw new Exception("The user " + replierUserID + " has already replied to this bid");
        }
        repliers.put(replierUserID, APPROVED);
        if (allReplied())
        {
            return true;
        }
        return false;
    }
    public boolean reject(int replierUserID) throws Exception
    {
        if (!repliers.keySet().contains(replierUserID))
        {
            //return false;
            throw new Exception("The user " + replierUserID + " is not allowed to reply to bid in this store");
        }
        if (repliers.get(replierUserID) != null)
        {
            throw new Exception("The user " + replierUserID + " has already replied to this bid");
        }
        repliers.put(replierUserID, REJECTED);
        if (bidRejected)
            return false;
        bidRejected = true;
        //finishBid();
        return true;
    }
    public boolean counterOffer(int replierUserID, double counterOffer) throws Exception
    {
        if (!repliers.keySet().contains(replierUserID))
        {
            //return false;
            throw new Exception("The user " + replierUserID + " is not allowed to reply to bid in this store");
        }
        if (repliers.get(replierUserID) != null)
        {
            throw new Exception("The user " + replierUserID + " has already replied to this bid");
        }
        repliers.put(replierUserID, COUNTERED);
        if (counterOffer > highestCounterOffer)
        {
            highestCounterOffer = counterOffer;
        }
        if (allReplied())
        {
            //finishBid();
            return true;
        }
        return false;
    }

    private boolean allReplied()
    {
        for (Map.Entry<Integer, BidReplies> entry : repliers.entrySet())
        {
            if (entry.getValue() == null) {
                return false;
            }
        }
        return true;
    }

    /*private void finishBid()
    {
        if (bidRejected) //reject
        {

        }
        else if (highestCounterOffer == -1) //approve
        {

        }
        else //counter-offer
        {

        }
    }*/

    public Map<Integer, BidReplies> getRepliers()
    {
        return repliers;
    }
}