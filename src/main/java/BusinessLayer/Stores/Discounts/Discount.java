package BusinessLayer.Stores.Discounts;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Discounts.DiscountScopes.DiscountScope;

import java.util.List;

public abstract class Discount {
    private int discountID;
    public Discount(int discountID)
    {
        this.discountID = discountID;
    }
    public abstract List<CartItemInfo> updateBasket(List<CartItemInfo> basketItems, List<String> coupons);
    public int getDiscountID() { return discountID; }
    public abstract boolean isDiscountApplyForItem(int itemID, String category);
    public abstract void removeItem(int itemID);
}
