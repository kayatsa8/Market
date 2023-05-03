package BusinessLayer.Stores.Policies.Compositions.LogicalCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public interface LogicalComponent {
    boolean checkConditions(List<CartItemInfo> basketItems);
}