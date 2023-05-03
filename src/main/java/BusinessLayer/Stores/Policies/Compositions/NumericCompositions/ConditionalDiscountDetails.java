package BusinessLayer.Stores.Policies.Compositions.NumericCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Policies.Compositions.LogicalCompositions.LogicalComponent;

import java.util.ArrayList;
import java.util.List;


//The leaves of numeric trees
//Wraps the root of logical tree
public class ConditionalDiscountDetails implements NumericComponent {
    private double percent;
    private List<Integer> itemIDs;
    private LogicalComponent root;

    public ConditionalDiscountDetails(double percent, List<Integer> itemIDs, LogicalComponent root)
    {
        this.percent = percent;
        this.itemIDs = itemIDs;
        this.root = root;
    }

    @Override
    public List<CartItemInfo> updateBasket(List<CartItemInfo> basketItems) {
        if(checkConditions(basketItems))
        {
            List<CartItemInfo> copyBasket = new ArrayList<>();
            for (CartItemInfo item: basketItems)
            {
                copyBasket.add(new CartItemInfo(item));
            }
            for (Integer itemID : itemIDs)
            {
                for (CartItemInfo item: copyBasket)
                {
                    if (item.getItemID() == itemID)
                    {
                        item.setPercent(percent);
                    }
                }
            }
            return copyBasket;
        }
        else //return basket with all items with 0 percent
        {
            List<CartItemInfo> noDiscountBasket = new ArrayList<>();
            for (CartItemInfo item : basketItems) {
                CartItemInfo noDiscountItem = new CartItemInfo(item);
                noDiscountItem.setPercent(0);
                noDiscountBasket.add(noDiscountItem);
            }
            return noDiscountBasket;
        }
    }

    private boolean checkConditions(List<CartItemInfo> basketItems)
    {
        return root.checkConditions(basketItems);
    }
}
