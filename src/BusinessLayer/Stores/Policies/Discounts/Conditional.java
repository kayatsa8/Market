package BusinessLayer.Stores.Policies.Discounts;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class Conditional extends Discount{

    private Map<Integer, Integer> itemsIDsToAmount;
    public Conditional(Map<Integer, Integer> itemsIDsToAmount, double discount, Calendar endOfSale){
        super();
        this.itemsIDsToAmount = itemsIDsToAmount;
    }

    public List<Integer> getItemsIDs()
    {
        return itemsIDsToAmount.keySet().stream().toList();
    }
    @Override
    public double getDiscountToItem() {
        if(isExpired())
        {
            return 0;
        }
        return getPercent();
    }

    public boolean isExpired() {
        Calendar now = Calendar.getInstance();
        return now.after(getExpiringDate());
    }
}
