package BusinessLayer.Stores.Policies.Rules;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;
import java.util.Map;

public class DiscountQuantityRule implements Rule{

    private Map<Integer, Integer> itemsAmounts;
    public DiscountQuantityRule(Map<Integer, Integer> itemsAmounts)
    {
        this.itemsAmounts = itemsAmounts;
    }

    @Override
    public boolean checkConditions(List<CartItemInfo> basketItems)
    {
        for (Map.Entry<Integer, Integer> itemAmount : itemsAmounts.entrySet())
        {
            if (!checkIfEnoughItemAmountInBasket(itemAmount, basketItems))
            {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfEnoughItemAmountInBasket(Map.Entry<Integer, Integer> itemAmount, List<CartItemInfo> basketItems)
    {
        for (CartItemInfo item : basketItems)
        {
            if (itemAmount.getKey() == item.getItemID())
            {
                if (itemAmount.getValue() <= item.getAmount())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        return false;
    }
}
