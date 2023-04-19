package BusinessLayer.Stores.Policies.Discounts;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

public class Visible extends Discount{


    private int itemId;
    private double discount;
    private Calendar expiringDate;

    public Visible(int itemId, double discount, Calendar endOfSale){
        super();
        this.discount = discount;
        this.itemId = itemId;
        this.expiringDate = endOfSale;
    }



    @Override
    public double getDiscountToItem() {
        if(getDaysUntilExpired() < 0){
            return 0;
        }
        return discount;
    }

    public Calendar getExpiringDate() {
        return expiringDate;
    }

    public int getDaysUntilExpired() {
        Calendar now = Calendar.getInstance();
        long diff = expiringDate.getTime().getTime() - now.getTime().getTime();
        return (int) diff/(1000*60*60*24);
    }
}
