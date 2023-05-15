package BusinessLayer.Stores.Conditions.LogicalCompositions.Rules;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public class BasketWeightLimitRule extends Rule
{
    private double basketWeightLimit;

    public BasketWeightLimitRule(double basketWeightLimit, int id)
    {
        super(id);
        this.basketWeightLimit = basketWeightLimit;
    }

    @Override
    public boolean checkConditions(List<CartItemInfo> basketItems)
    {
        return getBasketTotalWeight(basketItems) <= basketWeightLimit;
    }

    private double getBasketTotalWeight(List<CartItemInfo> basketItems)
    {
        double totalWeight = 0;
        for (CartItemInfo item : basketItems)
        {
            totalWeight += item.getWeight() * item.getAmount();
        }
        return totalWeight;
    }

    @Override
    public String toString()
    {
        return  "(Basket's total weight is at most: " + basketWeightLimit + " KG)";
    }

    public boolean isApplyForItem(int itemID, String category)
    {
        return false;
    }
}
