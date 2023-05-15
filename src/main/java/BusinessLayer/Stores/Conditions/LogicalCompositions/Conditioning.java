package BusinessLayer.Stores.Conditions.LogicalCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public class Conditioning extends LogicalComponent {    // if you break first -> you must apply second
    private LogicalComponent firstCondition;
    private LogicalComponent secondCondition;


    public Conditioning(LogicalComponent firstCondition, LogicalComponent secondCondition, int id)
    {
        super(id);
        this.firstCondition = firstCondition;
        this.secondCondition = secondCondition;
    }

    public boolean checkConditions(List<CartItemInfo> basketItems)
    {
        if (!firstCondition.checkConditions(basketItems) && !secondCondition.checkConditions(basketItems))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "(" + firstCondition.toString() + " unless " + secondCondition.toString() + ")";
    }

    public boolean isApplyForItem(int itemID, String category)
    {
        return (firstCondition.isApplyForItem(itemID, category) || secondCondition.isApplyForItem(itemID, category));
    }

}