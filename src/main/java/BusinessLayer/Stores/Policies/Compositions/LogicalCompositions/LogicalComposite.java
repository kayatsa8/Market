package BusinessLayer.Stores.Policies.Compositions.LogicalCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public abstract class LogicalComposite implements LogicalComponent {
    private List<LogicalComponent> components;
    public LogicalComposite(List<LogicalComponent> components)
    {
        this.components = components;
    }

    public List<LogicalComponent> getComponents() {
        return components;
    }
}
