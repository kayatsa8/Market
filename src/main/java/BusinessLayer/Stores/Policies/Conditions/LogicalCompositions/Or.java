package BusinessLayer.Stores.Policies.Conditions.LogicalCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public class Or extends LogicalComposite {

    public Or(List<LogicalComponent> components, int id)
    {
        super(components, id);
    }

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
    @Override
    public String toString()
    {
        String result = "";
        for (LogicalComponent logicalComponent : getComponents())
        {
            result += " | " + logicalComponent.toString();
        }
        if (result.length()>1)
            result = result.substring(3);
        return "(" + result + ")";
    }
}