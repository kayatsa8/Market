package BusinessLayer.Stores.Policies.Conditions.LogicalCompositions;

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
}
