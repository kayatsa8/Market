package BusinessLayer.Stores.Discounts.DiscountsTypes;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Discounts.Discount;
import BusinessLayer.Stores.Discounts.DiscountScopes.DiscountScope;
import BusinessLayer.Stores.Store;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class DiscountType extends Discount {
    protected double percent;
    protected Calendar endOfSale;
    private DiscountScope discountScope;
    protected Store store;
    @Override
    public String toString() {
        return  "Percent is: " + percent + ", " +
                "End of sale is at: " + getEndOfSaleString(endOfSale) + ", " +
                discountScope.toString();
    }
    private String getEndOfSaleString(Calendar endOfSale)
    {
        return  endOfSale.get(5) + "." + endOfSale.get(2) + "." + endOfSale.get(1);
    }
    public void removeItem(int itemID)
    {
        discountScope.removeItem(itemID);
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

    public boolean isDiscountApplyForItem(int itemID, String category)
    {
        return discountScope.isDiscountApplyForItem(itemID, category);
    }
}
