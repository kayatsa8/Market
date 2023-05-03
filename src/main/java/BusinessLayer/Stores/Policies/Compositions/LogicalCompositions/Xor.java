package BusinessLayer.Stores.Policies.Compositions.LogicalCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public class Xor extends LogicalComposite
{
    public Xor(List<LogicalComponent> components)
    {
        super(components);
    }
    @Override
    public boolean checkConditions(List<CartItemInfo> basketItems)
    {
        int truesCounter = 0;
        for (LogicalComponent component : getComponents())
        {
            if (component.checkConditions(basketItems))
            {
                truesCounter++;
            }
        }
        if (truesCounter == 1)
        {
            return true;
        }
        return false;
    }
}