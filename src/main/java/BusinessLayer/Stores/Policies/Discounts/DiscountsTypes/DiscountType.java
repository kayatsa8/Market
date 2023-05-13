package BusinessLayer.Stores.Policies.Discounts.DiscountsTypes;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Policies.Discounts.Discount;
import BusinessLayer.Stores.Policies.Discounts.DiscountScopes.DiscountScope;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class DiscountType extends Discount {
    protected double percent;
    protected Calendar endOfSale;
    private DiscountScope discountScope;

    @Override
    public String toString() {
        return  "Percent is: " + percent + "\n" +
                "End of sale is at: " + endOfSale.toString() + "\n" +
                discountScope.toString();
    }

    public DiscountType(int discountID, double percent, Calendar endOfSale, DiscountScope discountScope)
    {
        super(discountID);
        this.percent = percent;
        this.endOfSale = endOfSale;
        this.discountScope = discountScope;
    }
    public List<CartItemInfo> updateBasket(List<CartItemInfo> basketItems, List<String> coupons) {
        List<CartItemInfo> copyBasket = new ArrayList<>();
        for (CartItemInfo item: basketItems)
        {
            copyBasket.add(new CartItemInfo(item));
        }
        if (checkConditions(basketItems, coupons))
        {
            discountScope.setItemsPercents(copyBasket, percent);
        }
        else
        {
            discountScope.setItemsPercents(copyBasket, 0);
        }
        return copyBasket;
    }
    protected abstract boolean checkConditions(List<CartItemInfo> basketItems, List<String> coupons);
}
