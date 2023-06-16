package BusinessLayer.Stores.Discounts.DiscountsTypes;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Discounts.DiscountScopes.DiscountScope;

import javax.persistence.Entity;
import java.util.Calendar;
import java.util.List;

@Entity
public class Visible extends DiscountType {

    public Visible(int discountID, double percent, Calendar endOfSale, DiscountScope discountScope){
        super(discountID, percent, endOfSale, discountScope);
    }

    public Visible() {

    }

    protected boolean checkConditions(List<CartItemInfo> basketItems, List<String> coupons)
    {
        return true;
    }
}