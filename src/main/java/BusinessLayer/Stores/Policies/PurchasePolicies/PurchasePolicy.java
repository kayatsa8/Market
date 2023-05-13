package BusinessLayer.Stores.Policies.PurchasePolicies;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Policies.Conditions.LogicalCompositions.LogicalComponent;

import java.util.List;

public class PurchasePolicy
{
    private LogicalComponent root;

    public PurchasePolicy(LogicalComponent root)
    {
        this.root = root;
    }

    public LogicalComponent getRoot() {
        return root;
    }

    public boolean isValidForPurchase(List<CartItemInfo> basketItems) throws Exception
    {
        if (root != null)
        {
            return root.checkConditions(basketItems);
        }
        else
        {
            throw new Exception("Purchase policy has no logical component");
        }
    }
    @Override
    public String toString()
    {
        return "The purchase policy condition is: " + root.toString();
    }

    public boolean isPurchasePolicyApplyForItem(int itemID, String category)
    {
        return root.isApplyForItem(itemID, category);
    }
}
