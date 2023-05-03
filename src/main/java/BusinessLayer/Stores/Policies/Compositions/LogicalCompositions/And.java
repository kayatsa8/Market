package BusinessLayer.Stores.Policies.Compositions.LogicalCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public class And extends LogicalComposite {
    public And(List<LogicalComponent> components)
    {
        super(components);
    }
    @Override
    public boolean checkConditions(List<CartItemInfo> basketItems)
    {
        for (LogicalComponent component : getComponents())
        {
            if (!component.checkConditions(basketItems))
            {
                return false;
            }
        }
        return true;
    }
}