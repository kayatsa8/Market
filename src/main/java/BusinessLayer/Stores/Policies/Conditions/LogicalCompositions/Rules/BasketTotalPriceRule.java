package BusinessLayer.Stores.Policies.Conditions.LogicalCompositions.Rules;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;


//Basket total price must be at least "minimumPrice" (without discounts)

public class BasketTotalPriceRule extends Rule
{
    double minimumPrice;
    public BasketTotalPriceRule(double minimumPrice, int id)
    {
        super(id);
        this.minimumPrice = minimumPrice;
    }

    @Override
    public boolean checkConditions(List<CartItemInfo> basketItems)
    {
        return (minimumPrice <= getBasketPrice(basketItems));
    }

    private double getBasketPrice(List<CartItemInfo> basketItems)
    {
        double price = 0;
        for (CartItemInfo item : basketItems)
        {
            price += item.getOriginalPrice()*item.getAmount(); //Price without discounts percent
        }
        return price;
    }

    @Override
    public String toString()
    {
        return "(Basket must have a total price of at least " + minimumPrice + " not including discounts)";
    }
}
