package BusinessLayer.Stores.Policies.Conditions.NumericCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Policies.Discounts.Discount;

import java.util.ArrayList;
import java.util.List;

public class Add extends NumericComposite
{
    public Add(int id, List<Discount> discounts)
    {
        super(id, discounts);
    }

    protected List<CartItemInfo> updateBasketByNumericComposite(List<List<CartItemInfo>> tempBaskets)
    {
        List<CartItemInfo> result = tempBaskets.get(0);
        if (tempBaskets.size() > 1)
        {
            for (int j = 1; j < tempBaskets.size(); j++)
            {
                List<CartItemInfo> items = tempBaskets.get(j);
                for (int i = 0; i < result.size(); i++)
                {
                    CartItemInfo currentItem = result.get(i);
                    double currentPercent = currentItem.getPercent();
                    double addPercent = items.get(i).getPercent();
                    currentItem.setPercent(currentPercent + addPercent); //This line is the main logic of the class
                }
            }
        }
        return result;
    }
}