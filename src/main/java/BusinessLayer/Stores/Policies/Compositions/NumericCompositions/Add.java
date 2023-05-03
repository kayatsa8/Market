package BusinessLayer.Stores.Policies.Compositions.NumericCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.ArrayList;
import java.util.List;

public class Add extends NumericComposite
{

    public Add(List<NumericComponent> numericComponents)
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
        return updateBasketByAdd(tempBaskets);
    }

    private List<CartItemInfo> updateBasketByAdd(List<List<CartItemInfo>> tempBaskets)
    {
        List<CartItemInfo> result = new ArrayList<>();
        boolean first = true;
        for (List<CartItemInfo> items : tempBaskets)
        {
            if (first)
            {
                result = items;
                first = false;
            }
            else
            {
                for (int i=0; i<result.size(); i++)
                {
                    CartItemInfo currentItem = result.get(i);
                    double currentPercent = currentItem.getPercent();
                    double addPercent = items.get(i).getPercent();
                    currentItem.setPercent(currentPercent+addPercent); //This line is the main logic of the class
                }
            }
        }
        return result;
    }
}