package BusinessLayer.Stores.Policies.Discounts;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Hidden implements Discount{

    private int itemId;
    private double percent;
    private Calendar endOfSale;
    private String coupon;

    public Hidden(int itemId, double percent, Calendar endOfSale, String coupon){
        super();
        this.itemId = itemId;
        this.coupon = coupon;
        this.percent = percent;
        this.endOfSale = endOfSale;
    }

    public boolean couponValid(String coupon){
        return coupon == this.coupon;
    }

    @Override
    public List<CartItemInfo> updateBasket(List<CartItemInfo> basketItems, List<String> coupons) {
        List<CartItemInfo> copyBasket = new ArrayList<>();
        for (CartItemInfo item: basketItems)
        {
            copyBasket.add(new CartItemInfo(item));
        }
        boolean couponIsValid = false;
        for (String currCoupon : coupons)
        {
            if (couponValid(currCoupon))
            {
                couponIsValid = true;
            }
        }
        for (CartItemInfo item : copyBasket) {
            if (item.getItemID() == itemId && couponIsValid)
                item.setPercent(percent);
            else
                item.setPercent(0);
        }
        return copyBasket;
    }
}
