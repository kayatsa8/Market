package BusinessLayer.Stores.Discounts.DiscountsTypes;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Discounts.DiscountScopes.DiscountScope;

import java.util.Calendar;
import java.util.List;

public class Hidden extends DiscountType {
    private String coupon;

    public Hidden(int discountID, double percent, Calendar endOfSale, String coupon, DiscountScope discountScope){
        super(discountID, percent, endOfSale, discountScope);
        this.coupon = coupon;
    }

    protected boolean checkConditions(List<CartItemInfo> basketItems, List<String> coupons)
    {
        return coupons.contains(coupon);
    }
}
