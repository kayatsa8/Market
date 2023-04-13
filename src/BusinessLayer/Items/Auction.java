package BusinessLayer.Items;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Auction {
    private double initialPrice;
    private double currentPrice;
    private int currentWinningUserID;
    private Map<Integer, Double> offers;
    private Calendar endOfSale;
    private Timer auctionTimer;

    public Auction(double initialPrice, int auctionPeriodInDays)
    {
        endOfSale = Calendar.getInstance();
        endOfSale.add(Calendar.DAY_OF_MONTH, auctionPeriodInDays);
        endOfSale.set(Calendar.SECOND, 0);
        endOfSale.set(Calendar.HOUR_OF_DAY, 0);
        endOfSale.set(Calendar.MINUTE, 0);
        this.initialPrice = initialPrice;
        this.currentPrice = initialPrice;
        this.currentWinningUserID = -1;
        this.offers = new HashMap<>();
        this.auctionTimer = new Timer();
        TimerTask endOfAuctionTask = new TimerTask()
        {
            @Override
            public void run()
            {
                if (currentWinningUserID == -1)
                {
                    cancel();
                }
                else
                {
                    System.out.println("The item is sold to user ID: " + currentWinningUserID + " at a price of " + currentPrice); //The task
                }
            }
        };
        auctionTimer.schedule(endOfAuctionTask, endOfSale.getTime());
    }

    public boolean offer(int userId, double offerPrice)
    {
        if (Calendar.getInstance().before(endOfSale))
        {
            if (offerPrice > currentPrice)
            {
                currentPrice = offerPrice;
                currentWinningUserID = userId;
                offers.put(userId, offerPrice);
                return true;
            }
        }
        return false;
    }

    public int getDaysLeft()
    {
        Calendar now = Calendar.getInstance();
        if (now.after(endOfSale))
            return 0;
        return (int)TimeUnit.MILLISECONDS.toDays(endOfSale.getTimeInMillis()-now.getTimeInMillis());
    }

    public int getCurrentWinningUserID()
    {
        return currentWinningUserID;
    }
    public double getCurrentPrice()
    {
        return currentPrice;
    }
}
