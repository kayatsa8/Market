package BusinessLayer.Stores.Policies.Discounts.DiscountsTypes;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Policies.Discounts.DiscountScopes.DiscountScope;

import java.util.Calendar;
import java.util.List;

public class Visible extends DiscountType {

    public Visible(int discountID, double percent, Calendar endOfSale, DiscountScope discountScope){
        super(discountID, percent, endOfSale, discountScope);
    }

    protected boolean checkConditions(List<CartItemInfo> basketItems, List<String> coupons)
    {
        return true;
    }
}