package BusinessLayer.Stores.Policies.Conditions.LogicalCompositions.Rules;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.Calendar;
import java.util.List;

public class MustDatesRule extends Rule {
    private List<Calendar> forbiddenDates;
    public MustDatesRule(List<Calendar> forbiddenDates, int id) {
        super(id);
        this.forbiddenDates = forbiddenDates;
    }

    @Override
    public boolean checkConditions(List<CartItemInfo> basketItems) {
        Calendar now = Calendar.getInstance();
        for (Calendar date : forbiddenDates) {
            if (now.get(6) == date.get(6)) //6 = DAY_OF_YEAR (1-365, or 1-366 on leap years)
                return false;
        }
        return true;
    }
}
