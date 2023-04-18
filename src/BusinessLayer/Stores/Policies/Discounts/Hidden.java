package BusinessLayer.Stores.Policies.Discounts;

import java.util.Calendar;
import java.util.Date;

public class Hidden extends Discount{

    private int itemId;
    private double discount;
    private Calendar endOfSale;
    private String coupon;

    public Hidden(int itemId, double discount, Calendar endOfSale, String coupon){
        super();
        this.discount = discount;
        this.itemId = itemId;
        this.endOfSale = endOfSale;
        this.coupon = coupon;
    }



    @Override
    public double getDiscountToItem() {
        return 0;
    }

    public double getDiscountWithCoupon(String coupon){
        if(couponValid(coupon) & getDaysUntilExpired() >= 0){
            return discount;
        }
        return 0;
    }


    public Calendar getEndOfSale() {
        return endOfSale;
    }

    public int getDaysUntilExpired() {
        Calendar now = Calendar.getInstance();
        long diff = endOfSale.getTime().getTime() - now.getTime().getTime();
        return (int) diff/(1000*60*60*24);
    }

    public boolean couponValid(String coupon){
        return coupon == this.coupon;
    }




}
