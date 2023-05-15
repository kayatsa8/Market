package BusinessLayer.Stores.Conditions.LogicalCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public class And extends LogicalComposite {
    public And(List<LogicalComponent> components, int id)
    {
        super(components, id);
    }

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

    @Override
    public String toString()
    {
        String result = "";
        for (LogicalComponent logicalComponent : getComponents())
        {
            result += " & " + logicalComponent.toString();
        }
        if (result.length()>1)
            result = result.substring(3);
        return "(" + result + ")";
    }
}