package BusinessLayer.Stores.Policies.Conditions.NumericCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Policies.Discounts.Discount;

import java.util.ArrayList;
import java.util.List;

public abstract class NumericComposite extends Discount
{
    private List<Discount> discounts;
    public NumericComposite(int discountID, List<Discount> discounts)
    {
        super(discountID);
        this.discounts = discounts;
    }
    public List<Discount> getDiscounts()
    {
        return discounts;
    }

    public List<CartItemInfo> updateBasket(List<CartItemInfo> basketItems, List<String> coupons)
    {
        List<List<CartItemInfo>> tempBaskets = new ArrayList<>();
        for (Discount numericComponent: getDiscounts())
        {
            tempBaskets.add(numericComponent.updateBasket(basketItems, coupons));
        }
        return updateBasketByNumericComposite(tempBaskets);
    }
    protected abstract List<CartItemInfo> updateBasketByNumericComposite(List<List<CartItemInfo>> tempBaskets);
}
