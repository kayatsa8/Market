package BusinessLayer.Stores.Conditions.LogicalCompositions.Rules;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.Calendar;
import java.util.List;

public class MustDatesRule extends Rule {
    private List<Calendar> mustDates;
    public MustDatesRule(List<Calendar> mustDates, int id) {
        super(id);
        this.mustDates = mustDates;
    }
    @Override
    public boolean checkConditions(List<CartItemInfo> basketItems) {
        Calendar now = Calendar.getInstance();
        for (Calendar date : mustDates) {
            if (now.get(6) == date.get(6)) //6 = DAY_OF_YEAR (1-365, or 1-366 on leap years)
                return true;
        }
        return false;
    }

    private String getMustDateString(Calendar mustDate)
    {
        return  mustDate.get(5) + "." + mustDate.get(2) + "." + mustDate.get(1);
    }

    @Override
    public String toString()
    {
        String result = "";
        for (Calendar mustDate : mustDates)
        {
            result += "; " + getMustDateString(mustDate);
        }
        if (result.length() > 1)
        {
            result = result.substring(2);
        }
        return  "(The purchase date is one of the following dates: " + result + ")";
    }

    public boolean isApplyForItem(int itemID, String category)
    {
        return false;
    }
}
