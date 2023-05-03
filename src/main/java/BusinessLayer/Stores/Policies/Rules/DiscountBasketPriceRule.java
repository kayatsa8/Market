package BusinessLayer.Stores.Policies.Rules;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Policies.Discounts.Discount;

import java.util.List;


//Basket total price must be at least "minimumPrice" (without discounts)

public class DiscountBasketPriceRule implements Rule
{
    double minimumPrice;
    public DiscountBasketPriceRule(double minimumPrice)
    {
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
}
