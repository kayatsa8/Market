package BusinessLayer.Stores.Conditions.LogicalCompositions;

import java.util.List;

public abstract class LogicalComposite extends LogicalComponent {
    private List<LogicalComponent> components;
    public LogicalComposite(List<LogicalComponent> components, int id)
    {
        super(id);
        this.components = components;
    }

    public List<LogicalComponent> getComponents() {
        return components;
    }

    public boolean isApplyForItem(int itemID, String category)
    {
        for (LogicalComponent logicalComponent : components)
        {
            if (logicalComponent.isApplyForItem(itemID, category))
            {
                return true;
            }
        }
        return false;
    }

    public void removeItem(int itemID)
    {
        for (LogicalComponent logicalComponent : components)
        {
            logicalComponent.removeItem(itemID);
        }
    }
}
