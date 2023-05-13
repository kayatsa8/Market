package BusinessLayer.Stores.Policies.Discounts.DiscountScopes;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public class ItemsDiscount implements DiscountScope {

    private List<Integer> itemIDs;

    public ItemsDiscount(List<Integer> itemIDs) {
        this.itemIDs = itemIDs;
    }

    public void setItemsPercents(List<CartItemInfo> copyBasket, double percent) //ByItemsList
    {
        for (CartItemInfo item: copyBasket)
        {
            if (itemIDs.contains(item.getItemID()))
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
        return "Discount is applied on the items IDs: " + itemIDs;
    }

    public boolean isDiscountApplyForItem(int itemID, String category)
    {
        return itemIDs.contains(itemID);
    }
}
