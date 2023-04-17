package BusinessLayer.Stores;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static BusinessLayer.Stores.BidReplies.*;

public class Bid {
    private int itemID;
    private double offeredPrice;
    private int userID;
    private boolean bidRejected;
    private double highestCounterOffer;
    private Map<Integer, BidReplies> repliers;
    public Bid(int itemID, int userID, double offeredPrice)
    {
        this.itemID = itemID;
        this.offeredPrice = offeredPrice;
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

    public boolean approve(int replierUserID)
    {
        if (!repliers.keySet().contains(replierUserID) || repliers.get(replierUserID) != null)
        {
            return false;
        }
        repliers.put(replierUserID, APPROVED);
        if (allReplied())
        {
            finishBid();
        }
        return true;
    }
    public boolean reject(int replierUserID)
    {
        if (!repliers.keySet().contains(replierUserID) || repliers.get(replierUserID) != null)
        {
            return false;
        }
        repliers.put(replierUserID, REJECTED);
        bidRejected = true;
        finishBid();
        return true;
    }
    public boolean counterOffer(int replierUserID, double counterOffer)
    {
        if (!repliers.keySet().contains(replierUserID) || repliers.get(replierUserID) != null)
        {
            return false;
        }
        repliers.put(replierUserID, COUNTERED);
        if (counterOffer > highestCounterOffer)
        {
            highestCounterOffer = counterOffer;
        }
        if (allReplied())
        {
            finishBid();
        }
        return true;
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

    private void finishBid()
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
    }

    public Map<Integer, BidReplies> getRepliers()
    {
        return repliers;
    }
}