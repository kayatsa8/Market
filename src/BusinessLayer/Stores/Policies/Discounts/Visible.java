package BusinessLayer.Stores.Policies.Discounts;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

public class Visible extends Discount{


    private int itemId;
    private double discount;
    private Date expiringDate;

    public Visible(int itemId, double discount, Date date){
        super();
        this.discount = discount;
        this.itemId = itemId;
        this.expiringDate = date;
    }



    @Override
    public double getDiscountToItem() {
        if(getDaysUntilExpired() < 0){
            return 0;
        }
        return discount;
    }

    public Date getExpiringDate() {
        return expiringDate;
    }

    public int getDaysUntilExpired() {
        Date now = Calendar.getInstance().getTime();
        long diff = expiringDate.getTime() - now.getTime();
        return (int) diff/(1000*60*60*24);
    }
}
