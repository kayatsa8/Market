package BusinessLayer.Stores.Policies.Discounts;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Visible implements Discount{
    private int itemId;
    private double percent;
    private Calendar endOfSale;

    public Visible(int itemId, double percent, Calendar endOfSale){
        this.itemId = itemId;
        this.percent = percent;
        this.endOfSale = endOfSale;
    }

    @Override
    public List<CartItemInfo> updateBasket(List<CartItemInfo> basketItems, List<String> coupons) { //no need to use the coupons here
        List<CartItemInfo> copyBasket = new ArrayList<>();
        for (CartItemInfo item: basketItems)
        {
            copyBasket.add(new CartItemInfo(item));
        }
        for (CartItemInfo item : copyBasket) {
            if (item.getItemID() == itemId)
                item.setPercent(percent);
            else
                item.setPercent(0);
        }
        return copyBasket;
    }
}
