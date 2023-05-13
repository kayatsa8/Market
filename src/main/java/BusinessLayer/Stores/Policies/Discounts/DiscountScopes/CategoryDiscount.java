package BusinessLayer.Stores.Policies.Discounts.DiscountScopes;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public class CategoryDiscount implements DiscountScope {
    private String category;
    public CategoryDiscount(String category) {
        this.category = category;
    }

    public void setItemsPercents(List<CartItemInfo> copyBasket, double percent) //ByCategory
    {
        for (CartItemInfo item: copyBasket)
        {
            if (item.getCategory().equals(category))
            {
                item.setPercent(percent);
            }
            else
            {
                item.setPercent(0);
            }
        }
    }
    @Override
    public String toString()
    {
        return "Discount is applied on the category: " + category;
    }

    public boolean isDiscountApplyForItem(int itemID, String category)
    {
        return this.category.equals(category);
    }
}
