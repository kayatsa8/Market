package BusinessLayer.Stores.Policies.Discounts;

import java.util.Calendar;
import java.util.List;

public abstract class Discount {
    private double percent;
    private Calendar expiringDate;

    public Discount(){
        percent = 0;
        expiringDate = Calendar.getInstance();
        expiringDate.add(Calendar.HOUR, 1);
    }

    public double getPercent()
    {
        return percent;
    }
    public Calendar getExpiringDate()
    {
        return expiringDate;
    }
    public abstract List<Integer> getItemsIDs();
    public abstract double getDiscountToItem();





}
