package BusinessLayer.Stores.Policies.Compositions.NumericCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public interface NumericComponent {
    public List<CartItemInfo> updateBasket(List<CartItemInfo> basketItems);
}
