package BusinessLayer.Stores.Policies.Discounts;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.Calendar;
import java.util.List;

public interface Discount {
    List<CartItemInfo> updateBasket(List<CartItemInfo> basketItems, List<String> coupons);
}
