package BusinessLayer.Stores.Policies.Discounts;

import java.util.Calendar;
import java.util.Date;

public class Hidden extends Discount{



    private int itemId;
    private double discount;
    private Date expiringDate;
    private double coupon;

    public Hidden(int itemId, double discount, Date date, double coupon){
        super();
        this.discount = discount;
        this.itemId = itemId;
        this.expiringDate = date;
        this.coupon = coupon;
    }



    @Override
    public double getDiscountToItem() {
        return 0;
    }

    public double getDiscountWithCoupon(double coupon){
        if(couponValid(coupon) & getDaysUntilExpired() >= 0){
            return discount;
        }
        return 0;
    }


    public Date getExpiringDate() {
        return expiringDate;
    }

    public int getDaysUntilExpired() {
        Date now = Calendar.getInstance().getTime();
        long diff = expiringDate.getTime() - now.getTime();
        return (int) diff/(1000*60*60*24);
    }

    public boolean couponValid(double coupon){
        return coupon == this.coupon;
    }




}
