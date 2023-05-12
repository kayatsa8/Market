package BusinessLayer.Stores.Policies.Conditions.LogicalCompositions.Rules;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public class ForbiddenDatesRule extends Rule {
    public ForbiddenDatesRule(int id) {
        super(id);
    }

    @Override
    public boolean checkConditions(List<CartItemInfo> basketItems) {
        return false;
    }
}
