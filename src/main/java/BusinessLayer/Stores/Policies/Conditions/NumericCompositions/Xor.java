package BusinessLayer.Stores.Policies.Conditions.NumericCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Policies.Conditions.LogicalCompositions.LogicalComponent;
import BusinessLayer.Stores.Policies.Conditions.NumericCompositions.NumericComposite;
import BusinessLayer.Stores.Policies.Discounts.Discount;

import java.util.List;

public abstract class Xor extends NumericComposite
{
    public Xor(int id, List<Discount> discounts)
    {
        super(id, discounts);
    }
    protected double getBasketTotalPrice(List<CartItemInfo> items)
    {
        double result = 0;
        for (CartItemInfo item : items) {
            result += item.getFinalPrice();
        }
        return result;
    }
}