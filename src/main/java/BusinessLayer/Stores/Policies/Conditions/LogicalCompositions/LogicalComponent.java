package BusinessLayer.Stores.Policies.Conditions.LogicalCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public abstract class LogicalComponent {
    private int id;
    public LogicalComponent(int id)
    {
        this.id = id;
    }
    public abstract boolean checkConditions(List<CartItemInfo> basketItems);
    public int getID() { return id; }
}