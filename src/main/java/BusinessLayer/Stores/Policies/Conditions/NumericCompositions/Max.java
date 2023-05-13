package BusinessLayer.Stores.Policies.Conditions.NumericCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Policies.Discounts.Discount;

import java.util.ArrayList;
import java.util.List;

public class Max extends Xor //apply only the best discount for a given basket
{
    public Max(int id, List<Discount> discounts)
    {
        super(id, discounts);
    }

    protected List<CartItemInfo> updateBasketByNumericComposite(List<List<CartItemInfo>> tempBaskets)
    {
        double currentCheapestPrice = -1;
        List<CartItemInfo> currentCheapestBasket = new ArrayList<>();

        double tempPrice;
        for (List<CartItemInfo> items : tempBaskets) {
            tempPrice = getBasketTotalPrice(items);
            if (currentCheapestPrice == -1) {
                currentCheapestBasket = items;
                currentCheapestPrice = tempPrice;
            }
            else {
                currentCheapestPrice = Math.min(tempPrice, currentCheapestPrice);
                if (currentCheapestPrice == tempPrice) {
                    currentCheapestBasket = items;
                }
            }
        }
        return currentCheapestBasket;
    }
}