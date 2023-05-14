package BusinessLayer.Stores.Conditions.LogicalCompositions.Rules;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public class ForbiddenCategoryRule extends Rule {
    private String forbiddenCategory;
    public ForbiddenCategoryRule(String forbiddenCategory, int id) {
        super(id);
        this.forbiddenCategory = forbiddenCategory;
    }

    @Override
    public boolean checkConditions(List<CartItemInfo> basketItems) {
        for (CartItemInfo item : basketItems)
        {
            if (item.getCategory() == forbiddenCategory)
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        return  "(Basket does not contain items from category: " + forbiddenCategory + ")";
    }

    public boolean isApplyForItem(int itemID, String category)
    {
        return forbiddenCategory.equals(category);
    }
}
