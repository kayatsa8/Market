package BusinessLayer.Stores.Policies.Compositions.NumericCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.ArrayList;
import java.util.List;

public class Max extends NumericComposite
{
    public Max(List<NumericComponent> numericComponents)
    {
        super(numericComponents);
    }

    @Override
    public List<CartItemInfo> updateBasket(List<CartItemInfo> basketItems)
    {
        List<List<CartItemInfo>> tempBaskets = new ArrayList<>();
        List<CartItemInfo> currTempBasket;
        for (NumericComponent numericComponent: getNumericComponents())
        {
            currTempBasket = new ArrayList<>();
            copyBasket(currTempBasket, basketItems);
            numericComponent.updateBasket(currTempBasket);
            tempBaskets.add(currTempBasket);
        }
        return getCheapestBasket(tempBaskets);
    }

    private List<CartItemInfo> getCheapestBasket(List<List<CartItemInfo>> tempBaskets)
    {
        double currentCheapestPrice = -1;
        double tempPrice;
        List<CartItemInfo> currentCheapestBasket = new ArrayList<>();
        for (List<CartItemInfo> items : tempBaskets) {
            tempPrice = getBasketTotalPrice(items);
            if (currentCheapestPrice == -1) {
                copyBasket(currentCheapestBasket, items);
                currentCheapestPrice = tempPrice;
            }
            else {
                currentCheapestPrice = Math.min(tempPrice, currentCheapestPrice);
                if (currentCheapestPrice == tempPrice) {
                    copyBasket(currentCheapestBasket, items);
                }
            }
        }
        return currentCheapestBasket;
    }

    private double getBasketTotalPrice(List<CartItemInfo> items)
    {
        double result = 0;
        for (CartItemInfo item : items) {
            result += item.getFinalPrice();
        }
        return result;
    }
}