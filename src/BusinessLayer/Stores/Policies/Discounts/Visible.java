package BusinessLayer.Stores.Policies.Discounts;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Visible extends Discount{


    private int itemId;

    public Visible(int itemId, double discount, Calendar endOfSale){
        super();
        this.itemId = itemId;
    }

    public List<Integer> getItemsIDs()
    {
        List<Integer> item = new ArrayList<>();
        item.add(itemId);
        return item;
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
