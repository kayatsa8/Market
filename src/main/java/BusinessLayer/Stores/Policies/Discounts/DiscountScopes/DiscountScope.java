package BusinessLayer.Stores.Policies.Discounts.DiscountScopes;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public interface DiscountScope {
    void setItemsPercents(List<CartItemInfo> copyBasket, double percent);

    boolean isDiscountApplyForItem(int itemID, String category);
}
