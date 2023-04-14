package BusinessLayer.stores;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Lottery {
    private int itemID;
    private double price;
    private double priceLeft;
    private Calendar endOfSale;
    private boolean lotteryFinished;
    private Map<Integer, Double> winningOdds;
    private Timer lotteryTimer;

    public Lottery(int itemID, double price, int lotteryPeriodInDays)
    {
        this.itemID = itemID;
        this.price = price;
        this.priceLeft = price;
        this.lotteryFinished = false;
        this.endOfSale = Calendar.getInstance();
        this.endOfSale.add(Calendar.DAY_OF_MONTH, lotteryPeriodInDays);
        this.endOfSale.set(Calendar.SECOND, 0);
        this.endOfSale.set(Calendar.HOUR_OF_DAY, 0);
        this.endOfSale.set(Calendar.MINUTE, 0);
        this.winningOdds = new HashMap<>();
        this.lotteryTimer = new Timer();
        TimerTask endOfLotteryTask = new TimerTask()
        {
            @Override
            public void run()
            {
                if (!lotteryFinished)
                    System.out.println("The lottery is canceled and the money is returned to the participants"); //The task
                cancel();
            }
        };
        lotteryTimer.schedule(endOfLotteryTask, endOfSale.getTime());
    }

    public boolean participateInLottery(int userID, double offerPrice)
    {
        if (Calendar.getInstance().before(endOfSale))
        {
            if (offerPrice <= priceLeft && offerPrice>0)
            {
                priceLeft -= offerPrice;
                winningOdds.put(userID, offerPrice);
                if (priceLeft == 0)
                {
                    lotteryFinished = true;
                    int winningUserID = getWinner();
                    System.out.println("The item is sold to user ID: " + winningUserID + " at a price of " + winningOdds.get(winningUserID)); //The task
                }
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
        return (int) TimeUnit.MILLISECONDS.toDays(endOfSale.getTimeInMillis()-now.getTimeInMillis());
    }

    private int getWinner()
    {
        Random random = new Random();
        double randomNumber = random.nextDouble();
        for (Map.Entry<Integer, Double> entry : winningOdds.entrySet())
        {
            randomNumber -= (entry.getValue()/price);
            if (randomNumber <= 0)
            {
                return entry.getKey();
            }
        }
        return -1;
    }
}
