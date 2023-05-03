package BusinessLayer.Stores.Policies.Compositions.LogicalCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public class Or extends LogicalComposite {

    public Or(List<LogicalComponent> components)
    {
        super(components);
    }
    @Override
    public boolean checkConditions(List<CartItemInfo> basketItems)
    {
        for (LogicalComponent component : getComponents())
        {
            if (component.checkConditions(basketItems))
            {
                return true;
            }
        }
        return false;
    }
}