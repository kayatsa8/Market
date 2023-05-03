package BusinessLayer.Stores.Policies.Discounts;

import BusinessLayer.CartAndBasket.CartItemInfo;
import BusinessLayer.Stores.Policies.Compositions.NumericCompositions.NumericComponent;

import java.util.List;

public class Conditional implements Discount{

    private NumericComponent numericComponent;

    public Conditional(NumericComponent numericComponent){
        this.numericComponent = numericComponent;
    }

    @Override
    public List<CartItemInfo> updateBasket(List<CartItemInfo> basketItems, List<String> coupons) {
        return numericComponent.updateBasket(basketItems);
    }
}
