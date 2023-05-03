package BusinessLayer.Stores.Policies.Compositions.NumericCompositions;

import BusinessLayer.CartAndBasket.CartItemInfo;

import java.util.List;

public abstract class NumericComposite implements NumericComponent
{
    private List<NumericComponent> numericComponents;
    public NumericComposite(List<NumericComponent> numericComponents) {this.numericComponents = numericComponents;}
    public List<NumericComponent> getNumericComponents()
    {
        return numericComponents;
    }
    protected void copyBasket(List<CartItemInfo> copyTo, List<CartItemInfo> copyFrom) //"copyTo" should be an empty list
    {
        for (CartItemInfo item : copyFrom)
        {
            copyTo.add(new CartItemInfo(item));
        }
    }
}
