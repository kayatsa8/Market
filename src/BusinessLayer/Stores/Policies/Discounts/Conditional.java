package BusinessLayer.Stores.Policies.Discounts;

import java.util.Calendar;
import java.util.Map;

public class Conditional extends Discount{

    private Map<Integer, Integer> itemsIDsToAmount;
    public Conditional(Map<Integer, Integer> itemsIDsToAmount, double discount, Calendar endOfSale){
        super();
        this.itemsIDsToAmount = itemsIDsToAmount;
    }

    @Override
    public double getDiscountToItem() {
        return 0;
    }


}
