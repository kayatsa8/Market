package BusinessLayer.Stores.Policies.Conditions.LogicalCompositions.Rules;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public class BuyerAgeRule extends Rule { //you must be at the minimum age or older to pass the rule.
    private int minimumAge;
    public BuyerAgeRule(int minimumAge, int id) {
        super(id);
        this.minimumAge = minimumAge;
    }

    @Override
    public boolean checkConditions(List<CartItemInfo> basketItems) { //need to add user's age
        return false;
        //return minimumAge <= buyerAge;
    }

    @Override
    public String toString()
    {
        return  "(Buyer must be at least: " + minimumAge + " years old)";
    }

    public boolean isApplyForItem(int itemID, String category)
    {
        return false;
    }
}
