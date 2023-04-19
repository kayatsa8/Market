package BusinessLayer.Stores.Policies.Discounts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Hidden extends Discount{

    private int itemId;
    private String coupon;

    public Hidden(int itemId, double discount, Calendar endOfSale, String coupon){
        super();
        this.itemId = itemId;
        this.coupon = coupon;
    }

    public List<Integer> getItemsIDs()
    {
        List<Integer> item = new ArrayList<>();
        item.add(itemId);
        return item;
    }

    @Override
    public double getDiscountToItem() {
        if(isExpired())
        {
            return 0;
        }
        return getPercent();
    }

    public boolean isExpired() {
        Calendar now = Calendar.getInstance();
        return now.after(getExpiringDate());
    }

    public boolean couponValid(String coupon){
        return coupon == this.coupon;
    }
}
